package com.study.bugit.jwt;

import com.study.bugit.model.users.Status;
import com.study.bugit.model.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;
    private Status status;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static UserDetailsImpl fromUserModel(UserModel userModel) {
        return new UserDetailsImpl(
                userModel.getUserName(),
                userModel.getPassword(),
                userModel.getStatus(),
                userModel.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.isActive();
    }

    @Override
    public boolean isEnabled() {
        return status.isActive();
    }
}
