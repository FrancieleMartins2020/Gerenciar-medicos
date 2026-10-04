package br.edu.utfpr.td.tsi.medicos.exception;

public class ColecaoVaziaException extends RuntimeException {
    public ColecaoVaziaException(String message) {
        super(message);
    }
}