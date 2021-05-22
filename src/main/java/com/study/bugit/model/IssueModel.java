package com.study.bugit.model;

import com.study.bugit.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Duration;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "issues")
public class IssueModel extends BaseEntity{
    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "original_estimate")
    private Duration originalEstimate;

    @Column(name = "time_remaining")
    private Duration timeRemaining;

    @Column(name = "priority")
    private String priority;

    @ManyToOne
    private ProjectModel project;

    @ManyToOne
    private UserModel reporter;

    @ManyToOne
    private UserModel assignee;
}
