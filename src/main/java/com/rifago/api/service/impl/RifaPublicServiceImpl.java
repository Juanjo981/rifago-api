package com.rifago.api.service.impl;

import com.rifago.api.dto.publico.*;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.exception.BadRequestException;
import com.rifago.api.exception.NotFoundException;
import com.rifago.api.modelos.*;
import com.rifago.api.repositorios.ParticipanteRepository;
import com.rifago.api.repositorios.RifaRepository;
import com.rifago.api.service.RifaPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RifaPublicServiceImpl implements RifaPublicService {

    private final RifaRepository rifaRepository;
    private final ParticipanteRepository participanteRepository;

    @Override
    public ParticipantePublicResponse registrarParticipante(
            String codigoRifa,
            ParticipantePublicRequest request
    ) {

        Rifa rifa = rifaRepository.findByCodigo(codigoRifa)
                .orElseThrow(() -> new NotFoundException("La rifa no existe"));

        if (rifa.getEstado() != RifaEstado.ABIERTA) {
            throw new BadRequestException("La rifa no está abierta para registros");
        }

        boolean existeTelefono = participanteRepository
                .existsByRifaIdAndTelefono(rifa.getId(), request.getTelefono());

        if (existeTelefono) {
            throw new RuntimeException("Este teléfono ya está registrado en la rifa");
        }

        Participante participante = new Participante();
        participante.setNombre(request.getNombre());
        participante.setTelefono(request.getTelefono());
        participante.setRifa(rifa);

        Participante guardado = participanteRepository.save(participante);

        return ParticipantePublicResponse.builder()
                .id(guardado.getId())
                .nombre(guardado.getNombre())
                .telefono(guardado.getTelefono())
                .createdAt(guardado.getCreatedAt())
                .build();
    }

    @Override
    public RifaPublicInfoResponse obtenerInfoPublica(String codigoRifa) {

        Rifa rifa = rifaRepository.findByCodigo(codigoRifa)
                .orElseThrow(() -> new NotFoundException("La rifa no existe"));

        return RifaPublicInfoResponse.builder()
                .codigo(rifa.getCodigo())
                .titulo(rifa.getTitulo())
                .descripcion(rifa.getDescripcion())
                .fechaRifa(rifa.getFechaRifa())
                .estado(rifa.getEstado())
                .build();
    }
}
