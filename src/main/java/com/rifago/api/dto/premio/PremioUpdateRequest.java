package com.rifago.api.dto.premio;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremioUpdateRequest {

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String fotoUrl;
    private Integer orden;
}
