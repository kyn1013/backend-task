package com.example.backendtask.domain.user.controller;

import com.example.backendtask.domain.user.dto.request.RoleRequest;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
            description = "권한이 ADMIN인 유저만 실행할 수 있는 전체 회원 정보 조회",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "인가에 필요한 jWT",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIsIn..."
                    )
            })
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
            description = "권한이 ADMIN인 유저가 다른 유저에게 추가적인 권한을 부여",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "인가에 필요한 jWT",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIsIn..."
                    )
            })
    @PatchMapping("/api/v1/admin/roles")
    public ResponseEntity<User> grantRole(@RequestBody RoleRequest roleRequest) {
        User user = userService.grantAdmin(roleRequest);
        return ResponseEntity.ok().body(user);
    }
}
