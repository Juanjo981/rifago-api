package com.rifago.api.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthResponse {

    private String token;
    private String tokenType;
    private Long adminId;
    private String username;
}
