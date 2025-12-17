package com.rifago.api.controller;

import com.rifago.api.dto.resumen.RifaResumenPublicResponse;
import com.rifago.api.service.RifaResumenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/rifas")
@RequiredArgsConstructor
@Tag(name = "Rifas (Público)")
public class RifaResumenPublicController {

    private final RifaResumenService rifaResumenService;

    @GetMapping("/{codigo}/resumen")
    public ResponseEntity<RifaResumenPublicResponse> resumenPublico(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(
                rifaResumenService.obtenerResumenPublico(codigo)
        );
    }
}
