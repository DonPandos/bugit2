package com.study.bugit.service;

import com.study.bugit.dto.request.issue.CreateIssueRequest;
import com.study.bugit.dto.request.issue.LogTimeRequest;
import com.study.bugit.dto.request.issue.UpdateIssueRequest;
import com.study.bugit.dto.response.issue.IssueResponse;
import com.study.bugit.dto.response.issue.LogTimeResponse;

import java.util.List;

public interface IssueService {
    IssueResponse createIssue(CreateIssueRequest request, String reporterUsername);
    IssueResponse updateIssue(UpdateIssueRequest request);
    IssueResponse deleteIssue(String issueNumber);
    IssueResponse getIssueByIssueNumber(String issueNumber);
    List<IssueResponse> getIssuesByProjectName(String projectName);
    LogTimeResponse logTime(LogTimeRequest request, String username);
}
