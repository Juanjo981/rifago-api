package com.rifago.api.dto.resumen;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremioResumenAdmin {

    private Long premioId;
    private String nombre;
    private String ganadorNombre;
    private String ganadorTelefono;
    private List<SorteoEventoResumen> eventos;
}
