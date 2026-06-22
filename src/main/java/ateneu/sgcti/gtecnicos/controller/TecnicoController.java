package ateneu.sgcti.gtecnicos.controller;

import ateneu.sgcti.gtecnicos.dto.TecnicoRequest;
import ateneu.sgcti.gtecnicos.dto.TecnicoResponse;
import ateneu.sgcti.gtecnicos.dto.TecnicoUpdateRequest;
import ateneu.sgcti.gtecnicos.service.TecnicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public List<TecnicoResponse> listar() {
        return tecnicoService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TecnicoResponse buscarPorId(@PathVariable Integer id) {
        return tecnicoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TecnicoResponse> cadastrar(@Valid @RequestBody TecnicoRequest request) {
        TecnicoResponse response = tecnicoService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TecnicoResponse atualizar(@PathVariable Integer id, @Valid @RequestBody TecnicoUpdateRequest request) {
        return tecnicoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        tecnicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
