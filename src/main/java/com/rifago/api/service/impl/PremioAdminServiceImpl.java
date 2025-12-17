package com.rifago.api.service.impl;

import com.rifago.api.dto.premio.*;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.modelos.*;
import com.rifago.api.repositorios.PremioRepository;
import com.rifago.api.repositorios.RifaRepository;
import com.rifago.api.service.PremioAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PremioAdminServiceImpl implements PremioAdminService {

    private final PremioRepository premioRepository;
    private final RifaRepository rifaRepository;

    @Override
    public PremioResponse crearPremio(Long adminId, Long rifaId, PremioCreateRequest request) {

        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        if (rifa.getEstado() != RifaEstado.BORRADOR) {
            throw new RuntimeException("Solo se pueden agregar premios en estado BORRADOR");
        }

        Premio premio = new Premio();
        premio.setNombre(request.getNombre());
        premio.setDescripcion(request.getDescripcion());
        premio.setPrecio(request.getPrecio());
        premio.setFotoUrl(request.getFotoUrl());
        premio.setOrden(request.getOrden());
        premio.setRifa(rifa);

        return toResponse(premioRepository.save(premio));
    }

    @Override
    public PremioResponse editarPremio(Long adminId, Long premioId, PremioUpdateRequest request) {

        Premio premio = obtenerPremioDelAdmin(adminId, premioId);

        if (premio.getRifa().getEstado() != RifaEstado.BORRADOR) {
            throw new RuntimeException("No se puede editar un premio cuando la rifa no está en BORRADOR");
        }

        if (request.getNombre() != null) premio.setNombre(request.getNombre());
        if (request.getDescripcion() != null) premio.setDescripcion(request.getDescripcion());
        if (request.getPrecio() != null) premio.setPrecio(request.getPrecio());
        if (request.getFotoUrl() != null) premio.setFotoUrl(request.getFotoUrl());
        if (request.getOrden() != null) premio.setOrden(request.getOrden());

        return toResponse(premioRepository.save(premio));
    }

    @Override
    public void eliminarPremio(Long adminId, Long premioId) {

        Premio premio = obtenerPremioDelAdmin(adminId, premioId);

        if (premio.getRifa().getEstado() != RifaEstado.BORRADOR) {
            throw new RuntimeException("No se puede eliminar un premio cuando la rifa no está en BORRADOR");
        }

        premioRepository.delete(premio);
    }

    @Override
    public List<PremioResponse> listarPremiosPorRifa(Long adminId, Long rifaId) {

        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        return premioRepository.findByRifaIdOrderByPrecioAsc(rifa.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ======================
    // Helpers
    // ======================

    private Rifa obtenerRifaDelAdmin(Long adminId, Long rifaId) {
        return rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada para este admin"));
    }

    private Premio obtenerPremioDelAdmin(Long adminId, Long premioId) {
        return premioRepository.findById(premioId)
                .filter(p -> p.getRifa().getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Premio no encontrado para este admin"));
    }

    private PremioResponse toResponse(Premio premio) {
        return PremioResponse.builder()
                .id(premio.getId())
                .rifaId(premio.getRifa().getId())
                .nombre(premio.getNombre())
                .descripcion(premio.getDescripcion())
                .precio(premio.getPrecio())
                .fotoUrl(premio.getFotoUrl())
                .orden(premio.getOrden())
                .ganadorParticipanteId(
                        premio.getGanador() != null ? premio.getGanador().getId() : null
                )
                .build();
    }
}
