package com.study.bugit.model.users;

import com.study.bugit.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleModel extends BaseEntity {
    @Column(name = "role", unique = true)
    private String role;
}
