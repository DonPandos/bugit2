package com.study.bugit.dto.request.comment;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String issueNumber;
    private String text;
}
