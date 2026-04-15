package ateneu.sgcti.gchamados.repository;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<ChamadoEntity, Integer> {

    List<ChamadoEntity> findBySolicitanteEntity_Id(Integer solicitanteId);

    List<ChamadoEntity> findByTecnicoEntity_Id(Integer tecnicoId);

    List<ChamadoEntity> findByStatus(String status);

    List<ChamadoEntity> findByPrioridade(String prioridade);

    boolean existsBySolicitanteEntity_Id(Integer solicitanteId);
}

