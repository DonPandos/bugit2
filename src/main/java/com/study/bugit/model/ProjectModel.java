package com.study.bugit.model;

import com.study.bugit.dto.request.project.CreateProjectRequest;
import com.study.bugit.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class ProjectModel extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "owner")
    private String owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projects_members",
            joinColumns = @JoinColumn(
                    name = "project_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            )
    )
    private List<UserModel> members;

    public static ProjectModel createModel(CreateProjectRequest request, List<UserModel> members) {
        return new ProjectModel(
                request.getName(),
                request.getDescription(),
                request.getSenderUsername(),
                members
        );
    }
}
