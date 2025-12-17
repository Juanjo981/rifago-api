package com.rifago.api.dto.resumen;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SorteoEventoResumen {

    private int indiceSalida;
    private String participanteNombre;
    private String participanteTelefono;
    private boolean esGanador;
}
