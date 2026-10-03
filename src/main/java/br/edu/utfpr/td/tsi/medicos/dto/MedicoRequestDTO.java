package br.edu.utfpr.td.tsi.medicos.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class MedicoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome deve possuir pelo menos 3 caracteres")
    private String nome;

    @NotBlank(message = "O CRM é obrigatório")
    private String crm;

    @NotBlank(message = "A UF é obrigatória")
    @Pattern(
            regexp = "^[A-Za-z]{2}$",
            message = "A UF deve possuir 2 letras"
    )
    private String uf;

    @NotBlank(message = "A especialidade é obrigatória")
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

    public MedicoRequestDTO() {
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