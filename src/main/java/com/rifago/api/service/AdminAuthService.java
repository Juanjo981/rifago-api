package com.rifago.api.service;

import com.rifago.api.dto.auth.*;

public interface AdminAuthService {

    void register(AdminRegisterRequest request);

    AdminAuthResponse login(AdminLoginRequest request);
}
