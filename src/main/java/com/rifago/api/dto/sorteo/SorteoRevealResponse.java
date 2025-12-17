package com.rifago.api.dto.sorteo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SorteoRevealResponse {

    private Long participanteId;
    private String nombre;
    private int indiceSalida;
    private boolean esGanador;
}
