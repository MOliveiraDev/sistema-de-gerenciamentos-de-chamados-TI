package ateneu.sgcti.gchamados.repository;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<ChamadoEntity, Integer> {

    List<ChamadoEntity> findBySolicitanteEntity_Id(Integer solicitanteId);

    List<ChamadoEntity> findByTecnicoEntity_Id(Integer tecnicoId);

    List<ChamadoEntity> findByStatus(StatusChamado status);

    List<ChamadoEntity> findByPrioridade(PrioridadeChamado prioridade);

    boolean existsBySolicitanteEntity_Id(Integer solicitanteId);

    boolean existsByTecnicoEntity_Id(Integer tecnicoId);

    @Query("""
            select c from ChamadoEntity c
            where (:status is null or c.status = :status)
              and (:prioridade is null or c.prioridade = :prioridade)
              and (:tecnicoId is null or c.tecnicoEntity.id = :tecnicoId)
              and (:solicitanteId is null or c.solicitanteEntity.id = :solicitanteId)
            """)
    List<ChamadoEntity> buscarComFiltros(
            @Param("status") StatusChamado status,
            @Param("prioridade") PrioridadeChamado prioridade,
            @Param("tecnicoId") Integer tecnicoId,
            @Param("solicitanteId") Integer solicitanteId
    );

}

