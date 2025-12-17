package com.rifago.api.service;

import com.rifago.api.dto.rifa.*;

import java.util.List;

public interface RifaAdminService {

    RifaResponse crearRifa(Long adminId, RifaCreateRequest request);

    RifaResponse editarRifa(Long adminId, Long rifaId, RifaUpdateRequest request);

    List<RifaResponse> listarRifas(Long adminId);

    RifaResponse obtenerDetalle(Long adminId, Long rifaId);

    void abrirRifa(Long adminId, Long rifaId);

    void finalizarRifa(Long adminId, Long rifaId);
}
