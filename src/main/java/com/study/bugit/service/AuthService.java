package com.study.bugit.service;

import com.study.bugit.dto.request.auth.LoginRequest;
import com.study.bugit.dto.request.auth.RegisterRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.dto.response.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserResponse register(RegisterRequest request);
}
