package br.edu.utfpr.td.tsi.medicos.dto;

import br.edu.utfpr.td.tsi.medicos.model.Graduacao;

public class GraduacaoDTO {
    private Long id;
    private String instituicao;
    private Integer anoFormatura;
    public GraduacaoDTO() {
    }
    public GraduacaoDTO(Graduacao graduacao) {
        if (graduacao != null) {
            this.instituicao = graduacao.getInstituicao();
            this.anoFormatura = graduacao.getAnoFormatura();
        }
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { this.instituicao = instituicao; }
    public Integer getAnoFormatura() { return anoFormatura; }
    public void setAnoFormatura(Integer anoFormatura) { this.anoFormatura = anoFormatura; }
}
