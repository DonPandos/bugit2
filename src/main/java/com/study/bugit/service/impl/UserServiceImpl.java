package com.study.bugit.service.impl;

import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.CreateUserRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.model.users.RoleModel;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.UserRepository;
import com.study.bugit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserModel findUserByUsername(String username) {
        return userRepository.findByUserName(username).orElse(null);
    }

    @Override
    public boolean checkPasswordByUsername(String userName, String password) {

        UserModel userModel = findUserByUsername(userName);

        if (userModel == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, userName));
        }

        return passwordEncoder.matches(password, userModel.getPassword());
    }

    @Override
    public UserModel addRoleToUser(String userName, RoleModel role) {
        UserModel userModel = findUserByUsername(userName);

        if (userModel == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, userName));
        }

        userModel.getRoles().add(role);

        userRepository.save(userModel);

        return userModel;
    }

    public UserModel changeUserRoles(String userName, List<RoleModel> roles, String projectName) {
        UserModel userModel = findUserByUsername(userName);

        if (userModel == null) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, userName)
            );
        }

        userModel.setRoles(userModel.getRoles().stream()
                .filter(roleModel -> {
                    String role = roleModel.getRole();
                    return !role.substring(role.lastIndexOf("_") + 1)
                            .equals(projectName.toUpperCase(Locale.ROOT));
                })
                .collect(Collectors.toSet()));

        roles.forEach(userModel.getRoles()::add);

        userRepository.save(userModel);
        return userModel;
    }

    // returns UserModel if true; else return null
    @Override
    public UserModel checkUserRole(String username, String role) {
        UserModel userModel = findUserByUsername(username);

        if (userModel == null) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, username)
            );
        }

        for (RoleModel roleModel : userModel.getRoles()) {
            if (roleModel.getRole().equals(role)) return userModel;
        }

        return null;
    }
}
