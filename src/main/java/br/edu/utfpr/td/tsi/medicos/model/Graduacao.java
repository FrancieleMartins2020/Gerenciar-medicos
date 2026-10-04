package br.edu.utfpr.td.tsi.medicos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "graduacao")
public class Graduacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String instituicao;
    @Column(name = "ano_formatura")
    private Integer anoFormatura;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { this.instituicao = instituicao; }
    public Integer getAnoFormatura() { return anoFormatura; }
    public void setAnoFormatura(Integer anoFormatura) { this.anoFormatura = anoFormatura; }
}