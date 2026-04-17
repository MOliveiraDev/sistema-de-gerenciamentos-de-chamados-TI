package ateneu.sgcti.gchamados.relatorios.service;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.exception.ChamadoBusinessException;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPdfFiltro;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorPeriodoResponse;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorStatusResponse;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPorTecnicoResponse;
import ateneu.sgcti.gchamados.relatorios.repository.ChamadoRelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChamadoRelatorioService {

    private final ChamadoRelatorioRepository chamadoRelatorioRepository;
    private final ChamadoPdfGenerator chamadoPdfGenerator;

    public RelatorioPorPeriodoResponse relatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new ChamadoBusinessException("A data inicial não pode ser maior que a data final.");
        }

        LocalDateTime inicioPeriodo = inicio.atStartOfDay();
        LocalDateTime fimPeriodo = fim.atTime(LocalTime.MAX);

        long total = chamadoRelatorioRepository.countByDataAberturaBetween(inicioPeriodo, fimPeriodo);
        return new RelatorioPorPeriodoResponse(inicio, fim, total);
    }

    public List<RelatorioPorStatusResponse> relatorioPorStatus() {
        return chamadoRelatorioRepository.contarPorStatus().stream()
                .map(item -> new RelatorioPorStatusResponse(
                        (StatusChamado) item[0],
                        ((Number) item[1]).longValue()
                ))
                .toList();
    }

    public List<RelatorioPorTecnicoResponse> relatorioPorTecnico() {
        return chamadoRelatorioRepository.contarPorTecnico().stream()
                .map(item -> {
                    Integer tecnicoId = item[0] == null ? null : ((Number) item[0]).intValue();
                    String nomeTecnico = item[1] == null ? "Não atribuído" : item[1].toString();
                    long total = ((Number) item[2]).longValue();
                    return new RelatorioPorTecnicoResponse(tecnicoId, nomeTecnico, total);
                })
                .toList();
    }

    public byte[] exportarRelatorioPdf(StatusChamado status,
                                       PrioridadeChamado prioridade,
                                       Integer tecnicoId,
                                       Integer solicitanteId,
                                       LocalDate inicio,
                                       LocalDate fim) {
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new ChamadoBusinessException("A data inicial não pode ser maior que a data final.");
        }

        LocalDateTime inicioPeriodo = inicio == null ? null : inicio.atStartOfDay();
        LocalDateTime fimPeriodo = fim == null ? null : fim.atTime(LocalTime.MAX);

        List<ChamadoEntity> chamados = chamadoRelatorioRepository.buscarParaRelatorioPdf(
                status,
                prioridade,
                tecnicoId,
                solicitanteId,
                inicioPeriodo,
                fimPeriodo
        );

        RelatorioPdfFiltro filtro = new RelatorioPdfFiltro(status, prioridade, tecnicoId, solicitanteId, inicio, fim);
        return chamadoPdfGenerator.gerarRelatorioChamadosPdf(chamados, filtro);
    }
}

