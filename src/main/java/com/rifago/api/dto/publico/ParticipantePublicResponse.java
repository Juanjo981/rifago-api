package com.rifago.api.dto.publico;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantePublicResponse {

    private Long id;
    private String nombre;
    private String telefono;
    private OffsetDateTime createdAt;
}
