package com.study.bugit.service.impl;

import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.auth.LoginRequest;
import com.study.bugit.dto.request.auth.RegisterRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.dto.response.auth.LoginResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.jwt.JwtTokenProvider;
import com.study.bugit.model.users.Role;
import com.study.bugit.model.users.Status;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.RoleRepository;
import com.study.bugit.repository.UserRepository;
import com.study.bugit.service.AuthService;
import com.study.bugit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, UserService userService, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public LoginResponse login(@NotNull LoginRequest request) {
        LoginResponse response = new LoginResponse();

        if (Objects.isNull(userService.findUserByUsername(request.getUsername()))) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, request.getUsername())
            );
        }

        if (userService.checkPasswordByUsername(request.getUsername(), request.getPassword())) {
            response.setToken(jwtTokenProvider.generateToken(request.getUsername(),
                    userDetailsService.loadUserByUsername(request.getUsername()).getAuthorities()));
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorConstants.INCORRECT_PASSWORD);
        }

        return response;
    }

    @Override
    public UserResponse register(@NotNull RegisterRequest request) {

        if (Objects.nonNull(userService.findUserByUsername(request.getUsername()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorConstants.USER_WITH_THIS_USERNAME_ALREADY_EXISTS);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        UserModel user = UserModel.fromRegisterRequest(request);

        user.setRoles(Set.of(roleRepository.findByRole(Role.ROLE_USER.toString())));
        user.setStatus(Status.ACTIVE);

        return userRepository.save(user).toUserResponse();
    }
}
