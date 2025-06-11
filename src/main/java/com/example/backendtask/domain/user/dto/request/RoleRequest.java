package com.example.backendtask.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleRequest {

    @Schema(description = "권한을 부여할 유저의 이메일", example = "kimyena@example.com", required = true)
    private String email;

    @Schema(description = "부여할 권한", example = "ROLE_ADMIN 혹은 ROLE_USER", required = true)
    private String role;
}
