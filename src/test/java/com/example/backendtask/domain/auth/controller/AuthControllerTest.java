package com.example.backendtask.domain.auth.controller;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ClientException;
import com.example.backendtask.common.security.CustomAccessDeniedHandler;
import com.example.backendtask.common.security.JwtUtil;
import com.example.backendtask.common.security.SecurityConfig;
import com.example.backendtask.domain.auth.dto.request.SignupReqeust;
import com.example.backendtask.domain.auth.service.AuthService;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtUtil.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    public void 회원가입이_성공적으로_진행된다() throws Exception {
        // given
        SignupReqeust signupReqeust = new SignupReqeust();
        ReflectionTestUtils.setField(signupReqeust, "email", "kyn@test.com");
        ReflectionTestUtils.setField(signupReqeust, "password", "0000");
        ReflectionTestUtils.setField(signupReqeust, "nickName", "yena");
        ReflectionTestUtils.setField(signupReqeust, "userRole", "ROLE_USER");

        String password = passwordEncoder.encode("0000");
        UserRole userRole = UserRole.valueOf("ROLE_USER");
        User signupResponse = User.of("kyn@test.com", password, "yena", userRole);

        BDDMockito.given(authService.signup(any(SignupReqeust.class))).willReturn(signupResponse);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupReqeust)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kyn@test.com"))
                .andExpect(jsonPath("$.password").value(password))
                .andExpect(jsonPath("$.nickName").value("yena"))
                .andExpect(jsonPath("$.userRoles[0]").value("ROLE_USER"));
    }

    @Test
    public void 중복된_이메일로_인해서_회원가입이_실패한다() throws Exception {
        // given
        SignupReqeust signupReqeust = new SignupReqeust();
        ReflectionTestUtils.setField(signupReqeust, "email", "kyn@test.com");
        ReflectionTestUtils.setField(signupReqeust, "password", "0000");
        ReflectionTestUtils.setField(signupReqeust, "nickName", "yena");
        ReflectionTestUtils.setField(signupReqeust, "userRole", "ROLE_USER");

        // when
        BDDMockito.when(authService.signup(any(SignupReqeust.class)))
                .thenThrow(new ClientException(ErrorCode.USER_ALREADY_EXISTS));

        // then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReqeust)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }


}