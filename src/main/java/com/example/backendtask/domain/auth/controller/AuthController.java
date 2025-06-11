package com.example.backendtask.domain.auth.controller;

import com.example.backendtask.common.response.ErrorResponse;
import com.example.backendtask.domain.auth.dto.request.SigninRequest;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.auth.dto.response.SigninResponse;
import com.example.backendtask.domain.auth.service.AuthService;
import com.example.backendtask.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "회원가입",
            description = "이메일, 비밀번호, 닉네임, 유저권한을 기입하여 회원가입",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "회원가입 성공 예시",
                                            value = """
                    {
                      "email": "kyn@test.com",
                      "password": "$2a$10$hashedpassword...",
                      "nickName": "예나",
                      "userRoles": ["ROLE_USER"]
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "이메일 중복",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "이메일 중복 예시",
                                            value = """
                    {
                      "name": "USER_ALREADY_EXISTS",
                      "statusCode": 400,
                      "message": "이미 존재하는 이메일입니다."
                    }
                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<User> signup(@RequestBody SignupReqeust signupReqeust) {
        User user = authService.signup(signupReqeust);
        return ResponseEntity.ok().body(user);
    }

    @Operation(
            summary = "로그인",
            description = "이메일, 비밀번호를 사용하여 로그인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "로그인 성공 예시",
                                            value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "비밀번호 불일치",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "비밀번호 불일치 예시",
                                            value = """
                    {
                      "name": "INVALID_PASSWORD",
                      "statusCode": 401,
                      "message": "비밀번호가 일치하지 않습니다."
                    }
                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/api/v1/auth/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signin(signinRequest);
        return ResponseEntity.ok().body(signinResponse);
    }

}
