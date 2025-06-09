package com.example.backendtask.domain.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleRequest {

    private String email;
    private String role;
}
