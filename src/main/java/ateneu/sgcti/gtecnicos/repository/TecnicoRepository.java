package ateneu.sgcti.gtecnicos.repository;

import ateneu.sgcti.gtecnicos.entity.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<TecnicoEntity, Integer> {

    Optional<TecnicoEntity> findByUsuarioEntity_Id(Integer usuarioId);

    Optional<TecnicoEntity> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}

