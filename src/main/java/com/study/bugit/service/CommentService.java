package com.study.bugit.service;

import com.study.bugit.dto.request.comment.CreateCommentRequest;
import com.study.bugit.dto.response.comment.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CreateCommentRequest request, String username);
    CommentResponse deleteComment(Long commentId, String username);
    List<CommentResponse> getCommentsByIssueNumber(String issueNumber);
}
