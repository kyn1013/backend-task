package com.example.backendtask.common.exception.object;

import com.example.backendtask.common.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private ErrorCode errorCode;

    public ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
