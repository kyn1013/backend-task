package com.example.backendtask.domain.user.controller;

import com.example.backendtask.common.response.ErrorResponse;
import com.example.backendtask.domain.auth.dto.response.SigninResponse;
import com.example.backendtask.domain.user.dto.request.RoleRequest;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "관리자만 사용할 수 있는 API")
public class UserController {

    private final UserService userService;

    /*
     * 모든 회원 정보 조회 (관리자만 가능)
     */
    @Operation(
            summary = "전체 회원 정보 조회",
            description = "권한이 ADMIN인 유저만 실행할 수 있는 전체 회원 정보 조회, JWT를 Authorization 헤더에 입력 필요함",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)),
                                    examples = @ExampleObject(
                                            name = "회원 목록 예시",
                                            value = """
                    [
                      {
                        "email": "admin@test.com",
                        "password": "$2a$10$hashed...",
                        "nickName": "관리자",
                        "userRoles": ["ROLE_ADMIN"]
                      },
                      {
                        "email": "user@test.com",
                        "password": "$2a$10$hashed...",
                        "nickName": "일반회원",
                        "userRoles": ["ROLE_USER"]
                      }
                    ]
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT 없음 / 만료 / 유효하지 않음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "JWT 없음 예시",
                                            value = """
                    {
                      "name": "TOKEN_NOT_FOUND",
                      "statusCode": 401,
                      "message": "JWT 토큰이 없습니다."
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "일반 회원이 호출하여 회원 조회 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "접근 권한 없음 예시",
                                            value = """
                    {
                      "name": "ACCESS_DENIED",
                      "statusCode": 403,
                      "message": "접근 권한이 없습니다."
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "지원되지 않는 JWT 토큰",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "지원되지 않는 JWT 예시",
                                            value = """
                    {
                      "name": "UNSUPPORTED_JWT_TOKEN",
                      "statusCode": 400,
                      "message": "지원되지 않는 JWT 토큰입니다."
                    }
                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    /*
     * 일반 회원에게 권한 부여 (관리자만 가능)
     */
    @Operation(
            summary = "권한 부여",
            description = "권한이 ADMIN인 유저가 다른 유저에게 추가적인 권한을 부여, JWT를 Authorization 헤더에 입력 필요함",
            security = {@SecurityRequirement(name = "Authorization")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "권한 부여 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)),
                                    examples = @ExampleObject(
                                            name = "권한 부여 성공 예시",
                                            value = """
                    [
                      {
                        "email": "minji@test.com",
                        "password": "$2a$10$hashed...",
                        "nickName": "minji",
                        "userRoles": ["ROLE_USER", "ROLE_ADMIN"]
                      }
                    ]
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT 없음 / 만료 / 유효하지 않음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "JWT 없음 예시",
                                            value = """
                    {
                      "name": "TOKEN_NOT_FOUND",
                      "statusCode": 401,
                      "message": "JWT 토큰이 없습니다."
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "일반 회원이 호출하여 권한 부여 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "접근 권한 없음 예시",
                                            value = """
                    {
                      "name": "ACCESS_DENIED",
                      "statusCode": 403,
                      "message": "접근 권한이 없습니다."
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "지원되지 않는 JWT로 요청하여 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "지원되지 않는 JWT 예시",
                                            value = """
                    {
                      "name": "UNSUPPORTED_JWT_TOKEN",
                      "statusCode": 400,
                      "message": "지원되지 않는 JWT 토큰입니다."
                    }
                    """
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/api/v1/admin/roles")
    public ResponseEntity<User> grantRole(@RequestBody RoleRequest roleRequest) {
        User user = userService.grantAdmin(roleRequest);
        return ResponseEntity.ok().body(user);
    }
}
