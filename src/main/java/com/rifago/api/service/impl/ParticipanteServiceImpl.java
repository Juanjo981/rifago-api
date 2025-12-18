package com.rifago.api.service.impl;

import com.rifago.api.dto.participante.ParticipanteAdminResponse;
import com.rifago.api.modelos.Rifa;
import com.rifago.api.repositorios.ParticipanteRepository;
import com.rifago.api.repositorios.RifaRepository;
import com.rifago.api.service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipanteServiceImpl implements ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final RifaRepository rifaRepository;

    @Override
    public List<ParticipanteAdminResponse> listarPorRifa(Long adminId, Long rifaId) {

        // 🔐 Validación centralizada
        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        return participanteRepository
                .findByRifaIdOrderByCreatedAtAsc(rifa.getId())
                .stream()
                .map(p -> new ParticipanteAdminResponse(
                        p.getId(),
                        p.getNombre(),
                        p.getTelefono(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public long contarPorRifa(Long adminId, Long rifaId) {

        // 🔐 Misma validación reutilizada
        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        return participanteRepository.countByRifaId(rifa.getId());
    }

    // =========================
    // MÉTODO REUTILIZABLE
    // =========================
    private Rifa obtenerRifaDelAdmin(Long adminId, Long rifaId) {
        return rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() ->
                        new RuntimeException("Rifa no encontrada para este admin")
                );
    }
}