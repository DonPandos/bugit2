package com.study.bugit.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String expired = (String) request.getAttribute("jwt-expired");

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");

        if (Objects.nonNull(expired)) {
            response.getOutputStream().print("{\"errorMessage\":\"Jwt expired\", \"errorCode\":401}");
        } else {
            response.getOutputStream().print("{\"errorMessage\":\"Jwt invalid\", \"errorCode\":401}");
        }
        response.flushBuffer();
    }
}
