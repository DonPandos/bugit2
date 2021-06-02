package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.issue.CreateIssueRequest;
import com.study.bugit.dto.request.issue.UpdateIssueRequest;
import com.study.bugit.dto.response.ResponseInformation;
import com.study.bugit.dto.response.issue.IssueResponse;
import com.study.bugit.service.IssueService;
import com.study.bugit.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.DEFAULT_API_URL + "/issues")
@Slf4j
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // create issue
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_WRITE_' + #request.projectName.toUpperCase())")
    public ResponseEntity<IssueResponse> createIssue(@RequestBody CreateIssueRequest request,
                                                     @RequestAttribute("username") String userName) {
        log.info("{} -> create issue: " + request.toString());

        IssueResponse response = issueService.createIssue(request, userName);

        log.info("{} -> issue created: " + request.toString());

        response.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.ISSUE_SUCCESSFULLY_CREATED
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_WRITE_' + #request.projectName.toUpperCase())")
    public ResponseEntity<IssueResponse> updateIssue(@RequestBody UpdateIssueRequest request) {
        log.info("{} -> issue update: " + request.toString());

        IssueResponse response = issueService.updateIssue(request);

        log.info("{} -> issue updated: " + response.toString());

        response.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.ISSUE_SUCCESSFULLY_UPDATED
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete/issueNumber/{issueNumber}")
    @PreAuthorize("hasAuthority('ROLE_WRITE_' + " +
            "#issueNumber.substring(0, #issueNumber.lastIndexOf('-')).toUpperCase())")
    public ResponseEntity<IssueResponse> deleteIssue(@PathVariable String issueNumber) {
        log.info("{} -> issue delete by issueNumber: " + issueNumber);

        IssueResponse response = issueService.deleteIssue(issueNumber);

        log.info("{} -> issue deleted: " + response.toString());

        response.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.ISSUE_SUCCESSFULLY_DELETED
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/issueNumber/{issueNumber}")
    public ResponseEntity<IssueResponse> getIssue(@PathVariable String issueNumber) {
        log.info("{} -> get issue: " + issueNumber);

        IssueResponse response = issueService.getIssueByIssueNumber(issueNumber);

        log.info("{} -> get issue result: " + issueNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectName}")
    public ResponseEntity<List<IssueResponse>> getIssuesByProject(@PathVariable String projectName) {
        log.info("{} -> get project issues: " + projectName);

        List<IssueResponse> response = issueService.getIssuesByProjectName(projectName);

        log.info("{} -> get project issues result: " + response.toString());
        return ResponseEntity.ok(response);
    }

}
