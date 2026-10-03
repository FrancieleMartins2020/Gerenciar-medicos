package br.edu.utfpr.td.tsi.medicos.controller;

import br.edu.utfpr.td.tsi.medicos.dto.MedicoRequestDTO;
import br.edu.utfpr.td.tsi.medicos.dto.MedicoResponseDTO;
import br.edu.utfpr.td.tsi.medicos.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(
            @Valid @RequestBody MedicoRequestDTO dto) {

        MedicoResponseDTO medico =
                service.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(medico);
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listar(

            @RequestParam(required = false)
            String nome,

            @RequestParam(required = false)
            String crm,

            @RequestParam(required = false)
            String especialidade) {

        return ResponseEntity.ok(
                service.listar(
                        nome,
                        crm,
                        especialidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> alterar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequestDTO dto) {

        return ResponseEntity.ok(
                service.alterar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
