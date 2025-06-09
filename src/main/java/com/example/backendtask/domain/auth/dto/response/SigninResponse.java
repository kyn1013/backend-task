package com.example.backendtask.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String accessToken;

    private SigninResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static SigninResponse of (String accessToken) {
        return new SigninResponse(accessToken);
    }
}
