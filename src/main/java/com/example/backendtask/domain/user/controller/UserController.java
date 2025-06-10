package com.example.backendtask.domain.user.controller;

import com.example.backendtask.domain.user.dto.request.RoleRequest;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
     * 모든 회원 정보 조회 (관리자만 가능)
     */
    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    /*
     * 일반 회원에게 권한 부여 (관리자만 가능)
     */
    @PatchMapping("/api/v1/admin/roles")
    public ResponseEntity<User> grantRole(@RequestBody RoleRequest roleRequest) {
        User user = userService.grantAdmin(roleRequest);
        return ResponseEntity.ok().body(user);
    }
}
