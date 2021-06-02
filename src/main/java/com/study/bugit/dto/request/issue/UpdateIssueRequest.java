package com.study.bugit.dto.request.issue;

import lombok.Data;

@Data
public class UpdateIssueRequest {
    private String issueNumber;
    private String status;
    private String name;
    private String description;
    private Long originalEstimate;
    private Long timeRemaining;
    private String priority;
    private String assigneeUsername;
}
