package com.rifago.api.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getAdminId() {
        Object principal =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (principal instanceof JwtUserPrincipal jwt) {
            return jwt.getAdminId();
        }

        throw new RuntimeException("Admin no autenticado");
    }
}
