package com.study.bugit.service.impl;

import com.study.bugit.constants.Constants;
import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.dto.request.comment.CreateCommentRequest;
import com.study.bugit.dto.response.comment.CommentResponse;
import com.study.bugit.exception.CustomException;
import com.study.bugit.model.CommentModel;
import com.study.bugit.model.IssueModel;
import com.study.bugit.model.users.UserModel;
import com.study.bugit.repository.CommentRepository;
import com.study.bugit.repository.IssueRepository;
import com.study.bugit.service.CommentService;
import com.study.bugit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, IssueRepository issueRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userService = userService;
    }

    @Override
    public CommentResponse createComment(CreateCommentRequest request, String username) {

        IssueModel issueModel = issueRepository.findByIssueNumber(request.getIssueNumber());

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, request.getIssueNumber())
            );
        }

        UserModel userModel = userService.findUserByUsername(username);

        if (Objects.isNull(userModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT, username)
            );
        }

        CommentModel commentModel = new CommentModel(
                request.getText(),
                userModel,
                issueModel
        );

        return commentRepository.save(commentModel).toResponse();
    }

    @Override
    public CommentResponse deleteComment(Long commentId, String username) {
        CommentModel commentModel = commentRepository.findById(commentId).orElse(null);

        if (Objects.isNull(commentModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    ErrorConstants.COMMENT_DOES_NOT_EXISTS
            );
        }

        String issueNumber = commentModel.getIssue().getIssueNumber();

        if (Objects.isNull(userService.checkUserRole(
                username,
                Constants.READ_ROLE_TEMPLATE + issueNumber.substring(0, issueNumber.lastIndexOf("-")).toUpperCase(Locale.ROOT)))
        ) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    ErrorConstants.DONT_HAVE_PERMISSIONS
            );
        }

        commentRepository.delete(commentModel);

        return commentModel.toResponse();
    }

    @Override
    public List<CommentResponse> getCommentsByIssueNumber(String issueNumber) {
        IssueModel issueModel = issueRepository.findByIssueNumber(issueNumber);

        if (Objects.isNull(issueModel)) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    String.format(ErrorConstants.ISSUE_DOES_NOT_EXISTS, issueNumber)
            );
        }

        List<CommentModel> commentModels = commentRepository.findAllByIssueOrderByCreatedAt(issueModel);
        List<CommentResponse> commentResponses = commentModels.stream()
                .map(CommentModel::toResponse)
                .collect(Collectors.toList());

        return commentResponses;
    }
}
