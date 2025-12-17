package com.rifago.api.dto;

import com.rifago.api.modelos.AdminUser;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String username;
    private Boolean activo;
    private OffsetDateTime createdAt;

    public static AdminUserDto fromEntity(AdminUser entity) {
        return AdminUserDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AdminUser toEntity() {
        return AdminUser.builder()
                .id(this.id)
                .nombre(this.nombre)
                .telefono(this.telefono)
                .email(this.email)
                .username(this.username)
                .activo(this.activo)
                .build();
    }
}
