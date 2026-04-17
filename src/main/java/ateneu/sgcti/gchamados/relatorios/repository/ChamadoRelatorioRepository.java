package ateneu.sgcti.gchamados.relatorios.repository;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChamadoRelatorioRepository extends JpaRepository<ChamadoEntity, Integer> {

    long countByDataAberturaBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("select c.status, count(c) from ChamadoEntity c group by c.status")
    List<Object[]> contarPorStatus();

    @Query("""
            select t.id, u.nome, count(c)
            from ChamadoEntity c
            left join c.tecnicoEntity t
            left join t.usuarioEntity u
            group by t.id, u.nome
            """)
    List<Object[]> contarPorTecnico();

    @Query("""
            select c from ChamadoEntity c
            join fetch c.solicitanteEntity s
            join fetch s.usuarioEntity su
            left join fetch c.tecnicoEntity t
            left join fetch t.usuarioEntity tu
            where (:status is null or c.status = :status)
              and (:prioridade is null or c.prioridade = :prioridade)
              and (:tecnicoId is null or t.id = :tecnicoId)
              and (:solicitanteId is null or s.id = :solicitanteId)
              and (:inicio is null or c.dataAbertura >= :inicio)
              and (:fim is null or c.dataAbertura <= :fim)
            order by c.dataAbertura desc
            """)
    List<ChamadoEntity> buscarParaRelatorioPdf(
            @Param("status") StatusChamado status,
            @Param("prioridade") PrioridadeChamado prioridade,
            @Param("tecnicoId") Integer tecnicoId,
            @Param("solicitanteId") Integer solicitanteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}

