package ateneu.sgcti.gchamados.relatorios.controller;

import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorPeriodoResponse;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorStatusResponse;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorTecnicoResponse;
import ateneu.sgcti.gchamados.relatorios.service.ChamadoRelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/chamados/relatorios")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ChamadoRelatorioController {

    private final ChamadoRelatorioService chamadoRelatorioService;

    @GetMapping("/por-periodo")
    public RelatorioPorPeriodoResponse relatorioPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return chamadoRelatorioService.relatorioPorPeriodo(inicio, fim);
    }

    @GetMapping("/por-status")
    public List<RelatorioPorStatusResponse> relatorioPorStatus() {
        return chamadoRelatorioService.relatorioPorStatus();
    }

    @GetMapping("/por-tecnico")
    public List<RelatorioPorTecnicoResponse> relatorioPorTecnico() {
        return chamadoRelatorioService.relatorioPorTecnico();
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportarPdf(
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) PrioridadeChamado prioridade,
            @RequestParam(required = false) Integer tecnicoId,
            @RequestParam(required = false) Integer solicitanteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        byte[] pdf = chamadoRelatorioService.exportarRelatorioPdf(status, prioridade, tecnicoId, solicitanteId, inicio, fim);
        String nomeArquivo = "relatorio-chamados.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

