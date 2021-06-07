package com.study.bugit.dto.response.project;

import com.study.bugit.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberWithRolesResponse extends BaseResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
