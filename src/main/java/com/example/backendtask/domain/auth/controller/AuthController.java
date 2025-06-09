package com.example.backendtask.domain.auth.controller;

import com.example.backendtask.domain.auth.dto.request.SigninRequest;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.auth.dto.response.SigninResponse;
import com.example.backendtask.domain.auth.service.AuthService;
import com.example.backendtask.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<User> signup(@RequestBody SignupReqeust signupReqeust) {
        User user = authService.signup(signupReqeust);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/api/v1/auth/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signin(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

}
