package br.edu.utfpr.td.tsi.medicos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicoRequestDTO {
    private String nome;
    private String crm;
    private String uf;
    private String tipoInscricao;
    private String situacao;
    private LocalDate dataInscricao;
    private LocalDate primeiraInscricaoUf;

    private List<EspecialidadeDTO> especialidades = new ArrayList<>();
    private GraduacaoDTO graduacao;
    private EnderecoDTO endereco;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public String getTipoInscricao() { return tipoInscricao; }
    public void setTipoInscricao(String tipoInscricao) { this.tipoInscricao = tipoInscricao; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public LocalDate getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDate dataInscricao) { this.dataInscricao = dataInscricao; }
    public LocalDate getPrimeiraInscricaoUf() { return primeiraInscricaoUf; }
    public void setPrimeiraInscricaoUf(LocalDate primeiraInscricaoUf) { this.primeiraInscricaoUf = primeiraInscricaoUf; }
    public List<EspecialidadeDTO> getEspecialidades() {return especialidades;}
    public void setEspecialidades(List<EspecialidadeDTO> especialidades) { this.especialidades = especialidades; }
    public GraduacaoDTO getGraduacao() { return graduacao; }
    public void setGraduacao(GraduacaoDTO graduacao) { this.graduacao = graduacao; }
    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }
}