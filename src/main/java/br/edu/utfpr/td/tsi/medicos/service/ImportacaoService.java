package br.edu.utfpr.td.tsi.medicos.service;

import br.edu.utfpr.td.tsi.medicos.dto.MedicoBrutoDTO;
import br.edu.utfpr.td.tsi.medicos.mapper.MedicoMapper;
import br.edu.utfpr.td.tsi.medicos.model.Endereco;
import br.edu.utfpr.td.tsi.medicos.model.Especialidade;
import br.edu.utfpr.td.tsi.medicos.model.Graduacao;
import br.edu.utfpr.td.tsi.medicos.model.Medico;
import br.edu.utfpr.td.tsi.medicos.repository.MedicoRepository;
import br.edu.utfpr.td.tsi.medicos.exception.ImportacaoException;
import br.edu.utfpr.td.tsi.medicos.exception.ColecaoVaziaException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImportacaoService {

    private static final Logger log = LoggerFactory.getLogger(ImportacaoService.class);

    private final MedicoRepository mysqlRepository;
    private final MongoTemplate mongoTemplate;
    private final MedicoMapper medicoMapper;

    public ImportacaoService(MedicoRepository mysqlRepository, MongoTemplate mongoTemplate, MedicoMapper medicoMapper) {
        this.mysqlRepository = mysqlRepository;
        this.mongoTemplate = mongoTemplate;
        this.medicoMapper = medicoMapper;
    }

    public int processarCargaDoMongoParaMysql() {
        log.info("======= INICIANDO NORMALIZAÇÃO DE DADOS (MONGO -> MYSQL) =======");

        long totalNoMongo = 0;
        try {
            totalNoMongo = mongoTemplate.getCollection("medicos").countDocuments();
            log.info("[MONGO] Total de registros brutos identificados no Mongo: {}", totalNoMongo);
        } catch (Exception e) {
            throw new ImportacaoException("Falha ao tentar ler ou contar os documentos da base do MongoDB.", e);
        }

        if (totalNoMongo == 0) {
            throw new ColecaoVaziaException("A sincronização falhou: A coleção 'medicos' está vazia no MongoDB.");
        }

        int totalProcessadoESalvo = 0;
        int tamanhoLote = 1000;
        int skip = 0;
        boolean existemMaisRegistros = true;

        while (existemMaisRegistros) {
            log.info("[LOTE] Buscando medicos - Quantidade: {}, Limite: {}", skip, tamanhoLote);

            List<MedicoBrutoDTO> loteMongo;
            try {
                Query query = new Query().skip(skip).limit(tamanhoLote);
                loteMongo = mongoTemplate.find(query, MedicoBrutoDTO.class, "medicos");
            } catch (Exception e) {
                throw new ImportacaoException("Erro ao extrair os dados paginados do MongoDB: " + skip, e);
            }

            if (loteMongo == null || loteMongo.isEmpty()) {
                log.info("[LOTE] Todos os dados brutos do MongoDB foram percorridos.");
                existemMaisRegistros = false;
                break;
            }

            List<Medico> loteParaSalvarNoMysql = new ArrayList<>();

            for (MedicoBrutoDTO doc : loteMongo) {
                String crm = doc.nuCrm();
                String uf = doc.sgUf();

                if (crm == null || uf == null) {
                    continue;
                }

                if (mysqlRepository.existsByCrmAndUf(crm, uf)) {
                    continue;
                }

                Medico m = medicoMapper.paraEntidade(doc);

                loteParaSalvarNoMysql.add(m);
                totalProcessadoESalvo++;
            }

            if (!loteParaSalvarNoMysql.isEmpty()) {
                try {
                    log.info("[MYSQL] Inserindo os dados normalizados de {} médicos no MySQL", loteParaSalvarNoMysql.size());
                    mysqlRepository.saveAll(loteParaSalvarNoMysql);
                } catch (Exception e) {
                    throw new ImportacaoException("Erro ao persistir médicos normalizados nas tabelas relacionais do MySQL.", e);
                }
            }

            skip += tamanhoLote;
        }

        return totalProcessadoESalvo;
    }

    public Map<String, Object> gerarRelatorioDiferenca() {
        log.info("[RELATÓRIO] Executando busca dos dados");

        long totalNoMongo;
        long totalNoMysql;

        try {
            totalNoMongo = mongoTemplate.getCollection("medicos").countDocuments();
            totalNoMysql = mysqlRepository.count();
        } catch (Exception e) {
            throw new ImportacaoException("Não foi possível gerar os relatórios pois um dos servidores de banco de dados ficou indisponível.", e);
        }

        java.util.Set<String> crmsUnicosNoMongo = new java.util.HashSet<>();
        int duplicadosInternosNoMongo = 0;
        int dadosBrutosInvalidos = 0;
        int naoEncontradosNoMysql = 0;

        List<String> amostraNaoEncontrados = new ArrayList<>();

        try (com.mongodb.client.MongoCursor<Document> cursor = mongoTemplate.getDb().getCollection("medicos").find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String crm = doc.getString("NU_CRM");
                String uf = doc.getString("SG_UF");

                if (crm == null || crm.isBlank() || uf == null || uf.isBlank()) {
                    dadosBrutosInvalidos++;
                    continue;
                }

                String chaveComposta = crm.trim() + "-" + uf.trim().toUpperCase();

                if (crmsUnicosNoMongo.contains(chaveComposta)) {
                    duplicadosInternosNoMongo++;
                    continue;
                }
                crmsUnicosNoMongo.add(chaveComposta);

                boolean existeNoMysql = mysqlRepository.existsByCrmAndUf(crm.trim(), uf.trim());
                if (!existeNoMysql) {
                    naoEncontradosNoMysql++;
                    if (amostraNaoEncontrados.size() < 20) {
                        amostraNaoEncontrados.add("CRM: " + crm + " / UF: " + uf + " (Falta no MySQL)");
                    }
                }
            }
        }
        long registrosEsperadosNoMysql = totalNoMongo - duplicadosInternosNoMongo - dadosBrutosInvalidos;
        long diferencaInexplicavel = Math.abs(registrosEsperadosNoMysql - totalNoMysql);
        return Map.of(
                "registrosNoMongoDB", totalNoMongo,
                "registrosNoMySQL", totalNoMysql,
                "duplicidadesInternasNoMongoOmitidas", duplicadosInternosNoMongo,
                "documentosSemCamposObrigatoriosNoMongo", dadosBrutosInvalidos,
                "registrosVálidosFaltantesNoMysql", naoEncontradosNoMysql,
                "resultados", Map.of(
                        "totalEsperadoCalulado", registrosEsperadosNoMysql,
                        "diferencas", diferencaInexplicavel,
                        "conclusao", diferencaInexplicavel == 0 ? "Operação bem sucedida." : "Atenção: Perda de dados"
                ),
                "amostraMedicosAusentesNoMysql", amostraNaoEncontrados
        );
    }
}