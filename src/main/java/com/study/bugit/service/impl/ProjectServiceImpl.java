package com.study.bugit.service.impl;

import com.study.bugit.constants.Constants;
import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.project.ProjectResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.model.ProjectModel;
import com.study.bugit.model.users.RoleModel;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.ProjectRepository;
import com.study.bugit.repository.RoleRepository;
import com.study.bugit.service.ProjectService;
import com.study.bugit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, RoleRepository roleRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public List<ProjectResponse> getByUsername(String username) {
        List<ProjectModel> projectModels = projectRepository.findAllByOwnerOrderByCreatedAt(username);


        return null;
    }

    @Override
    public ProjectResponse create(CreateProjectRequest request) {
        if (Objects.nonNull(getProjectByName(request.getName()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_ALREADY_EXISTS_FORMAT, request.getName()));
        }

        ProjectModel projectModel = ProjectModel.createModel(
                request,
                List.of(userService.findUserByUsername(request.getSenderUsername()))
        );

        addRolesToOwnerAfterProjectCreating(request.getName(), request.getSenderUsername());

        projectRepository.save(projectModel);

        return ProjectResponse.fromProjectModel(projectModel);
    }

    @Override
    public void addMemberToProject(String projectName, String userName) {
        ProjectModel projectModel = getProjectByName(projectName);
        if  (Objects.isNull(projectModel)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, projectName));
        }

        RoleModel role = roleRepository.findByRole(
                defaultProjectRoles.get(Constants.READ_ROLE_KEY) + projectName.toUpperCase(Locale.ROOT)
        );

        UserModel userModel = userService.addRoleToUser(userName, role);

        projectModel.getMembers()
                .forEach((user) -> {
                    if (user.getUserName().equals(userName)) {
                        throw new CustomException(
                                HttpStatus.BAD_REQUEST,
                                String.format(ErrorConstants.USER_ALREADY_MEMBER_OF_PROJECT_FORMAT, userName, projectName)
                        );
                    }
                });

        projectModel.getMembers().add(userModel);
        projectRepository.save(projectModel);
    }

    @Override
    public void changeMemberRoles(ChangeMemberRolesRequest request) {
        ProjectModel projectModel = projectRepository.findByName(request.getProjectName());
        if (Objects.isNull(projectModel)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, request.getProjectName()));
        }

        if (!isMemberByUsername(request.getUserName(), projectModel)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, String.format(ErrorConstants.USERNAME_NOT_MEMBER_OF_PROJECT, request.getUserName(), request.getProjectName()));
        }

        List<RoleModel> roleModelList = request.getRoleKeyList().stream()
                .map(roleKey -> defaultProjectRoles.get(roleKey) + request.getProjectName().toUpperCase(Locale.ROOT))
                .map(roleRepository::findByRole)
                .collect(Collectors.toList());

        userService.changeUserRoles(request.getUserName(), roleModelList);
    }

    private ProjectModel getProjectByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    private void addRolesToOwnerAfterProjectCreating(String projectName, String userName) {
        defaultProjectRoles.values().stream()
                .map(defaultRoleTemplate -> defaultRoleTemplate + projectName.toUpperCase(Locale.ROOT))
                .forEach(projectRole -> {
                    RoleModel roleModel = new RoleModel(projectRole);
                    roleRepository.save(roleModel);

                    userService.addRoleToUser(userName, roleModel);
                });
    }

    private boolean isMemberByUsername(String userName, ProjectModel projectModel) {
        return projectModel.getMembers().stream()
                .anyMatch(userModel -> userModel.getUserName().equals(userName));
    }

    private final Map<String, String> defaultProjectRoles = new HashMap<>() {{
        put(Constants.OWNER_ROLE_KEY, Constants.OWNER_ROLE_TEMPLATE);
        put(Constants.READ_ROLE_KEY, Constants.READ_ROLE_TEMPLATE);
        put(Constants.WRITE_ROLE_KEY, Constants.WRITE_ROLE_TEMPLATE);
    }};

}
