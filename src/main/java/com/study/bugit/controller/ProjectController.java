package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.BaseResponse;
import com.study.bugit.dto.response.project.ProjectResponse;
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

        return ResponseEntity.ok(projectService.getByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody CreateProjectRequest request,
                                                         @RequestAttribute("username") String username) {
        log.info("{} -> create project : " + request.toString());

        request.setSenderUsername(username);

        ProjectResponse response = projectService.create(request);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(Constants.PROJECT_SUCCESSFULLY_CREATED);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectname}/addmember/{username}")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #projectName.toUpperCase())")
    public ResponseEntity<BaseResponse> addMember(@PathVariable("projectname") String projectName,
                                                  @PathVariable("username") String userName) {
        log.info("{} -> add member : " + userName + " add to " + projectName);

        projectService.addMemberToProject(projectName, userName);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                String.format(Constants.MEMBER_ADDED_TO_PROJECT_FORMAT, userName, projectName)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_OWNER_' + #request.projectName.toUpperCase())")
    public ResponseEntity<BaseResponse> changeMemberRoles(@RequestBody ChangeMemberRolesRequest request,
                                                          @RequestAttribute("username") String userName) {
        log.info("{} -> changeMemberRoles : " + request.toString());

        request.setSenderUsername(userName);

        //todo
        projectService.changeMemberRoles(request);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                String.format(Constants.ROLES_SUCCESSFULLY_CHANGED_TO_USER_FORMAT, request.getUserName(), request.getProjectName())
        );

        return ResponseEntity.ok(response);
    }
}