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

    public UserModel changeUserRoles(String userName, List<RoleModel> roles) {
        UserModel userModel = findUserByUsername(userName);

        if (userModel == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, userName));
        }

        roles.forEach(userModel.getRoles()::add);

        userRepository.save(userModel);
        return userModel;
    }
}
