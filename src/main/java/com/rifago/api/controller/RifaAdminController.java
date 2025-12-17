package com.rifago.api.controller;

import com.rifago.api.dto.rifa.*;
import com.rifago.api.security.SecurityUtils;
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

@Slf4j
@RestController
@RequestMapping("/api/admin/rifas")
@RequiredArgsConstructor
@Tag(name = "Rifas (Admin)", description = "Gestión de rifas por administrador")
public class RifaAdminController {

    private final RifaAdminService rifaAdminService;



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
}
