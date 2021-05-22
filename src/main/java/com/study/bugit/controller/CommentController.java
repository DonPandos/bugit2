package com.study.bugit.controller;

import com.study.bugit.constants.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.DEFAULT_API_URL + "/comments")
public class CommentController {
}
