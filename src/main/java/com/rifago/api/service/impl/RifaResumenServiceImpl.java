package com.rifago.api.service.impl;

import com.rifago.api.dto.resumen.*;
import com.rifago.api.modelos.*;
import com.rifago.api.repositorios.*;
import com.rifago.api.service.RifaResumenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RifaResumenServiceImpl implements RifaResumenService {

    private final RifaRepository rifaRepository;
    private final PremioRepository premioRepository;
    private final SorteoEventoRepository sorteoEventoRepository;

    // =====================
    // 🔓 PUBLIC
    // =====================
    @Override
    public RifaResumenPublicResponse obtenerResumenPublico(String codigoRifa) {

        Rifa rifa = rifaRepository.findByCodigo(codigoRifa)
                .orElseThrow(() -> new RuntimeException("Rifa no existe"));

        List<PremioResumenPublic> premios = premioRepository
                .findByRifaIdOrderByPrecioAsc(rifa.getId())
                .stream()
                .map(p -> PremioResumenPublic.builder()
                        .premioId(p.getId())
                        .nombre(p.getNombre())
                        .ganadorNombre(
                                p.getGanador() != null ? p.getGanador().getNombre() : null
                        )
                        .build())
                .toList();

        return RifaResumenPublicResponse.builder()
                .codigo(rifa.getCodigo())
                .titulo(rifa.getTitulo())
                .premios(premios)
                .build();
    }

    // =====================
    // 🔐 ADMIN
    // =====================
    @Override
    public RifaResumenAdminResponse obtenerResumenAdmin(Long adminId, Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada para este admin"));

        List<PremioResumenAdmin> premios = premioRepository
                .findByRifaIdOrderByPrecioAsc(rifaId)
                .stream()
                .map(p -> PremioResumenAdmin.builder()
                        .premioId(p.getId())
                        .nombre(p.getNombre())
                        .ganadorNombre(
                                p.getGanador() != null ? p.getGanador().getNombre() : null
                        )
                        .ganadorTelefono(
                                p.getGanador() != null ? p.getGanador().getTelefono() : null
                        )
                        .eventos(
                                sorteoEventoRepository
                                        .findByPremioIdOrderByIndiceSalidaAsc(p.getId())
                                        .stream()
                                        .map(e -> SorteoEventoResumen.builder()
                                                .indiceSalida(e.getIndiceSalida())
                                                .participanteNombre(e.getParticipante().getNombre())
                                                .participanteTelefono(e.getParticipante().getTelefono())
                                                .esGanador(e.getEsGanador())
                                                .build())
                                        .toList()
                        )
                        .build())
                .toList();

        return RifaResumenAdminResponse.builder()
                .rifaId(rifa.getId())
                .codigo(rifa.getCodigo())
                .titulo(rifa.getTitulo())
                .premios(premios)
                .build();
    }
}
