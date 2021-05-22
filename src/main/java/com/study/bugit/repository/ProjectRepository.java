package com.study.bugit.repository;

import com.study.bugit.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
    List<ProjectModel> findAllByOwnerOrderByCreatedAt(String username);
    ProjectModel findByName(String projectName);
}
