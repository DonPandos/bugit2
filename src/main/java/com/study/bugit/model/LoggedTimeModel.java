package com.study.bugit.model;

import com.study.bugit.dto.request.issue.LogTimeRequest;
import com.study.bugit.dto.response.issue.LogTimeResponse;
import com.study.bugit.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;

@Table(name = "logged_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LoggedTimeModel extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private IssueModel issue;

    @Column(name = "time_logged")
    private Duration timeLogged;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserModel user;

    public static LoggedTimeModel fromLogTimeRequest(LogTimeRequest request,
                                                     IssueModel issueModel,
                                                     UserModel user) {
        return new LoggedTimeModel(
                issueModel,
                Duration.ofSeconds(request.getTimeInSeconds()),
                user
        );
    }

    public LogTimeResponse toResponse() {
        return new LogTimeResponse(
                timeLogged,
                issue.getIssueNumber(),
                issue.getTimeRemaining(),
                issue.getLoggedTime()
        );
    }
}
