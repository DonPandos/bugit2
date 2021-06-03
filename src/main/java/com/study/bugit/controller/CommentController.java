package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import com.study.bugit.dto.request.comment.CreateCommentRequest;
import com.study.bugit.dto.response.ResponseInformation;
import com.study.bugit.dto.response.comment.CommentResponse;
import com.study.bugit.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.DEFAULT_API_URL + "/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_READ_' + " +
            "#request.issueNumber.substring(0, #request.issueNumber.lastIndexOf('-')).toUpperCase())")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest request,
                                                         @RequestAttribute String username) {
        log.info("{} -> create comment: " + request.toString());

        CommentResponse commentResponse = commentService.createComment(request, username);

        commentResponse.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.COMMENT_SUCCESSFULLY_ADDED
                )
        );

        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable("id") Long commentId,
                                                         @RequestAttribute("username") String username) {
        log.info("{} -> delete comment: " + commentId);

        CommentResponse commentResponse = commentService.deleteComment(commentId, username);

        commentResponse.setResponseInformation(
                new ResponseInformation(
                        HttpStatus.OK.value(),
                        Constants.COMMENT_SUCCESSFULLY_DELETED
                )
        );

        return ResponseEntity.ok(commentResponse);
    }

    @GetMapping("/issueNumber/{issueNumber}")
    @PreAuthorize("hasAuthority('ROLE_READ_' + " +
            "#issueNumber.substring(0, #issueNumber.lastIndexOf('-')).toUpperCase())")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String issueNumber) {
        log.info("{} -> get all comments: " + issueNumber);

        List<CommentResponse> commentResponses = commentService.getCommentsByIssueNumber(issueNumber);

        return ResponseEntity.ok(commentResponses);
    }
}
