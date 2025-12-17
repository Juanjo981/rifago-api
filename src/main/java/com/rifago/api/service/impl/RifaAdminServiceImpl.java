package com.rifago.api.service.impl;

import com.rifago.api.dto.rifa.*;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.modelos.*;
import com.rifago.api.repositorios.RifaRepository;
import com.rifago.api.service.RifaAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RifaAdminServiceImpl implements RifaAdminService {

    private final RifaRepository rifaRepository;

    @Override
    public RifaResponse crearRifa(Long adminId, RifaCreateRequest request) {

        Rifa rifa = new Rifa();
        rifa.setCodigo(request.getCodigo());
        rifa.setTitulo(request.getTitulo());
        rifa.setDescripcion(request.getDescripcion());
        rifa.setFechaRifa(request.getFechaRifa());
        rifa.setEstado(RifaEstado.BORRADOR);

        AdminUser admin = new AdminUser();
        admin.setId(adminId);
        rifa.setAdmin(admin);

        return toResponse(rifaRepository.save(rifa));
    }

    @Override
    public RifaResponse editarRifa(Long adminId, Long rifaId, RifaUpdateRequest request) {

        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        if (rifa.getEstado() != RifaEstado.BORRADOR) {
            throw new RuntimeException("Solo se puede editar una rifa en estado BORRADOR");
        }

        if (request.getTitulo() != null) rifa.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) rifa.setDescripcion(request.getDescripcion());
        if (request.getFechaRifa() != null) rifa.setFechaRifa(request.getFechaRifa());

        return toResponse(rifaRepository.save(rifa));
    }

    @Override
    public List<RifaResponse> listarRifas(Long adminId) {

        return rifaRepository.findByAdminId(adminId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RifaResponse obtenerDetalle(Long adminId, Long rifaId) {
        return toResponse(obtenerRifaDelAdmin(adminId, rifaId));
    }

    @Override
    public void abrirRifa(Long adminId, Long rifaId) {

        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        if (rifa.getEstado() != RifaEstado.BORRADOR) {
            throw new RuntimeException("La rifa no puede abrirse desde el estado actual");
        }

        rifa.setEstado(RifaEstado.ABIERTA);
        rifaRepository.save(rifa);
    }

    @Override
    public void finalizarRifa(Long adminId, Long rifaId) {

        Rifa rifa = obtenerRifaDelAdmin(adminId, rifaId);

        if (rifa.getEstado() != RifaEstado.ABIERTA && rifa.getEstado() != RifaEstado.EN_SORTEO) {
            throw new RuntimeException("La rifa no puede finalizarse desde el estado actual");
        }

        rifa.setEstado(RifaEstado.FINALIZADA);
        rifaRepository.save(rifa);
    }

    // ======================
    // Helpers
    // ======================

    private Rifa obtenerRifaDelAdmin(Long adminId, Long rifaId) {
        return rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada para este admin"));
    }

    private RifaResponse toResponse(Rifa rifa) {
        return RifaResponse.builder()
                .id(rifa.getId())
                .codigo(rifa.getCodigo())
                .titulo(rifa.getTitulo())
                .descripcion(rifa.getDescripcion())
                .createdAt(rifa.getCreatedAt())
                .fechaRifa(rifa.getFechaRifa())
                .estado(rifa.getEstado())
                .build();
    }
}
