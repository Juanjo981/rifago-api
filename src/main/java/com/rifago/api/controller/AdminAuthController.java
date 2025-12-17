package com.rifago.api.controller;

import com.rifago.api.dto.auth.*;
import com.rifago.api.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Auth", description = "Autenticación y registro de administradores")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar administrador",
            description = "Crea un nuevo usuario administrador para operar rifas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Void> register(
            @Valid @RequestBody AdminRegisterRequest request
    ) {
        adminAuthService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login administrador",
            description = "Autentica al admin y devuelve un JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<AdminAuthResponse> login(
            @Valid @RequestBody AdminLoginRequest request
    ) {
        return ResponseEntity.ok(adminAuthService.login(request));
    }
}
