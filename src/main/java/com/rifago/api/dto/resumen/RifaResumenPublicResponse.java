package com.rifago.api.dto.resumen;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RifaResumenPublicResponse {

    private String codigo;
    private String titulo;
    private List<PremioResumenPublic> premios;
}
