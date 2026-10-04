package br.edu.utfpr.td.tsi.medicos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "especialidade")
public class Especialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String rqe;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    public Especialidade(Especialidade especialidade) { }

    public Especialidade() {  }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRqe() { return rqe; }
    public void setRqe(String rqe) { this.rqe = rqe; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
}
