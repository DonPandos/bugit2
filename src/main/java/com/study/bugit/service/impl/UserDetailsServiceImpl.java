package com.study.bugit.service.impl;

import com.study.bugit.jwt.UserDetailsImpl;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userService.findUserByUsername(username);
        return UserDetailsImpl.fromUserModel(userModel);
    }
}
