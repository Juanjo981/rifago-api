package com.rifago.api.controller;

import com.rifago.api.dto.resumen.RifaResumenAdminResponse;
import com.rifago.api.security.SecurityUtils;
import com.rifago.api.service.RifaResumenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/rifas")
@RequiredArgsConstructor
@Tag(name = "Rifas (Admin)")
public class RifaResumenAdminController {

    private final RifaResumenService rifaResumenService;


    @GetMapping("/{rifaId}/resumen")
    public ResponseEntity<RifaResumenAdminResponse> resumenAdmin(
            @PathVariable Long rifaId
    ) {
        Long adminId = SecurityUtils.getAdminId();
        return ResponseEntity.ok(
                rifaResumenService.obtenerResumenAdmin(
                        adminId,
                        rifaId
                )
        );
    }
}
