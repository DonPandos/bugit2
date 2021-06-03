package com.study.bugit.service.impl;

import com.study.bugit.constants.Constants;
import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.issue.CreateIssueRequest;
import com.study.bugit.dto.request.issue.LogTimeRequest;
import com.study.bugit.dto.request.issue.UpdateIssueRequest;
import com.study.bugit.dto.response.issue.IssueResponse;
import com.study.bugit.dto.response.issue.LogTimeResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.model.IssueModel;
import com.study.bugit.model.LoggedTimeModel;
import com.study.bugit.model.ProjectModel;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.IssueRepository;
import com.study.bugit.repository.LoggedTimeRepository;
import com.study.bugit.service.IssueService;
import com.study.bugit.service.ProjectService;
import com.study.bugit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final LoggedTimeRepository loggedTimeRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public IssueServiceImpl(IssueRepository issueRepository, LoggedTimeRepository loggedTimeRepository, ProjectService projectService, UserService userService) {
        this.issueRepository = issueRepository;
        this.loggedTimeRepository = loggedTimeRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public IssueResponse createIssue(CreateIssueRequest request, String reporterUsername) {

        ProjectModel projectModel = projectService.getProjectByName(request.getProjectName());
        if (Objects.isNull(projectModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.PROJECT_WITH_NAME_NOT_EXISTS_FORMAT, request.getProjectName())
            );
        }

        UserModel assigneeUserModel = userService.checkUserRole(
                request.getAssigneeUsername(),
                Constants.READ_ROLE_TEMPLATE + projectModel.getName().toUpperCase(Locale.ROOT)
        );

        if (Objects.isNull(assigneeUserModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USERNAME_NOT_MEMBER_OF_PROJECT, request.getAssigneeUsername(), projectModel.getName())
            );
        }

        UserModel reporterUserModel = userService.checkUserRole(
                reporterUsername,
                Constants.WRITE_ROLE_TEMPLATE + projectModel.getName().toUpperCase(Locale.ROOT)
        );

        IssueModel issueModel = IssueModel.fromCreateRequest(
                request,
                projectModel,
                reporterUserModel,
                assigneeUserModel
        );

        issueRepository.save(issueModel);

        return issueModel.toResponse();
    }

    @Override
    public IssueResponse updateIssue(UpdateIssueRequest request) {
        IssueModel issueModel = issueRepository.findByIssueNumber(request.getIssueNumber());

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, request.getIssueNumber())
            );
        }

        String projectName = issueModel.getProject().getName();

        if (!request.getAssigneeUsername().equals(issueModel.getAssignee().getUserName())) {
            UserModel assigneeUserModel = userService.checkUserRole(
                    request.getAssigneeUsername(),
                    Constants.READ_ROLE_TEMPLATE + projectName.toUpperCase(Locale.ROOT)
            );

            if (Objects.isNull(assigneeUserModel)) {
                throw new CustomException(
                        HttpStatus.BAD_REQUEST,
                        String.format(ErrorConstants.USERNAME_NOT_MEMBER_OF_PROJECT, assigneeUserModel.getUserName(), projectName)
                );
            }

            issueModel.setAssignee(assigneeUserModel);
        }

        issueModel.update(request);

        issueRepository.save(issueModel);
        return issueModel.toResponse();
    }

    @Override
    public IssueResponse deleteIssue(String issueNumber) {
        IssueModel issueModel = issueRepository.findByIssueNumber(issueNumber);

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, issueNumber)
            );
        }
        issueRepository.delete(issueModel);
        return issueModel.toResponse();
    }

    @Override
    public IssueResponse getIssueByIssueNumber(String issueNumber) {
        IssueModel issueModel = issueRepository.findByIssueNumber(issueNumber);

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, issueNumber)
            );
        }

        return issueModel.toResponse();
    }

    @Override
    public List<IssueResponse> getIssuesByProjectName(String projectName) {
        ProjectModel projectModel = projectService.getProjectByName(projectName);

        List<IssueModel> issueModels = issueRepository.findALlByProjectOrderByCreatedAt(projectModel);
        List<IssueResponse> issueResponses = issueModels.stream()
                .map(IssueModel::toResponse)
                .collect(Collectors.toList());

        return issueResponses;
    }

    @Override
    public LogTimeResponse logTime(LogTimeRequest request, String username) {

        IssueModel issueModel = issueRepository.findByIssueNumber(request.getIssueNumber());

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, request.getIssueNumber())
            );
        }

        long timeRemaining = issueModel.getTimeRemaining().toSeconds();

        issueModel.setTimeRemaining(timeRemaining > request.getTimeInSeconds() ?
                Duration.ofSeconds(timeRemaining - request.getTimeInSeconds()) :
                Duration.ofSeconds(0L)
        );

        issueModel.setLoggedTime(issueModel.getLoggedTime().plusSeconds(request.getTimeInSeconds()));

        issueRepository.save(issueModel);

        UserModel userModel = userService.findUserByUsername(username);
        LoggedTimeModel loggedTimeModel = LoggedTimeModel.fromLogTimeRequest(
                request,
                issueModel,
                userModel
        );

        loggedTimeRepository.save(loggedTimeModel);

        return loggedTimeModel.toResponse();
    }
}
