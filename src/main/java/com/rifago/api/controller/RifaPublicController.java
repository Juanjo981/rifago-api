package com.rifago.api.controller;

import com.rifago.api.dto.publico.*;
import com.rifago.api.service.RifaPublicService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/rifas")
@RequiredArgsConstructor
@Tag(name = "Rifas (Público)", description = "Endpoints públicos para participantes")
public class RifaPublicController {

    private final RifaPublicService rifaPublicService;

    @PostMapping("/{codigo}/participantes")
    @Operation(
            summary = "Registrar participante en rifa",
            description = "Registra un participante usando el código de la rifa"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participante registrado"),
            @ApiResponse(responseCode = "400", description = "Rifa no válida o teléfono duplicado")
    })
    public ResponseEntity<ParticipantePublicResponse> registrarParticipante(
            @PathVariable String codigo,
            @Valid @RequestBody ParticipantePublicRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rifaPublicService.registrarParticipante(codigo, request));
    }

    @GetMapping("/{codigo}")
    @Operation(
            summary = "Obtener información pública de la rifa",
            description = "Devuelve información básica de la rifa para mostrar al participante"
    )
    public ResponseEntity<RifaPublicInfoResponse> obtenerInfoRifa(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(
                rifaPublicService.obtenerInfoPublica(codigo)
        );
    }
}
