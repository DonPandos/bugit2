package com.study.bugit.model;

import com.study.bugit.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentModel extends BaseEntity {

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    private UserModel owner;

    @ManyToOne
    private IssueModel issue;
}
