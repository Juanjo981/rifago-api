package com.rifago.api.dto.resumen;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RifaResumenAdminResponse {

    private Long rifaId;
    private String codigo;
    private String titulo;
    private List<PremioResumenAdmin> premios;
}
