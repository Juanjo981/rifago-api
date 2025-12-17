package com.rifago.api.modelos;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.enums.SorteoEstado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sorteo_sesion")
public class SorteoSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rifa_id", nullable = false)
    private Rifa rifa;

    @CreationTimestamp
    private OffsetDateTime startedAt;

    private OffsetDateTime endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private SorteoEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private AdminUser createdBy;
}
