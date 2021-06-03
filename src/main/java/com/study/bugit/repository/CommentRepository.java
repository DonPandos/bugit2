package com.study.bugit.repository;

import com.study.bugit.model.CommentModel;
import com.study.bugit.model.IssueModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findAllByIssueOrderByCreatedAt(IssueModel issueModel);
}
