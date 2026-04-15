package ateneu.sgcti.gsolicitantes.repository;

import ateneu.sgcti.gsolicitantes.entity.SolicitanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitanteRepository extends JpaRepository<SolicitanteEntity, Integer> {

    Optional<SolicitanteEntity> findByUsuarioEntity_Id(Integer usuarioId);
}

