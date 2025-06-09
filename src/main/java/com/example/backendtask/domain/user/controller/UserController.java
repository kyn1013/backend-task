package com.example.backendtask.domain.user.controller;

import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
     * 모든 회원 정보 조회 (관리자만 가능)
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }
}
