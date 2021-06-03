package com.study.bugit.dto.response.comment;

import com.study.bugit.dto.response.BaseResponse;
import com.study.bugit.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse extends BaseResponse {
    private String text;
    private UserResponse owner;
    private String issueNumber;
}
