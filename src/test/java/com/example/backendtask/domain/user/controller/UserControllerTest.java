package com.example.backendtask.domain.user.controller;

import com.example.backendtask.common.security.*;
import com.example.backendtask.domain.auth.dto.AuthUser;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.example.backendtask.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtUtil.class, CustomAccessDeniedHandler.class, JwtAuthenticationFilter.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    public void 관리자가_모든_회원정보를_조회하는데_성공한다() throws Exception {
        // given
        AuthUser authUser = new AuthUser("kyn@test.com", List.of(UserRole.ROLE_ADMIN));
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        List<User> userList = List.of(
                User.of("user1@test.com", "0000", "사용자1", UserRole.ROLE_ADMIN),
                User.of("user2@test.com", "0000", "사용자2", UserRole.ROLE_USER)
        );

        BDDMockito.given(userService.findAll()).willReturn(userList);

        // when & then
        mockMvc.perform(get("/api/v1/admin/users")
                        .with(authentication(authenticationToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[0].password").value("0000"))
                .andExpect(jsonPath("$[0].nickName").value("사용자1"))
                .andExpect(jsonPath("$[0].userRoles[0]").value("ROLE_ADMIN"));
    }

    @Test
    public void 일반회원이_모든_회원정보를_조회하는데_실패하여_401에러가_뜬다() throws Exception {
        // given
        AuthUser authUser = new AuthUser("kyn@test.com", List.of(UserRole.ROLE_USER));
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);

        // when & then
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authenticationToken)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.name").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }



}