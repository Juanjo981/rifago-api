package com.rifago.api.service.impl;

import com.rifago.api.dto.sorteo.*;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.enums.SorteoEstado;
import com.rifago.api.modelos.*;
import com.rifago.api.repositorios.*;
import com.rifago.api.service.SorteoAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SorteoAdminServiceImpl implements SorteoAdminService {

    private final RifaRepository rifaRepository;
    private final PremioRepository premioRepository;
    private final SorteoSesionRepository sorteoSesionRepository;
    private final SorteoEventoRepository sorteoEventoRepository;
    private final ParticipanteRepository participanteRepository;
    private final SorteoPremioConfigRepository sorteoPremioConfigRepository;

    // Config temporal por premio (sesion-premio)
    private final Map<String, PremioConfigRequest> premioConfigMap = new HashMap<>();

    @Override
    @Transactional
    public Long iniciarSorteo(Long adminId, Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada"));

        if (rifa.getEstado() != RifaEstado.ABIERTA) {
            throw new RuntimeException("La rifa no está ABIERTA");
        }

        if (sorteoSesionRepository.existsByRifaIdAndEstado(rifaId, SorteoEstado.ACTIVA)) {
            throw new RuntimeException("Ya existe un sorteo activo para esta rifa");
        }

        AdminUser admin = new AdminUser();
        admin.setId(adminId);

        SorteoSesion sesion = SorteoSesion.builder()
                .rifa(rifa)
                .estado(SorteoEstado.ACTIVA)
                .createdBy(admin)
                .build();


        sorteoSesionRepository.save(sesion);

        rifa.setEstado(RifaEstado.EN_SORTEO);
        rifaRepository.save(rifa);

        // Fuerza orden por precio ASC (visual / lógico)
        premioRepository.findByRifaIdOrderByPrecioAsc(rifaId);

        return sesion.getId();
    }

    @Override
    public void configurarPremio(
            Long adminId,
            Long sesionId,
            Long premioId,
            PremioConfigRequest request
    ) {

        if (request.getGanadorEn() > request.getSalidas()) {
            throw new RuntimeException("ganadorEn no puede ser mayor a salidas");
        }

        SorteoSesion sesion = validarSesion(adminId, sesionId);
        Premio premio = validarPremioDeSesion(sesion, premioId);

        String key = key(sesionId, premioId);
        premioConfigMap.put(key, request);
    }

    @Override
    @Transactional
    public SorteoRevealResponse siguienteParticipante(
            Long adminId,
            Long sesionId,
            Long premioId
    ) {

        SorteoSesion sesion = validarSesion(adminId, sesionId);
        Premio premio = validarPremioDeSesion(sesion, premioId);

        PremioConfigRequest config = premioConfigMap.get(key(sesionId, premioId));
        if (config == null) {
            throw new RuntimeException("Premio no configurado");
        }

        int indiceActual = (int) sorteoEventoRepository
                .countBySorteoSesionIdAndPremioId(sesionId, premioId) + 1;

        if (indiceActual > config.getSalidas()) {
            throw new RuntimeException("Ya no hay más salidas para este premio");
        }

        Participante participante = participanteRepository
                .findRandomElegible(
                        premio.getRifa().getId(),
                        sesionId,
                        premioId
                )
                .orElseThrow(() -> new RuntimeException("No hay participantes elegibles"));

        boolean esGanador = indiceActual == config.getGanadorEn();

        SorteoEvento evento = SorteoEvento.builder()
                .sorteoSesion(sesion)
                .premio(premio)
                .participante(participante)
                .indiceSalida(indiceActual)
                .esGanador(esGanador)
                .build();

        sorteoEventoRepository.save(evento);

        if (esGanador) {
            premio.setGanador(participante);
            premioRepository.save(premio);
        }

        return SorteoRevealResponse.builder()
                .participanteId(participante.getId())
                .nombre(participante.getNombre())
                .indiceSalida(indiceActual)
                .esGanador(esGanador)
                .build();
    }

    @Override
    @Transactional
    public void finalizarSorteo(Long adminId, Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .filter(r -> r.getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada"));

        SorteoSesion sesion = sorteoSesionRepository
                .findByRifaIdAndEstado(rifaId, SorteoEstado.ACTIVA)
                .orElseThrow(() -> new RuntimeException("No hay sorteo activo"));

        boolean faltanGanadores = premioRepository.findByRifaId(rifaId)
                .stream()
                .anyMatch(p -> p.getGanador() == null);

        if (faltanGanadores) {
            throw new RuntimeException("No todos los premios tienen ganador");
        }

        sesion.setEstado(SorteoEstado.FINALIZADA);
        sesion.setEndedAt(OffsetDateTime.now());
        sorteoSesionRepository.save(sesion);

        rifa.setEstado(RifaEstado.FINALIZADA);
        rifaRepository.save(rifa);
    }

    @Override
    public SorteoEstadoResponse obtenerEstadoActual(Long rifaId) {

        Rifa rifa = rifaRepository.findById(rifaId)
                .orElseThrow(() -> new RuntimeException("Rifa no encontrada"));

        if (rifa.getEstado() != RifaEstado.EN_SORTEO) {
            return SorteoEstadoResponse.builder()
                    .rifaId(rifa.getId())
                    .rifaEstado(rifa.getEstado())
                    .build();
        }

        SorteoSesion sesion = sorteoSesionRepository
                .findByRifaIdAndEstado(rifaId, SorteoEstado.ACTIVA)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa"));

        List<Premio> premios =
                premioRepository.findByRifaIdOrderByPrecioAsc(rifaId);

        for (int i = 0; i < premios.size(); i++) {
            Premio premio = premios.get(i);

            // CONFIG DEL PREMIO
            Optional<SorteoPremioConfig> configOpt =
                    sorteoPremioConfigRepository
                            .findBySorteoSesionIdAndPremioId(
                                    sesion.getId(),
                                    premio.getId()
                            );

            boolean premioConfigurado = configOpt.isPresent();
            SorteoPremioConfig config = configOpt.orElse(null);

            // EVENTOS
            boolean ganadorDefinido =
                    sorteoEventoRepository
                            .existsBySorteoSesionIdAndPremioIdAndEsGanadorTrue(
                                    sesion.getId(), premio.getId()
                            );

            int salidasActuales =
                    (int) sorteoEventoRepository
                            .countBySorteoSesionIdAndPremioId(
                                    sesion.getId(), premio.getId()
                            );

            if (!ganadorDefinido) {
                return SorteoEstadoResponse.builder()
                        .rifaId(rifa.getId())
                        .rifaEstado(rifa.getEstado())
                        .sesionId(sesion.getId())
                        .sorteoEstado(sesion.getEstado())

                        .premioId(premio.getId())
                        .premioNombre(premio.getNombre())
                        .ordenPremio(i + 1)

                        .premioConfigurado(premioConfigurado)
                        .totalSalidas(config != null ? config.getTotalSalidas() : null)
                        .ganadorEn(config != null ? config.getGanadorEn() : null)

                        .salidaActual(salidasActuales)
                        .ganadorYaDefinido(false)

                        .puedeConfigurarPremio(!premioConfigurado)
                        .puedeRevelarParticipante(premioConfigurado && !ganadorDefinido)
                        .puedeFinalizarSorteo(false)
                        .build();
            }
        }

        // TODOS LOS PREMIOS TERMINADOS
        return SorteoEstadoResponse.builder()
                .rifaId(rifaId)
                .rifaEstado(RifaEstado.EN_SORTEO)
                .sesionId(sesion.getId())
                .sorteoEstado(sesion.getEstado())
                .puedeFinalizarSorteo(true)
                .build();
    }


    @Override
    public void configurarPremio(
            Long sesionId,
            Long premioId,
            ConfigurarPremioRequest request
    ) {
        SorteoSesion sesion = sorteoSesionRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Premio premio = premioRepository.findById(premioId)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));

        if (sorteoPremioConfigRepository
                .existsBySorteoSesionIdAndPremioId(sesionId, premioId)) {
            throw new RuntimeException("Este premio ya fue configurado");
        }

        if (request.getGanadorEn() > request.getTotalSalidas()) {
            throw new RuntimeException("GanadorEn no puede ser mayor que totalSalidas");
        }

        SorteoPremioConfig config = SorteoPremioConfig.builder()
                .sorteoSesion(sesion)
                .premio(premio)
                .totalSalidas(request.getTotalSalidas())
                .ganadorEn(request.getGanadorEn())
                .build();

        sorteoPremioConfigRepository.save(config);
    }

    // ======================
    // Helpers
    // ======================

    private SorteoSesion validarSesion(Long adminId, Long sesionId) {
        return sorteoSesionRepository.findById(sesionId)
                .filter(s -> s.getRifa().getAdmin().getId().equals(adminId))
                .orElseThrow(() -> new RuntimeException("Sesión no válida"));
    }

    private Premio validarPremioDeSesion(SorteoSesion sesion, Long premioId) {
        Premio premio = premioRepository.findById(premioId)
                .orElseThrow(() -> new RuntimeException("Premio no existe"));

        if (!premio.getRifa().getId().equals(sesion.getRifa().getId())) {
            throw new RuntimeException("El premio no pertenece a esta rifa");
        }

        if (premio.getGanador() != null) {
            throw new RuntimeException("Este premio ya tiene ganador");
        }

        return premio;
    }

    private String key(Long sesionId, Long premioId) {
        return sesionId + "-" + premioId;
    }
}
