package br.edu.utfpr.td.tsi.medicos.exception;

public class MedicoNaoEncontradoException
        extends RuntimeException {
    public MedicoNaoEncontradoException(String id) {
        super("Médico não encontrado: " + id);
    }
}