package ateneu.sgcti.gchamados.repository;

import ateneu.sgcti.gchamados.entity.ChamadoHistoricoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoHistoricoRepository extends JpaRepository<ChamadoHistoricoEntity, Integer> {

    List<ChamadoHistoricoEntity> findByChamado_IdOrderByDataEventoDesc(Integer chamadoId);

    void deleteByChamado_Id(Integer chamadoId);
}

