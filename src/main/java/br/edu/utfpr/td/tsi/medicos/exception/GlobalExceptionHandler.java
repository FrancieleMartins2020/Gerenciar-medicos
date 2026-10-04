package br.edu.utfpr.td.tsi.medicos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Violação de Regra de Negócio",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MedicoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(MedicoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Recurso Não Encontrado",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ImportacaoException.class)
    public ResponseEntity<Map<String, Object>> tratarImportacaoException(ImportacaoException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Falha Operacional Híbrida",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ColecaoVaziaException.class)
    public ResponseEntity<Map<String, Object>> tratarColecaoVaziaException(ColecaoVaziaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Coleção Inexistente ou Limpa",
                "message", ex.getMessage()
        ));
    }
}