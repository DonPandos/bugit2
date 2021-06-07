package com.study.bugit.dto.response.project;

import com.study.bugit.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectMembersResponse {
    List<UserResponse> members;
}
