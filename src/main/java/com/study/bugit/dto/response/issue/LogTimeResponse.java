package com.study.bugit.dto.response.issue;

import com.study.bugit.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class LogTimeResponse extends BaseResponse {
    private Duration loggedTime;
    private String issueNumber;
    private Duration timeRemaining;
    private Duration issueLoggedTime;
}
