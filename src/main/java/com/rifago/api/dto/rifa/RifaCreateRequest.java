package com.rifago.api.dto.rifa;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RifaCreateRequest {

    @NotBlank
    private String codigo;

    @NotBlank
    private String titulo;

    private String descripcion;

    private LocalDateTime fechaRifa;

}
