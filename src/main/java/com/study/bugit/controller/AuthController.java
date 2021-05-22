package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.auth.LoginRequest;
import com.study.bugit.dto.request.auth.RegisterRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.dto.response.auth.LoginResponse;
import com.study.bugit.service.AuthService;
import com.study.bugit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( Constants.DEFAULT_API_URL + "/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("{} -> login : " + request.toString());

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        log.info("{} -> register : " + request.toString());

        UserResponse response = authService.register(request);

        log.info("{} -> register result : " + response.toString());

        return ResponseEntity.ok(response);
    }
}
