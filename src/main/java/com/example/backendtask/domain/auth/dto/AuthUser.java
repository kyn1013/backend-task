package com.example.backendtask.domain.auth.dto;

import com.example.backendtask.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
public class AuthUser {

    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String email, List<UserRole> roles) {
        this.email = email;
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }
}
