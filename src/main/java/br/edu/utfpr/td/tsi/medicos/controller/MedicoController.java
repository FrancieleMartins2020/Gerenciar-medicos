package br.edu.utfpr.td.tsi.medicos.controller;

import br.edu.utfpr.td.tsi.medicos.dto.MedicoRequestDTO;
import br.edu.utfpr.td.tsi.medicos.dto.MedicoResponseDTO;
import br.edu.utfpr.td.tsi.medicos.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(@Valid @RequestBody MedicoRequestDTO dto) {
        MedicoResponseDTO medico = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(medico);
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String crm,
            @RequestParam(required = false) String especialidade,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<MedicoResponseDTO> medicos = service.listar(nome, crm, especialidade, pageable);
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> alterar(@PathVariable Long id, @Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.ok(service.alterar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}