package com.study.bugit.dto.request.project;

import com.study.bugit.dto.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChangeMemberRolesRequest extends BaseRequest {
    private String userName;
    private String projectName;
    private List<String> roleKeyList;
}
