package com.example.backendtask.domain.user.service;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ClientException;
import com.example.backendtask.domain.user.dto.request.RoleRequest;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.example.backendtask.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.selectAll();
    }

    public User grantAdmin(RoleRequest roleRequest) {
        User user = userRepository.findByEmail(roleRequest.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        UserRole userRole = UserRole.of(roleRequest.getRole());
        user.addRole(userRole);
        return user;
    }
}
