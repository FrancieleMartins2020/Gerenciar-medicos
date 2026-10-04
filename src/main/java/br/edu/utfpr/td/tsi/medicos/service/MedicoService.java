package br.edu.utfpr.td.tsi.medicos.service;

import br.edu.utfpr.td.tsi.medicos.dto.EspecialidadeDTO;
import br.edu.utfpr.td.tsi.medicos.dto.MedicoRequestDTO;
import br.edu.utfpr.td.tsi.medicos.dto.MedicoResponseDTO;
import br.edu.utfpr.td.tsi.medicos.exception.MedicoNaoEncontradoException;
import br.edu.utfpr.td.tsi.medicos.exception.RegraNegocioException;
import br.edu.utfpr.td.tsi.medicos.model.Endereco;
import br.edu.utfpr.td.tsi.medicos.model.Especialidade;
import br.edu.utfpr.td.tsi.medicos.model.Graduacao;
import br.edu.utfpr.td.tsi.medicos.model.Medico;
import br.edu.utfpr.td.tsi.medicos.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class MedicoService {

    private final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public Page<MedicoResponseDTO> listar(String nome, String crm, String especialidade, Pageable pageable) {
        Page<Medico> medicos = repository.buscarComFiltros(nome, crm, especialidade, pageable);
        return medicos.map(MedicoResponseDTO::new);
    }

    public MedicoResponseDTO buscarPorId(Long id) {
        Medico medico = repository.findById(id)
                .orElseThrow(() -> new MedicoNaoEncontradoException("Médico com ID " + id + " não encontrado."));
        return new MedicoResponseDTO(medico);
    }

    public MedicoResponseDTO cadastrar(MedicoRequestDTO dto) {
        validarCamposObrigatorios(dto);
        validarCRM(dto.getCrm(), dto.getUf(), null);

        if (dto.getGraduacao() != null) {
            validarAnoGraduacao(dto.getGraduacao().getAnoFormatura());
        }
        validarConsistenciaDatas(dto.getDataInscricao(), dto.getPrimeiraInscricaoUf());

        Medico medico = new Medico();
        medico.setGraduacao(new Graduacao());
        medico.setEndereco(new Endereco());

        atualizarCampos(medico, dto);

        Medico salvo = repository.save(medico);
        return new MedicoResponseDTO(salvo);
    }

    public MedicoResponseDTO alterar(Long id, MedicoRequestDTO dto) {
        Medico medico = repository.findById(id)
                .orElseThrow(() -> new MedicoNaoEncontradoException("Médico com ID " + id + " não encontrado."));

        validarCamposObrigatorios(dto);
        validarCRM(dto.getCrm(), dto.getUf(), id);

        if (dto.getGraduacao() != null) {
            validarAnoGraduacao(dto.getGraduacao().getAnoFormatura());
        }
        validarConsistenciaDatas(dto.getDataInscricao(), dto.getPrimeiraInscricaoUf());

        atualizarCampos(medico, dto);

        Medico salvo = repository.save(medico);
        return new MedicoResponseDTO(salvo);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new MedicoNaoEncontradoException("Médico com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validarCRM(String crm, String uf, Long idAtual) {
        var existente = repository.findByCrmAndUf(crm.trim(), uf.trim());
        if (existente.isEmpty()) {
            return;
        }

        if (idAtual == null || !existente.get().getId().equals(idAtual)) {
            throw new RegraNegocioException("Não foi possível salvar o registro: Já existe um médico cadastrado com o CRM " + crm + " no estado " + uf);
        }
    }

    private void validarCamposObrigatorios(MedicoRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O campo 'nome' é obrigatório e não pode ficar em branco.");
        }
        if (dto.getCrm() == null || dto.getCrm().isBlank()) {
            throw new RegraNegocioException("O campo 'crm' é obrigatório e não pode ficar em branco.");
        }
        if (dto.getUf() == null || dto.getUf().isBlank()) {
            throw new RegraNegocioException("O campo 'uf' de inscrição é obrigatório.");
        }
    }

    private void validarAnoGraduacao(Integer anoFormatura) {
        if (anoFormatura != null) {
            int anoAtual = Year.now().getValue();
            if (anoFormatura > anoAtual) {
                throw new RegraNegocioException("O ano de formatura (" + anoFormatura + ") não pode ser maior que o ano atual (" + anoAtual + ").");
            }
        }
    }

    private void validarConsistenciaDatas(LocalDate dataInscricao, LocalDate primeiraInscricao) {
        LocalDate hoje = LocalDate.now();
        if (dataInscricao != null && dataInscricao.isAfter(hoje)) {
            throw new RegraNegocioException("A data de inscrição no conselho não pode estar no futuro.");
        }
        if (primeiraInscricao != null && primeiraInscricao.isAfter(hoje)) {
            throw new RegraNegocioException("A data de primeira inscrição na UF não pode estar no futuro.");
        }
    }

    private void atualizarCampos(Medico medico, MedicoRequestDTO dto) {
        medico.setNome(dto.getNome().trim());
        medico.setCrm(dto.getCrm().trim());
        medico.setUf(dto.getUf().trim().toUpperCase());
        medico.setTipoInscricao(dto.getTipoInscricao());
        medico.setSituacao(dto.getSituacao());
        medico.setDataInscricao(dto.getDataInscricao());
        medico.setPrimeiraInscricaoUf(dto.getPrimeiraInscricaoUf());

        medico.getEspecialidades().clear();
        if (dto.getEspecialidades() != null) {
            for (EspecialidadeDTO espDto : dto.getEspecialidades()) {
                if (espDto.getNome() != null && !espDto.getNome().isBlank()) {
                    Especialidade novaEsp = new Especialidade();
                    novaEsp.setNome(espDto.getNome().trim());
                    novaEsp.setRqe(espDto.getRqe() != null ? espDto.getRqe().trim() : null);
                    novaEsp.setMedico(medico);
                    medico.getEspecialidades().add(novaEsp);
                }
            }
        }

        if (dto.getGraduacao() != null) {
            medico.getGraduacao().setInstituicao(dto.getGraduacao().getInstituicao());
            medico.getGraduacao().setAnoFormatura(dto.getGraduacao().getAnoFormatura());
        }

        if (dto.getEndereco() != null) {
            medico.getEndereco().setLogradouro(dto.getEndereco().getLogradouro());
            medico.getEndereco().setNumero(dto.getEndereco().getNumero());
            medico.getEndereco().setComplemento(dto.getEndereco().getComplemento());
            medico.getEndereco().setBairro(dto.getEndereco().getBairro());
            medico.getEndereco().setMunicipio(dto.getEndereco().getMunicipio());
            medico.getEndereco().setUf(dto.getEndereco().getUf() != null ? dto.getEndereco().getUf().trim().toUpperCase() : null);
            medico.getEndereco().setCep(dto.getEndereco().getCep());
            medico.getEndereco().setTelefone(dto.getEndereco().getTelefone());
        }
    }
}