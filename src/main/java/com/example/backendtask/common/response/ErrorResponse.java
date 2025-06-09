package com.example.backendtask.common.response;

import com.example.backendtask.common.exception.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String name;
    private int statusCode;
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
