package ateneu.sgcti.gchamados.relatorios.repository;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}

