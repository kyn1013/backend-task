package com.example.backendtask.domain.auth.service;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ClientException;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.example.backendtask.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /*
     * 회원가입
     */
    public User signup(SignupReqeust signupReqeust) {
        Boolean isMatched = userRepository.existByUsername(signupReqeust.getUsername());

        // 동일한 username이 있는지 검증
        if (isMatched) {
            throw new ClientException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String password = passwordEncoder.encode(signupReqeust.getPassword());
        UserRole userRole = UserRole.valueOf(signupReqeust.getUserRole());
        User user = new User(signupReqeust.getUsername(), password, signupReqeust.getNickName(), userRole);
        User savedUser = userRepository.insert(user);
        return savedUser;
    }
}
