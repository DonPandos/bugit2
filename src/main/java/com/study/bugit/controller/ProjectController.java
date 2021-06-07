package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.BaseResponse;
import com.study.bugit.dto.response.ResponseInformation;
import com.study.bugit.dto.response.project.MemberWithRolesResponse;
import com.study.bugit.dto.response.project.ProjectMembersResponse;
import com.study.bugit.dto.response.project.ProjectResponse;
import com.study.bugit.dto.response.project.UserProjectRolesResponse;
import com.study.bugit.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
@RestController
@RequestMapping(Constants.DEFAULT_API_URL + "/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@RequestAttribute("username") String username) {
        log.info("{} -> getProjects(username:" + username + ")");

        return ResponseEntity.ok(projectService.getAllByUsernameWhereMember(username));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody CreateProjectRequest request,
                                                         @RequestAttribute("username") String username) {
        log.info("{} -> create project : " + request.toString());

        request.setSenderUsername(username);

        ProjectResponse response = projectService.create(request);

        response.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.PROJECT_SUCCESSFULLY_CREATED
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectname}/addmember/{username}")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #projectName.toUpperCase())")
    public ResponseEntity<MemberWithRolesResponse> addMember(@PathVariable("projectname") String projectName,
                                                             @PathVariable("username") String userName) {
        log.info("{} -> add member : " + userName + " add to " + projectName);

        MemberWithRolesResponse response = projectService.addMemberToProject(projectName, userName);

        response.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        String.format(Constants.MEMBER_ADDED_TO_PROJECT_FORMAT, userName, projectName)
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #request.projectName.toUpperCase())")
    public ResponseEntity<BaseResponse> changeMemberRoles(@RequestBody ChangeMemberRolesRequest request,
                                                          @RequestAttribute("username") String username) {
        log.info("{} -> changeMemberRoles : " + request.toString());

        request.setSenderUsername(username);

        projectService.changeMemberRoles(request);

        BaseResponse response = new BaseResponse(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        String.format(Constants.ROLES_SUCCESSFULLY_CHANGED_TO_USER_FORMAT, request.getUserName(), request.getProjectName())
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles/project/{projectName}/username/{username}")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #projectName.toUpperCase())")
    public ResponseEntity<UserProjectRolesResponse> getUserProjectRoles(@PathVariable String projectName,
                                                                        @PathVariable String username) {
        log.info("{} -> check roles : " + username + " on project " + projectName);

        UserProjectRolesResponse response = projectService.getUsernameRoles(projectName, username);

        log.info("{} -> check roles result: " + response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/projectName/{projectName}")
    @PreAuthorize("hasAuthority('ROLE_READ_' + #projectName.toUpperCase())")
    public ResponseEntity<ProjectMembersResponse> getProjectMembers(@PathVariable String projectName) {
        log.info("{} -> get project members: " + projectName);

        ProjectMembersResponse response = projectService.getProjectMembers(projectName);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/membersWithRoles/projectName/{projectName}")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #projectName.toUpperCase())")
    public ResponseEntity<List<MemberWithRolesResponse>> getMembersWithRoles(@PathVariable String projectName) {
        log.info("{} -> get members with roles: " + projectName);

        List<MemberWithRolesResponse> responses = projectService.getMembersWithRoles(projectName);

        log.info("{} -> get members with roles response: " + responses.toString());

        return ResponseEntity.ok(responses);
    }
}