package com.example.backendtask.domain.auth.controller;

import com.example.backendtask.domain.auth.dto.request.SigninRequest;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.auth.dto.response.SigninResponse;
import com.example.backendtask.domain.auth.service.AuthService;
import com.example.backendtask.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "회원가입 및 로그인 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임, 유저권한을 기입하여 회원가입")
    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<User> signup(@RequestBody SignupReqeust signupReqeust) {
        User user = authService.signup(signupReqeust);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "로그인", description = "이메일, 비밀번호를 사용하여 로그인")
    @PostMapping("/api/v1/auth/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signin(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

}
