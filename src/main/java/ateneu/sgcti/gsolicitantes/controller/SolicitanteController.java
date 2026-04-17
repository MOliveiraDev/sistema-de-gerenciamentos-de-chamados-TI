package ateneu.sgcti.gsolicitantes.controller;

import ateneu.sgcti.gsolicitantes.dto.SolicitanteRequest;
import ateneu.sgcti.gsolicitantes.dto.SolicitanteResponse;
import ateneu.sgcti.gsolicitantes.service.SolicitanteService;
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
@RequestMapping("/api/solicitantes/v1")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SolicitanteController {

    private final SolicitanteService solicitanteService;

    @GetMapping
    public List<SolicitanteResponse> listar() {
        return solicitanteService.listarTodos();
    }

    @GetMapping("/{id}/buscar")
    public SolicitanteResponse buscarPorId(@PathVariable Integer id) {
        return solicitanteService.buscarPorId(id);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<SolicitanteResponse> cadastrar(@Valid @RequestBody SolicitanteRequest request) {
        SolicitanteResponse response = solicitanteService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/atualizar")
    public SolicitanteResponse atualizar(@PathVariable Integer id, @Valid @RequestBody SolicitanteRequest request) {
        return solicitanteService.atualizar(id, request);
    }

    @DeleteMapping("/{id}/deletar")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        solicitanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
