package ateneu.sgcti.gchamados.controller;

import ateneu.sgcti.gchamados.dto.AtribuirTecnicoRequest;
import ateneu.sgcti.gchamados.dto.AtualizarStatusRequest;
import ateneu.sgcti.gchamados.dto.ChamadoHistoricoResponse;
import ateneu.sgcti.gchamados.dto.ChamadoRequest;
import ateneu.sgcti.gchamados.dto.ChamadoResponse;
import ateneu.sgcti.gchamados.dto.ChamadoUpdateRequest;
import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import ateneu.sgcti.gchamados.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO','SOLICITANTE')")
    public List<ChamadoResponse> listar(
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) PrioridadeChamado prioridade,
            @RequestParam(required = false) Integer tecnicoId,
            @RequestParam(required = false) Integer solicitanteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return chamadoService.listar(status, prioridade, tecnicoId, solicitanteId, inicio, fim);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SOLICITANTE')")
    public ChamadoResponse buscarPorId(@PathVariable Integer id) {
        return chamadoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SOLICITANTE')")
    public ResponseEntity<ChamadoResponse> registrar(@Valid @RequestBody ChamadoRequest request) {
        return ResponseEntity.ok(chamadoService.registrar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public ChamadoResponse atualizar(@PathVariable Integer id, @Valid @RequestBody ChamadoUpdateRequest request) {
        return chamadoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        chamadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/atribuir-tecnico")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public ChamadoResponse atribuirTecnico(@PathVariable Integer id,
                                           @Valid @RequestBody AtribuirTecnicoRequest request) {
        return chamadoService.atribuirTecnico(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public ChamadoResponse atualizarStatus(@PathVariable Integer id,
                                           @Valid @RequestBody AtualizarStatusRequest request) {
        return chamadoService.atualizarStatus(id, request);
    }

    @PatchMapping("/{id}/atender")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO')")
    public ChamadoResponse atenderChamado(@PathVariable Integer id,
                                          @Valid @RequestBody AtribuirTecnicoRequest request) {
        return chamadoService.atenderChamado(id, request);
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasAnyRole('ADMIN','TECNICO','SOLICITANTE')")
    public List<ChamadoHistoricoResponse> listarHistorico(@PathVariable Integer id) {
        return chamadoService.listarHistorico(id);
    }

}

