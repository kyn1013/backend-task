package com.example.backendtask.domain.user.enums;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ServerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String userRole;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ServerException(ErrorCode.UNAUTHORIZED_ROLE));
    }
}
