package com.rifago.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {

    @NotBlank
    private String nombre;

    private String telefono;

    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String inviteCode;
}
