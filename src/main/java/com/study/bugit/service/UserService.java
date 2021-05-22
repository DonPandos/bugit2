package com.study.bugit.service;

import com.study.bugit.model.users.RoleModel;
import com.study.bugit.model.users.UserModel;

import java.util.List;

public interface UserService {
    UserModel findUserByUsername(String username);
    boolean checkPasswordByUsername(String username, String password);
    UserModel addRoleToUser(String username, RoleModel role);
    UserModel changeUserRoles(String userName, List<RoleModel> roles);
}
