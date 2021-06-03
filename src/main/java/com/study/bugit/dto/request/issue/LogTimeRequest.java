package com.study.bugit.dto.request.issue;

import lombok.Data;

@Data
public class LogTimeRequest {
    private String issueNumber;
    private Long timeInSeconds;
}
