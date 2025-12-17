package com.rifago.api.controller;

import com.rifago.api.dto.premio.*;
import com.rifago.api.security.SecurityUtils;
import com.rifago.api.service.PremioAdminService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Premios (Admin)", description = "Gestión de premios por rifa")
public class PremioAdminController {

    private final PremioAdminService premioAdminService;




    @PostMapping("/rifas/{rifaId}/premios")
    @Operation(summary = "Crear premio para una rifa")
    @ApiResponse(responseCode = "201", description = "Premio creado")
    public ResponseEntity<PremioResponse> crearPremio(
            @PathVariable Long rifaId,
            @Valid @RequestBody PremioCreateRequest request
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(premioAdminService.crearPremio(adminId, rifaId, request));
    }

    @PutMapping("/premios/{premioId}")
    @Operation(summary = "Editar premio")
    public ResponseEntity<PremioResponse> editarPremio(
            @PathVariable Long premioId,
            @RequestBody PremioUpdateRequest request
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                premioAdminService.editarPremio(adminId, premioId, request)
        );
    }

    @DeleteMapping("/premios/{premioId}")
    @Operation(summary = "Eliminar premio")
    @ApiResponse(responseCode = "204", description = "Premio eliminado")
    public ResponseEntity<Void> eliminarPremio(@PathVariable Long premioId) {
        Long adminId = SecurityUtils.getAdminId();
        premioAdminService.eliminarPremio(adminId, premioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rifas/{rifaId}/premios")
    @Operation(summary = "Listar premios por rifa (ordenados por precio asc)")
    public ResponseEntity<List<PremioResponse>> listarPremios(
            @PathVariable Long rifaId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                premioAdminService.listarPremiosPorRifa(adminId, rifaId)
        );
    }
}
