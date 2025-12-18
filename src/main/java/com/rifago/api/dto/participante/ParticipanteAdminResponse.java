package com.rifago.api.dto.participante;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ParticipanteAdminResponse {

    private Long id;
    private String nombre;
    private String telefono;
    private OffsetDateTime createdAt;

}