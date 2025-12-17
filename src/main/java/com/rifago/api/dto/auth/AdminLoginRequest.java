package com.rifago.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
