package com.study.bugit.dto.request.auth;

import com.study.bugit.dto.request.BaseRequest;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest extends BaseRequest {
    private String username;
    private String password;
}
