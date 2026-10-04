package br.edu.utfpr.td.tsi.medicos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medico", uniqueConstraints = {
        @UniqueConstraint(name = "uk_medico_crm_uf", columnNames = {"crm", "uf"})
})
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 20)
    private String crm;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(name = "tipo_inscricao", length = 30)
    private String tipoInscricao;

    @Column(length = 50)
    private String situacao;

    @Column(name = "data_inscricao")
    private LocalDate dataInscricao;

    @Column(name = "primeira_inscricao_uf")
    private LocalDate primeiraInscricaoUf;

    @OneToMany(mappedBy = "medico", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Especialidade> especialidades = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "graduacao_id", referencedColumnName = "id")
    private Graduacao graduacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    public List<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidade> especialidades) { this.especialidades = especialidades; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public void setTipoInscricao(String tipoInscricao) { this.tipoInscricao = tipoInscricao; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public LocalDate getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDate dataInscricao) { this.dataInscricao = dataInscricao; }
    public LocalDate getPrimeiraInscricaoUf() { return primeiraInscricaoUf; }
    public void setPrimeiraInscricaoUf(LocalDate primeiraInscricaoUf) { this.primeiraInscricaoUf = primeiraInscricaoUf; }
    public Graduacao getGraduacao() { return graduacao; }
    public void setGraduacao(Graduacao graduacao) { this.graduacao = graduacao; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public String getTipoInscricao() { return tipoInscricao;}
}