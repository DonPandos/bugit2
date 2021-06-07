package com.study.bugit.service;

import com.study.bugit.dto.request.project.ChangeMemberRolesRequest;
import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.dto.response.project.MemberWithRolesResponse;
import com.study.bugit.dto.response.project.ProjectMembersResponse;
import com.study.bugit.dto.response.project.ProjectResponse;
import com.study.bugit.dto.response.project.UserProjectRolesResponse;
import com.study.bugit.model.ProjectModel;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllByUsernameWhereMember(String username);
    ProjectResponse create(CreateProjectRequest request);
    MemberWithRolesResponse addMemberToProject(String projectName, String userName);
    void changeMemberRoles(ChangeMemberRolesRequest request);
    ProjectModel getProjectByName(String projectName);
    ProjectModel getProjectByIssueNumber(String issueNumber);
    UserProjectRolesResponse getUsernameRoles(String projectName, String username);
    ProjectMembersResponse getProjectMembers(String projectName);
    List<MemberWithRolesResponse> getMembersWithRoles(String projectName);
}
