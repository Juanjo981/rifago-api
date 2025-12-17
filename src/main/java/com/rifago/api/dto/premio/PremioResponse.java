package com.rifago.api.dto.premio;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremioResponse {

    private Long id;
    private Long rifaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String fotoUrl;
    private Integer orden;
    private Long ganadorParticipanteId;
}
