package com.rifago.api.dto.publico;

import com.rifago.api.enums.RifaEstado;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RifaPublicInfoResponse {

    private String codigo;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaRifa;
    private RifaEstado estado;
}
