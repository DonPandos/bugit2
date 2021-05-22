package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.CreateUserRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.DEFAULT_API_URL + "/users")
@Slf4j
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/info")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        log.info("{} -> create new user");

        return null;
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority(#name)")
    public String getInfo(@PathVariable("name") String name) {
        log.info(name);
        return "HELLO";
    }
}