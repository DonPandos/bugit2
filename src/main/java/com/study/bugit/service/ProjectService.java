package com.study.bugit.service;

import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.project.ProjectResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getByUsername(String username);
    ProjectResponse create(CreateProjectRequest request);
    void addMemberToProject(String projectName, String userName);
    void changeMemberRoles(ChangeMemberRolesRequest request);
}
