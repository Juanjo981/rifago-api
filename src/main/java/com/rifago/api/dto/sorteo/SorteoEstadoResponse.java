package com.rifago.api.dto.sorteo;

import com.rifago.api.enums.RifaEstado;
import com.rifago.api.enums.SorteoEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SorteoEstadoResponse {

    private Long rifaId;
    private RifaEstado rifaEstado;

    private Long sesionId;
    private SorteoEstado sorteoEstado;

    // Premio actual
    private Long premioId;
    private String premioNombre;
    private Integer ordenPremio; // 1,2,3...
    private Boolean premioConfigurado;

    // Configuración
    private Integer totalSalidas;
    private Integer ganadorEn;

    // Progreso
    private Integer salidaActual; // ej. vamos en la 2
    private Boolean ganadorYaDefinido;

    // Flags útiles para UI
    private Boolean puedeConfigurarPremio;
    private Boolean puedeRevelarParticipante;
    private Boolean puedeFinalizarSorteo;
}

