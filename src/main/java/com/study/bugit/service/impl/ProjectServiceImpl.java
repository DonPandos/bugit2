package com.study.bugit.service.impl;

import com.study.bugit.constants.Constants;
import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.dto.response.project.MemberWithRolesResponse;
import com.study.bugit.dto.response.project.ProjectMembersResponse;
import com.study.bugit.dto.response.project.ProjectResponse;
import com.study.bugit.dto.response.project.UserProjectRolesResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.model.IssueModel;
import com.study.bugit.model.ProjectModel;
import com.study.bugit.model.users.RoleModel;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.IssueRepository;
import com.study.bugit.repository.ProjectRepository;
import com.study.bugit.repository.RoleRepository;
import com.study.bugit.service.ProjectService;
import com.study.bugit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final IssueRepository issueRepository;
    private final UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, RoleRepository roleRepository, IssueRepository issueRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
        this.issueRepository = issueRepository;
        this.userService = userService;
    }

    @Override
    public List<ProjectResponse> getAllByUsernameWhereMember(String username) {
        List<ProjectModel> projectModels = projectRepository.findAllByMembersOrderByCreatedAt(userService.findUserByUsername(username));
        List<ProjectResponse> projectResponses = projectModels.stream()
                .map(ProjectResponse::fromProjectModel)
                .collect(Collectors.toList());
        return projectResponses;
    }

    @Override
    public ProjectResponse create(CreateProjectRequest request) {
        if (Objects.nonNull(getProjectByName(request.getName()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_ALREADY_EXISTS_FORMAT, request.getName()));
        }

        ProjectModel projectModel = ProjectModel.createModel(
                request,
                Set.of(userService.findUserByUsername(request.getSenderUsername()))
        );

        addRolesToOwnerAfterProjectCreating(request.getName(), request.getSenderUsername());

        projectRepository.save(projectModel);

        return ProjectResponse.fromProjectModel(projectModel);
    }

    @Override
    public MemberWithRolesResponse addMemberToProject(String projectName, String userName) {
        ProjectModel projectModel = getProjectByName(projectName);
        if (Objects.isNull(projectModel)) {
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

        return userModel.toMemberWithRolesResponse(projectName);
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

        userService.changeUserRoles(request.getUserName(), roleModelList, request.getProjectName());
    }

    public ProjectModel getProjectByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    @Override
    public ProjectModel getProjectByIssueNumber(String issueNumber) {
        IssueModel issueModel = issueRepository.findByIssueNumber(issueNumber);

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, issueNumber)
            );
        }

        return issueModel.getProject();
    }

    @Override
    public UserProjectRolesResponse getUsernameRoles(String projectName, String username) {

        ProjectModel projectModel = getProjectByName(projectName);

        if (Objects.isNull(projectModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, projectName)
            );
        }

        if (!isMemberByUsername(username, projectModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USERNAME_NOT_MEMBER_OF_PROJECT, username, projectName)
            );
        }

        UserModel userModel = userService.findUserByUsername(username);

        List<String> userRoles = userModel.getRoles().stream()
                .filter(roleModel -> {
                    String role = roleModel.getRole();
                    return role.substring(role.lastIndexOf("_") + 1, role.length()).equals(projectName.toUpperCase(Locale.ROOT));
                })
                .map(RoleModel::getRole)
                .collect(Collectors.toList());

        return new UserProjectRolesResponse(userRoles);
    }

    @Override
    public ProjectMembersResponse getProjectMembers(String projectName) {
        ProjectModel projectModel = getProjectByName(projectName);

        if (Objects.isNull(projectModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, projectName)
            );
        }

        List<UserModel> userModels = userService.findAllUsersByRole(Constants.READ_ROLE_TEMPLATE + projectName.toUpperCase(Locale.ROOT));

        ProjectMembersResponse response = new ProjectMembersResponse(
                userModels.stream()
                        .map(userModel -> userModel.toUserResponse())
                        .collect(Collectors.toList())
        );

        return response;
    }

    @Override
    public List<MemberWithRolesResponse> getMembersWithRoles(String projectName) {
        ProjectModel projectModel = getProjectByName(projectName);

        if (Objects.isNull(projectModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, projectName)
            );
        }

        List<MemberWithRolesResponse> responses = new ArrayList<>();

        projectModel.getMembers().forEach(userModel -> {
            responses.add(userModel.toMemberWithRolesResponse(projectName));
        });

        return responses;
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
        return projectModel.getMembers()
                .stream()
                .anyMatch(userModel -> userModel.getUserName().equals(userName));
    }

    private final Map<String, String> defaultProjectRoles = new HashMap<>() {{
        put(Constants.OWNER_ROLE_KEY, Constants.OWNER_ROLE_TEMPLATE);
        put(Constants.READ_ROLE_KEY, Constants.READ_ROLE_TEMPLATE);
        put(Constants.WRITE_ROLE_KEY, Constants.WRITE_ROLE_TEMPLATE);
    }};

}
