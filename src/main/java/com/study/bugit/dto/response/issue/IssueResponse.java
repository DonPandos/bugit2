package com.study.bugit.dto.response.issue;

import com.study.bugit.dto.response.BaseResponse;
import com.study.bugit.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class IssueResponse extends BaseResponse {
    private String issueNumber;
    private String status;
    private String name;
    private String description;
    private Duration originalEstimate;
    private Duration timeRemaining;
    private String priority;
    private String projectName;
    private UserResponse assignee;
    private UserResponse reporter;
}
