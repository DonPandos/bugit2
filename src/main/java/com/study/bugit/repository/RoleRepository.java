package com.study.bugit.repository;

import com.study.bugit.model.users.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    RoleModel findByRole(String role);
}
