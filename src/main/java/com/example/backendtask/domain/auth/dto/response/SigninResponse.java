package com.example.backendtask.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SigninResponse {

    @Schema(description = "요청 시 Authentication 헤더에 사용할 JWT 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGc...", required = true)
    private final String accessToken;

    private SigninResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static SigninResponse of (String accessToken) {
        return new SigninResponse(accessToken);
    }
}
