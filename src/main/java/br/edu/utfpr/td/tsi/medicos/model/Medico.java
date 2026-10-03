package br.edu.utfpr.td.tsi.medicos.model;

import java.time.LocalDate;

public class Medico {

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

    public Medico() {
    }

    public Medico(
            Long id,
            String nome,
            String crm,
            String uf,
            String especialidade,
            String areaAtuacao,
            String tipoInscricao,
            String situacao,
            String municipio,
            LocalDate dataInscricao,
            LocalDate primeiraInscricaoUf,
            String endereco,
            String telefone,
            String instituicaoGraduacao,
            Integer anoFormatura) {

        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.uf = uf;
        this.especialidade = especialidade;
        this.areaAtuacao = areaAtuacao;
        this.tipoInscricao = tipoInscricao;
        this.situacao = situacao;
        this.municipio = municipio;
        this.dataInscricao = dataInscricao;
        this.primeiraInscricaoUf = primeiraInscricaoUf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.instituicaoGraduacao = instituicaoGraduacao;
        this.anoFormatura = anoFormatura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public String getTipoInscricao() {
        return tipoInscricao;
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public LocalDate getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDate dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public LocalDate getPrimeiraInscricaoUf() {
        return primeiraInscricaoUf;
    }

    public void setPrimeiraInscricaoUf(LocalDate primeiraInscricaoUf) {
        this.primeiraInscricaoUf = primeiraInscricaoUf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getInstituicaoGraduacao() {
        return instituicaoGraduacao;
    }

    public void setInstituicaoGraduacao(String instituicaoGraduacao) {
        this.instituicaoGraduacao = instituicaoGraduacao;
    }

    public Integer getAnoFormatura() {
        return anoFormatura;
    }

    public void setAnoFormatura(Integer anoFormatura) {
        this.anoFormatura = anoFormatura;
    }
}