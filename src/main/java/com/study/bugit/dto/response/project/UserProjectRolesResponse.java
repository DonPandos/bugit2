package com.study.bugit.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProjectRolesResponse {
    private List<String> roles;
}
