package com.study.bugit.model;

import com.study.bugit.dto.request.issue.CreateIssueRequest;
import com.study.bugit.dto.request.issue.UpdateIssueRequest;
import com.study.bugit.dto.response.issue.IssueResponse;
import com.study.bugit.model.users.UserModel;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "issues")
public class IssueModel extends BaseEntityWithoutId implements Serializable {
    @Id
    @Column(name = "issue_number")
    @GenericGenerator(name = "issue_number", strategy = "com.study.bugit.utils.IssueIdentificatorGenerator")
    @GeneratedValue(generator = "issue_number")
    private String issueNumber;

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

    @Column(name = "logged_time")
    private Duration loggedTime;

    @Column(name = "priority")
    private String priority;

    @ManyToOne()
    private ProjectModel project;

    @ManyToOne
    private UserModel reporter;

    @ManyToOne
    private UserModel assignee;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issue")
    private List<CommentModel> comments = Collections.emptyList();

    public static IssueModel fromCreateRequest(CreateIssueRequest request,
                                               ProjectModel projectModel,
                                               UserModel reporter,
                                               UserModel assignee) {
        IssueModel issueModel = new IssueModel();
        issueModel.setStatus(request.getStatus());
        issueModel.setName(request.getName());
        issueModel.setDescription(request.getDescription());
        issueModel.setOriginalEstimate(Duration.ofSeconds(request.getOriginalEstimate()));
        issueModel.setTimeRemaining(Duration.ofSeconds(request.getTimeRemaining()));
        issueModel.setLoggedTime(Duration.ofSeconds(0));
        issueModel.setPriority(request.getPriority());
        issueModel.setProject(projectModel);
        issueModel.setReporter(reporter);
        issueModel.setAssignee(assignee);

        return issueModel;
    }

    public IssueResponse toResponse() {
        return new IssueResponse(
                issueNumber,
                status,
                name,
                description,
                originalEstimate,
                timeRemaining,
                priority,
                project.getName(),
                Objects.isNull(assignee) ? null : assignee.toUserResponse(),
                Objects.isNull(reporter) ? null : reporter.toUserResponse()
        );
    }

    public void update(UpdateIssueRequest request) {
        this.status = request.getStatus();
        this.name = request.getName();
        this.description = request.getDescription();
        this.originalEstimate = Duration.ofSeconds(request.getOriginalEstimate());
        this.timeRemaining = Duration.ofSeconds(request.getTimeRemaining());
        this.priority = request.getPriority();
    }
}
