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
        name = "sorteo_premio_config",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sorteo_sesion_id", "premio_id"}
        )
)
public class SorteoPremioConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorteo_sesion_id", nullable = false)
    private SorteoSesion sorteoSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "premio_id", nullable = false)
    private Premio premio;

    @Column(name = "total_salidas", nullable = false)
    private Integer totalSalidas;

    @Column(name = "ganador_en", nullable = false)
    private Integer ganadorEn;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
