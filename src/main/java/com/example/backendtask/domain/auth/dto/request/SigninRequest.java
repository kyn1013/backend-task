package com.example.backendtask.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SigninRequest {

    @Schema(description = "이메일(로그인시 아이디가 됨)", example = "kimyena@example.com", required = true)
    private String email;

    @Schema(description = "비밀번호", example = "0000", required = true)
    private String password;
}
