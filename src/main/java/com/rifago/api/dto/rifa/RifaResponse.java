package com.rifago.api.dto.rifa;

import com.rifago.api.enums.RifaEstado;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RifaResponse {

    private Long id;
    private String codigo;
    private String titulo;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime fechaRifa;
    private RifaEstado estado;
}
