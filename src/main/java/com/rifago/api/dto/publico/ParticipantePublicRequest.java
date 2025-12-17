package com.rifago.api.dto.publico;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantePublicRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String telefono;
}
