package br.edu.utfpr.td.tsi.medicos.exception;

public class RegraNegocioException
        extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
