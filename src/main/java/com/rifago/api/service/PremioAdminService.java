package com.rifago.api.service;

import com.rifago.api.dto.premio.*;

import java.util.List;

public interface PremioAdminService {

    PremioResponse crearPremio(Long adminId, Long rifaId, PremioCreateRequest request);

    PremioResponse editarPremio(Long adminId, Long premioId, PremioUpdateRequest request);

    void eliminarPremio(Long adminId, Long premioId);

    List<PremioResponse> listarPremiosPorRifa(Long adminId, Long rifaId);
}
