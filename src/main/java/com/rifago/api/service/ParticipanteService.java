package com.rifago.api.service;

import com.rifago.api.dto.participante.ParticipanteAdminResponse;

import java.util.List;

public interface ParticipanteService {

    List<ParticipanteAdminResponse> listarPorRifa(Long adminId, Long rifaId);

    long contarPorRifa(Long adminId, Long rifaId);

    }
