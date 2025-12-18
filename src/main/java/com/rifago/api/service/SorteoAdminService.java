package com.rifago.api.service;

import com.rifago.api.dto.sorteo.*;

public interface SorteoAdminService {

    Long iniciarSorteo(Long adminId, Long rifaId);

    void configurarPremio(
            Long adminId,
            Long sesionId,
            Long premioId,
            PremioConfigRequest request
    );

    SorteoRevealResponse siguienteParticipante(
            Long adminId,
            Long sesionId,
            Long premioId
    );

    void finalizarSorteo(Long adminId, Long rifaId);

    SorteoEstadoResponse obtenerEstadoActual(Long rifaId);

    void configurarPremio(
            Long sesionId,
            Long premioId,
            ConfigurarPremioRequest request
    );
}
