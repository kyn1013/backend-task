package com.example.backendtask.domain.user.entity;

import com.example.backendtask.domain.user.enums.UserRole;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

@Getter
public class User {

    private String username;

    private String password;

    private String nickName;

    private Set<UserRole> userRoles = new HashSet<>();

    public User(String username, String password, String nickName, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.userRoles.add(userRole);
    }

    /*
     * 권한 추가
     */
    public void addRole(UserRole userRole) {
        this.userRoles.add(userRole);
    }

}
