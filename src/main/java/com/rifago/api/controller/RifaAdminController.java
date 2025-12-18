package com.rifago.api.controller;

import com.rifago.api.dto.participante.ParticipanteAdminResponse;
import com.rifago.api.dto.rifa.*;
import com.rifago.api.security.SecurityUtils;
import com.rifago.api.service.ParticipanteService;
import com.rifago.api.service.RifaAdminService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/rifas")
@RequiredArgsConstructor
@Tag(name = "Rifas (Admin)", description = "Gestión de rifas por administrador")
public class RifaAdminController {

    private final RifaAdminService rifaAdminService;

    private final ParticipanteService participanteService;

    @PostMapping
    @Operation(summary = "Crear rifa")
    public ResponseEntity<RifaResponse> crearRifa(
            @Valid @RequestBody RifaCreateRequest request
    ) {
        Long adminId = SecurityUtils.getAdminId();
        log.info("ADMIN ID = " + adminId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rifaAdminService.crearRifa(adminId, request));
    }

    @PutMapping("/{rifaId}")
    @Operation(summary = "Editar rifa")
    public ResponseEntity<RifaResponse> editarRifa(
            @PathVariable Long rifaId,
            @RequestBody RifaUpdateRequest request
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                rifaAdminService.editarRifa(adminId, rifaId, request)
        );
    }

    @GetMapping
    @Operation(summary = "Listar rifas del admin")
    public ResponseEntity<List<RifaResponse>> listarRifas() {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                rifaAdminService.listarRifas(adminId)
        );
    }

    @GetMapping("/{rifaId}")
    @Operation(summary = "Detalle de rifa")
    public ResponseEntity<RifaResponse> detalleRifa(
            @PathVariable Long rifaId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                rifaAdminService.obtenerDetalle(adminId, rifaId)
        );
    }

    @PostMapping("/{rifaId}/abrir")
    @Operation(summary = "Abrir rifa (estado ABIERTA)")
    public ResponseEntity<Void> abrirRifa(@PathVariable Long rifaId) {
        Long adminId = SecurityUtils.getAdminId();
        rifaAdminService.abrirRifa(adminId, rifaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{rifaId}/finalizar")
    @Operation(summary = "Finalizar rifa (estado FINALIZADA)")
    public ResponseEntity<Void> finalizarRifa(@PathVariable Long rifaId) {
        Long adminId = SecurityUtils.getAdminId();
        rifaAdminService.finalizarRifa(adminId, rifaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{rifaId}/participantes")
    @Operation(
            summary = "Listar participantes de una rifa",
            description = "Obtiene todos los participantes registrados en una rifa del administrador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participantes obtenidos correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Rifa no encontrada")
    })
    public ResponseEntity<List<ParticipanteAdminResponse>> listarParticipantes(
            @PathVariable Long rifaId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                participanteService.listarPorRifa(adminId, rifaId)
        );
    }

    @GetMapping("/{rifaId}/participantes/count")
    @Operation(
            summary = "Contar participantes de una rifa",
            description = "Obtiene el total de participantes registrados en una rifa"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Rifa no encontrada")
    })
    public ResponseEntity<Map<String, Long>> contarParticipantes(
            @PathVariable Long rifaId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        long total = participanteService.contarPorRifa(adminId, rifaId);

        return ResponseEntity.ok(
                Map.of("total", total)
        );
    }

}
