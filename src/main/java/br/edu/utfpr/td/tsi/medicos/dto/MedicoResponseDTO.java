package br.edu.utfpr.td.tsi.medicos.dto;

import br.edu.utfpr.td.tsi.medicos.model.Medico;
import java.time.LocalDate;

public class MedicoResponseDTO {

    private Long id;

    private String nome;

    private String crm;

    private String uf;

    private String especialidade;

    private String areaAtuacao;

    private String tipoInscricao;

    private String situacao;

    private String municipio;

    private LocalDate dataInscricao;

    private LocalDate primeiraInscricaoUf;

    private String endereco;

    private String telefone;

    private String instituicaoGraduacao;

    private Integer anoFormatura;

    public MedicoResponseDTO(Medico medico) {

        this.id = medico.getId();
        this.nome = medico.getNome();
        this.crm = medico.getCrm();
        this.uf = medico.getUf();
        this.especialidade = medico.getEspecialidade();
        this.areaAtuacao = medico.getAreaAtuacao();
        this.tipoInscricao = medico.getTipoInscricao();
        this.situacao = medico.getSituacao();
        this.municipio = medico.getMunicipio();
        this.dataInscricao = medico.getDataInscricao();
        this.primeiraInscricaoUf = medico.getPrimeiraInscricaoUf();
        this.endereco = medico.getEndereco();
        this.telefone = medico.getTelefone();
        this.instituicaoGraduacao = medico.getInstituicaoGraduacao();
        this.anoFormatura = medico.getAnoFormatura();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCrm() {
        return crm;
    }

    public String getUf() {
        return uf;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public String getTipoInscricao() {
        return tipoInscricao;
    }

    public String getSituacao() {
        return situacao;
    }

    public String getMunicipio() {
        return municipio;
    }

    public LocalDate getDataInscricao() {
        return dataInscricao;
    }

    public LocalDate getPrimeiraInscricaoUf() {
        return primeiraInscricaoUf;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getInstituicaoGraduacao() {
        return instituicaoGraduacao;
    }

    public Integer getAnoFormatura() {
        return anoFormatura;
    }
}