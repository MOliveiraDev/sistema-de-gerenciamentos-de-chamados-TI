package ateneu.sgcti.gchamados.entity;

import ateneu.sgcti.gchamados.enums.StatusChamado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chamados_historico_tbl")
public class ChamadoHistoricoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historico_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chamado_id", nullable = false)
    private ChamadoEntity chamado;

    @Column(name = "data_evento", nullable = false)
    private LocalDateTime dataEvento;

    @Column(name = "descricao_evento", nullable = false, length = 255)
    private String descricaoEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 45)
    private StatusChamado statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", length = 45)
    private StatusChamado statusNovo;

    @Column(name = "tecnico_anterior_id")
    private Integer tecnicoAnteriorId;

    @Column(name = "tecnico_novo_id")
    private Integer tecnicoNovoId;
}

