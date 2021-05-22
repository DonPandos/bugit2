package com.study.bugit.config;

import com.study.bugit.model.users.Role;
import com.study.bugit.model.users.RoleModel;
import com.study.bugit.repository.RoleRepository;
import com.study.bugit.repository.UserRepository;
import com.study.bugit.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class InitializingBean implements org.springframework.beans.factory.InitializingBean {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public InitializingBean(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(roleRepository.findByRole(Role.ROLE_USER.toString()))) {
            roleRepository.save(new RoleModel(Role.ROLE_USER.toString()));
        }
        if (Objects.isNull(roleRepository.findByRole(Role.ROLE_ADMIN.toString()))) {
            roleRepository.save(new RoleModel(Role.ROLE_ADMIN.toString()));
        }
    }
}
