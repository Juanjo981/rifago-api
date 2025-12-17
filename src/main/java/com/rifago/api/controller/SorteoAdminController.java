package com.rifago.api.controller;

import com.rifago.api.dto.sorteo.*;
import com.rifago.api.security.SecurityUtils;
import com.rifago.api.service.SorteoAdminService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Sorteo (Admin)", description = "Gestión del sorteo de rifas")
public class SorteoAdminController {

    private final SorteoAdminService sorteoAdminService;



    @PostMapping("/rifas/{rifaId}/sorteo/iniciar")
    @Operation(summary = "Iniciar sorteo")
    public ResponseEntity<Long> iniciarSorteo(@PathVariable Long rifaId) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                sorteoAdminService.iniciarSorteo(adminId, rifaId)
        );
    }

    @PostMapping("/sorteo/{sesionId}/premio/{premioId}/config")
    @Operation(summary = "Configurar premio del sorteo")
    public ResponseEntity<Void> configurarPremio(
            @PathVariable Long sesionId,
            @PathVariable Long premioId,
            @Valid @RequestBody PremioConfigRequest request
    ) {
        Long adminId = SecurityUtils.getAdminId();
        sorteoAdminService.configurarPremio(
                adminId, sesionId, premioId, request
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sorteo/{sesionId}/premio/{premioId}/siguiente")
    @Operation(summary = "Revelar siguiente participante")
    public ResponseEntity<SorteoRevealResponse> siguienteParticipante(
            @PathVariable Long sesionId,
            @PathVariable Long premioId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                sorteoAdminService.siguienteParticipante(
                        adminId, sesionId, premioId
                )
        );
    }

    @PostMapping("/rifas/{rifaId}/sorteo/finalizar")
    @Operation(summary = "Finalizar sorteo")
    public ResponseEntity<Void> finalizarSorteo(@PathVariable Long rifaId) {
        Long adminId = SecurityUtils.getAdminId();
        sorteoAdminService.finalizarSorteo(adminId, rifaId);
        return ResponseEntity.ok().build();
    }
}
