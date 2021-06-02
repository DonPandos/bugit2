package com.study.bugit.repository;

import com.study.bugit.model.IssueModel;
import com.study.bugit.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<IssueModel, String> {
    IssueModel findByIssueNumber(String issueNumber);
    List<IssueModel> findALlByProjectOrderByCreatedAt(ProjectModel projectModel);
}
