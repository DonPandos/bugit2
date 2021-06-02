package com.study.bugit.model.users;

import com.study.bugit.dto.request.auth.RegisterRequest;
import com.study.bugit.dto.response.UserResponse;
import com.study.bugit.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserModel extends BaseEntity {

    @Column(name = "username", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Set<RoleModel> roles;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public static UserModel fromRegisterRequest(RegisterRequest request) {
        UserModel userModel = new UserModel();
        userModel.setUserName(request.getUsername());
        userModel.setPassword(request.getPassword());
        userModel.setEmail(request.getEmail());
        userModel.setFirstName(request.getFirstName());
        userModel.setLastName(request.getLastName());

        return userModel;
    }

    public UserResponse toUserResponse() {
        return new UserResponse(
                userName,
                email,
                firstName,
                lastName
        );
    }
}
