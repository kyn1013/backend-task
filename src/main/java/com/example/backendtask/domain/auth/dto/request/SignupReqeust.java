package com.example.backendtask.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReqeust {

    private String username;
    private String password;
    private String nickName;
    private String userRole;
}
