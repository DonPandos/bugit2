package com.study.bugit.constants;

public class ErrorConstants {

    //jwt
    public static final String JWT_TOKEN_EXPIRED = "JWT is expired";
    public static final String UNSUPPORTED_JWT = "Unsupported JWT";
    public static final String MALFORMED_JWT = "Malformed JWT";
    public static final String INVALID_SIGNATURE = "Invalid signature";
    public static final String INVALID_JWT = "Invalid JWT";

    //signup/signin
    public static final String USER_WITH_THIS_USERNAME_ALREADY_EXISTS = "User with this username already exists";
    public static final String USER_WITH_THIS_USERNAME_DOES_NOT_EXISTS_FORMAT = "User with username %s does not exists";
    public static final String INCORRECT_PASSWORD = "Password is incorrect";

    //projects
    public static final String PROJECT_WITH_NAME_ALREADY_EXISTS_FORMAT = "Project with name %s already exists";
    public static final String PROJECT_WITH_NAME_NOT_EXISTS_FORMAT = "Project with name %s does not exists";
    public static final String USER_ALREADY_MEMBER_OF_PROJECT_FORMAT = "User %s already member of project %s";
    public static final String USERNAME_NOT_MEMBER_OF_PROJECT = "User %s not member of %s project";

    //issues
    public static final String ISSUE_DOES_NOT_EXISTS = "Issue %s does not exists";


}
