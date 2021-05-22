package com.study.bugit.dto.request.project;

import com.study.bugit.dto.request.BaseRequest;
import lombok.Data;

@Data
public class CreateProjectRequest extends BaseRequest {
    private String name;
    private String description;
}
