package com.rifago.api.modelos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "sorteo_evento",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sorteo_sesion_id", "premio_id", "indice_salida"}
        )
)
public class SorteoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorteo_sesion_id", nullable = false)
    private SorteoSesion sorteoSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "premio_id", nullable = false)
    private Premio premio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @Column(nullable = false)
    private Integer indiceSalida;

    @Builder.Default
    private Boolean esGanador = false;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
