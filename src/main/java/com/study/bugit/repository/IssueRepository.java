package com.study.bugit.repository;

import com.study.bugit.model.IssueModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueModel, Long> {
}
