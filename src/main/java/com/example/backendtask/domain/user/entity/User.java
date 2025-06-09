package com.example.backendtask.domain.user.entity;

import com.example.backendtask.domain.user.enums.UserRole;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

@Getter
public class User {

    private String email;

    private String password;

    private String nickName;

    private Set<UserRole> userRoles = new HashSet<>();

    private User(String email, String password, String nickName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRoles.add(userRole);
    }

    public static User of(String email, String password, String nickName, UserRole userRole) {
        return new User(email, password, nickName, userRole);
    }

    /*
     * 권한 추가
     */
    public void addRole(UserRole userRole) {
        this.userRoles.add(userRole);
    }

}
