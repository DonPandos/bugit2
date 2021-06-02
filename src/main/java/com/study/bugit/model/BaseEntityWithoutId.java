package com.study.bugit.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public class BaseEntityWithoutId {

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @PrePersist
    private void onPersist() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.modifiedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    private void onUpdate() {
        this.modifiedAt = new Timestamp(System.currentTimeMillis());
    }
}
