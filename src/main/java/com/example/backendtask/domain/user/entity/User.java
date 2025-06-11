package com.example.backendtask.domain.user.entity;

import com.example.backendtask.domain.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

@Getter
public class User {

    @Schema(description = "이메일(로그인시 아이디가 됨)", example = "kimyena@example.com", required = true)
    private String email;

    @Schema(description = "비밀번호", example = "0000", required = true)
    private String password;

    @Schema(description = "별명", example = "예나리자", required = true)
    private String nickName;

    @Schema(description = "유저 권한", example = "ROLE_USER 혹은 ROLE_ADMIN, 여러개의 권한을 가질 수 있음", required = true)
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
