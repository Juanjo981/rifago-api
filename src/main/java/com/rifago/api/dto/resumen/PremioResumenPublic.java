package com.rifago.api.dto.resumen;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremioResumenPublic {

    private Long premioId;
    private String nombre;
    private String ganadorNombre;
}
