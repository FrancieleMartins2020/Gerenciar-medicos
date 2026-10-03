package br.edu.utfpr.td.tsi.medicos.service;

import br.edu.utfpr.td.tsi.medicos.dto.MedicoRequestDTO;
import br.edu.utfpr.td.tsi.medicos.dto.MedicoResponseDTO;
import br.edu.utfpr.td.tsi.medicos.exception.MedicoNaoEncontradoException;
import br.edu.utfpr.td.tsi.medicos.exception.RegraNegocioException;
import br.edu.utfpr.td.tsi.medicos.model.Medico;
import br.edu.utfpr.td.tsi.medicos.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public MedicoResponseDTO cadastrar(
            MedicoRequestDTO dto) {

        validarDatas(dto);

        validarAnoFormatura(dto);

        validarCRM(dto.getCrm(), dto.getUf(), null);

        Medico medico = converterParaModel(dto);

        Medico salvo = repository.save(medico);

        return new MedicoResponseDTO(salvo);
    }

    public List<MedicoResponseDTO> listar(
            String nome,
            String crm,
            String especialidade) {

        return repository.findAll()
                .stream()
                .filter(medico ->
                        nome == null
                                || nome.isBlank()
                                || medico.getNome()
                                .toLowerCase()
                                .contains(nome.toLowerCase()))
                .filter(medico ->
                        crm == null
                                || crm.isBlank()
                                || medico.getCrm()
                                .equalsIgnoreCase(crm))
                .filter(medico ->
                        especialidade == null
                                || especialidade.isBlank()
                                || medico.getEspecialidade()
                                .toLowerCase()
                                .contains(especialidade.toLowerCase()))
                .map(MedicoResponseDTO::new)
                .toList();
    }

    public MedicoResponseDTO buscarPorId(Long id) {

        Medico medico = repository.findById(id)
                .orElseThrow(() ->
                        new MedicoNaoEncontradoException(id));

        return new MedicoResponseDTO(medico);
    }

    public MedicoResponseDTO alterar(
            Long id,
            MedicoRequestDTO dto) {

        Medico medico = repository.findById(id)
                .orElseThrow(() ->
                        new MedicoNaoEncontradoException(id));

        validarDatas(dto);

        validarAnoFormatura(dto);

        validarCRM(dto.getCrm(), dto.getUf(), id);

        atualizarModel(medico, dto);

        return new MedicoResponseDTO(
                repository.save(medico));
    }

    public void excluir(Long id) {

        if (!repository.existsById(id)) {

            throw new MedicoNaoEncontradoException(id);
        }

        repository.deleteById(id);
    }

    private Medico converterParaModel(
            MedicoRequestDTO dto) {

        Medico medico = new Medico();

        atualizarModel(medico, dto);

        return medico;
    }

    private void atualizarModel(
            Medico medico,
            MedicoRequestDTO dto) {

        medico.setNome(dto.getNome().trim());

        medico.setCrm(dto.getCrm().trim());

        medico.setUf(dto.getUf().trim().toUpperCase());

        medico.setEspecialidade(
                dto.getEspecialidade().trim());

        medico.setAreaAtuacao(dto.getAreaAtuacao());

        medico.setTipoInscricao(dto.getTipoInscricao());

        medico.setSituacao(dto.getSituacao());

        medico.setMunicipio(dto.getMunicipio());

        medico.setDataInscricao(dto.getDataInscricao());

        medico.setPrimeiraInscricaoUf(
                dto.getPrimeiraInscricaoUf());

        medico.setEndereco(dto.getEndereco());

        medico.setTelefone(dto.getTelefone());

        medico.setInstituicaoGraduacao(
                dto.getInstituicaoGraduacao());

        medico.setAnoFormatura(
                dto.getAnoFormatura());
    }

    private void validarCRM(
            String crm,
            String uf,
            Long idExcluir) {

        boolean existe = repository.existsByCrmAndUf(
                crm,
                uf,
                idExcluir);

        if (existe) {

            throw new RegraNegocioException(
                    "Já existe um médico cadastrado com o CRM "
                            + crm
                            + "/"
                            + uf);
        }
    }

    private void validarDatas(
            MedicoRequestDTO dto) {

        if (dto.getDataInscricao() != null
                && dto.getDataInscricao()
                .isAfter(LocalDate.now())) {

            throw new RegraNegocioException(
                    "A data de inscrição não pode ser futura");
        }

        if (dto.getPrimeiraInscricaoUf() != null
                && dto.getPrimeiraInscricaoUf()
                .isAfter(LocalDate.now())) {

            throw new RegraNegocioException(
                    "A primeira inscrição na UF não pode ser futura");
        }

        if (dto.getDataInscricao() != null
                && dto.getPrimeiraInscricaoUf() != null
                && dto.getDataInscricao()
                .isBefore(dto.getPrimeiraInscricaoUf())) {

            throw new RegraNegocioException(
                    "A data de inscrição não pode ser anterior à primeira inscrição na UF");
        }
    }

    private void validarAnoFormatura(
            MedicoRequestDTO dto) {

        if (dto.getAnoFormatura() == null) {
            return;
        }

        int anoAtual = LocalDate.now().getYear();

        if (dto.getAnoFormatura() < 1900
                || dto.getAnoFormatura() > anoAtual) {

            throw new RegraNegocioException(
                    "Ano de formatura inválido");
        }
    }
}