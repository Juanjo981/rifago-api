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
        name = "participante",
        uniqueConstraints = @UniqueConstraint(columnNames = {"rifa_id", "telefono"})
)
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rifa_id", nullable = false)
    private Rifa rifa;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
