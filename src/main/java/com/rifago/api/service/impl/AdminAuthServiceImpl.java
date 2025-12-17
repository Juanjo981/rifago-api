package com.rifago.api.service.impl;

import com.rifago.api.dto.auth.*;
import com.rifago.api.modelos.AdminUser;
import com.rifago.api.repositorios.AdminUserRepository;
import com.rifago.api.service.AdminAuthService;
import com.rifago.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${security.admin.invite-code}")
    private String adminInviteCode;

    @Override
    public void register(AdminRegisterRequest request) {

        if (!adminInviteCode.equals(request.getInviteCode())) {
            throw new RuntimeException("Código de invitación inválido");
        }

        if (adminUserRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        AdminUser admin = AdminUser.builder()
                .nombre(request.getNombre())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .activo(true)
                .build();

        adminUserRepository.save(admin);
    }

    @Override
    public AdminAuthResponse login(AdminLoginRequest request) {

        AdminUser admin = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(
                admin.getId(),
                admin.getUsername()
        );

        return AdminAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .adminId(admin.getId())
                .username(admin.getUsername())
                .build();
    }
}
