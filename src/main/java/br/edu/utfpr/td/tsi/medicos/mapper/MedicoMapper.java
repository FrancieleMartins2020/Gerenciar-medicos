package br.edu.utfpr.td.tsi.medicos.mapper;

import br.edu.utfpr.td.tsi.medicos.dto.MedicoBrutoDTO;
import br.edu.utfpr.td.tsi.medicos.model.Endereco;
import br.edu.utfpr.td.tsi.medicos.model.Especialidade;
import br.edu.utfpr.td.tsi.medicos.model.Graduacao;
import br.edu.utfpr.td.tsi.medicos.model.Medico;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MedicoMapper {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Medico paraEntidade(MedicoBrutoDTO dto) {
        Medico m = new Medico();

        String nome = dto.nomeMedico();
        if (nome != null && nome.length() > 200) {
            nome = nome.substring(0, 197) + "...";
        }
        m.setNome(nome);
        m.setCrm(dto.nuCrm());
        m.setUf(dto.sgUf());

        String especialidadeBruta = dto.especialidade();
        if (especialidadeBruta != null && !especialidadeBruta.isBlank()) {
            String[] partes = especialidadeBruta.split("&|\\(Áreas de atuação:|\\)");

            for (String parte : partes) {
                String textoLimpo = parte.replace("\"", "").trim();
                if (textoLimpo.isBlank()) continue;

                Especialidade esp = new Especialidade();

                if (textoLimpo.contains("RQE Nº:")) {
                    String[] dadosEsp = textoLimpo.split("RQE Nº:");

                    String nomeEsp = dadosEsp[0].replace("-", "").trim();
                    esp.setNome(nomeEsp);

                    String numeroRqe = dadosEsp[1].trim();
                    esp.setRqe(numeroRqe);
                } else {
                    esp.setNome(textoLimpo);
                }

                esp.setMedico(m);
                m.getEspecialidades().add(esp);
            }
        }

        String tipoInsc = dto.tipoInscricao();
        if (tipoInsc != null && tipoInsc.length() > 30) tipoInsc = tipoInsc.substring(0, 30);
        m.setTipoInscricao(tipoInsc);

        String situacao = dto.situacao();
        if (situacao != null && situacao.length() > 50) situacao = situacao.substring(0, 50);
        m.setSituacao(situacao);

        try {
            String dtInscricao = dto.dtInscricao();
            if (dtInscricao != null && !dtInscricao.isBlank()) {
                m.setDataInscricao(LocalDate.parse(dtInscricao.trim(), dtf));
            }
            String primInscricao = dto.primInscricaoUf();
            if (primInscricao != null && !primInscricao.isBlank()) {
                m.setPrimeiraInscricaoUf(LocalDate.parse(primInscricao.trim(), dtf));
            }
        } catch (Exception e) {
            // Mantém nulo caso venha em formato quebrado e segue em frente
        }

        Graduacao g = new Graduacao();
        g.setInstituicao(dto.nmInstituicaoGraduacao());
        Object dtGraduacao = dto.dtGraduacao();
        if (dtGraduacao != null) {
            try {
                g.setAnoFormatura(Integer.parseInt(dtGraduacao.toString().trim()));
            } catch (Exception e) {
                g.setAnoFormatura(null);
            }
        }
        m.setGraduacao(g);

        Endereco end = new Endereco();
        end.setMunicipio(dto.municipio());
        end.setUf(dto.sgUf());

        String telefone = dto.telefone();
        if (telefone != null && telefone.length() > 30) {
            telefone = telefone.substring(0, 30);
        }
        end.setTelefone(telefone);
        m.setEndereco(end);

        return m;
    }
}