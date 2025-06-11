package com.example.backendtask.common.response;

import com.example.backendtask.common.exception.constant.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "에러 발생 시 응답 코드")
public class ErrorResponse {

    @Schema(description = "에러 이름", example = "TOKEN_NOT_FOUND")
    private String name;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int statusCode;

    @Schema(description = "에러 메시지", example = "JWT 토큰이 없습니다.")
    private String message;

    private ErrorResponse(String name, int statusCode, String message) {
        this.name = name;
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.name(), code.getStatus().value(), code.getMessage());
    }
}
