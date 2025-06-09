package com.example.backendtask.domain.auth.service;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ClientException;
import com.example.backendtask.common.security.JwtUtil;
import com.example.backendtask.domain.auth.dto.request.SigninRequest;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.auth.dto.response.SigninResponse;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.example.backendtask.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /*
     * 회원가입
     */
    public User signup(SignupReqeust signupReqeust) {
        Boolean isMatched = userRepository.existByUsername(signupReqeust.getEmail());

        // 동일한 username이 있는지 검증
        if (isMatched) {
            throw new ClientException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String password = passwordEncoder.encode(signupReqeust.getPassword());
        UserRole userRole = UserRole.valueOf(signupReqeust.getUserRole());
        User user = User.of(signupReqeust.getEmail(), password, signupReqeust.getNickName(), userRole);
        User savedUser = userRepository.insert(user);
        return savedUser;
    }

    /*
     * 로그인
     */
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 검증
        Boolean isMatched = passwordEncoder.matches(signinRequest.getPassword(), user.getPassword());
        if (!isMatched) {
            throw new ClientException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getUserRoles());
        return SigninResponse.of(accessToken);
    }
}
