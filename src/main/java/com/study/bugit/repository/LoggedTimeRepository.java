package com.study.bugit.repository;

import com.study.bugit.model.LoggedTimeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggedTimeRepository extends JpaRepository<LoggedTimeModel, Long> {
}
