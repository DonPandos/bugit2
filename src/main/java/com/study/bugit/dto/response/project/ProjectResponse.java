package com.study.bugit.dto.response.project;

import com.study.bugit.dto.response.BaseResponse;
import com.study.bugit.model.ProjectModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse extends BaseResponse {
    private String name;
    private String description;


    public static ProjectResponse fromProjectModel(ProjectModel projectModel) {
        return new ProjectResponse(
                projectModel.getName(),
                projectModel.getDescription()
        );
    }
}
