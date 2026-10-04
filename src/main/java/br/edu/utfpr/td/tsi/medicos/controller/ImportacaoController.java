package br.edu.utfpr.td.tsi.medicos.controller;

import br.edu.utfpr.td.tsi.medicos.service.ImportacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/importacao")
@CrossOrigin(origins = "*")
public class ImportacaoController {

    private final ImportacaoService service;

    public ImportacaoController(ImportacaoService service) {
        this.service = service;
    }

    @PostMapping("/sincronizar")
    public ResponseEntity<Map<String, Object>> sincronizarBancos() {
        long tempoInicio = System.currentTimeMillis();

        int totalInseridos = service.processarCargaDoMongoParaMysql();

        long tempoFim = System.currentTimeMillis();
        double tempoTotalSegundos = (tempoFim - tempoInicio) / 1000.0;

        return ResponseEntity.ok(Map.of(
                "status", "Sucesso",
                "mensagem", "Dados normalizados do MongoDB para o MySQL com sucesso.",
                "registrosSincronizados", totalInseridos,
                "tempoDeExecucaoSegundos", tempoTotalSegundos
        ));
    }
    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, Object>> obterRegistros() {
        return ResponseEntity.ok(service.gerarRelatorioDiferenca());
    }
}