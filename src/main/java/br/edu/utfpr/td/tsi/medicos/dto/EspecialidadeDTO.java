package br.edu.utfpr.td.tsi.medicos.dto;

import br.edu.utfpr.td.tsi.medicos.model.Especialidade;

public class EspecialidadeDTO {
    private String nome;
    private String rqe;

    public EspecialidadeDTO() {}

    public EspecialidadeDTO(Especialidade especialidade) {
        if (especialidade != null) {
            this.nome = especialidade.getNome();
            this.rqe = especialidade.getRqe();
        }
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRqe() { return rqe; }
    public void setRqe(String rqe) { this.rqe = rqe; }
}
