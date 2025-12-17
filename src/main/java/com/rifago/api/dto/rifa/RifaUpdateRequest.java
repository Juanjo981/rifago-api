package com.rifago.api.dto.rifa;

import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RifaUpdateRequest {

    private String titulo;
    private String descripcion;
    private LocalDateTime fechaRifa;
}
