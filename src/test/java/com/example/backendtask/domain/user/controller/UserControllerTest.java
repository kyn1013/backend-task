package com.example.backendtask.domain.user.controller;

import com.example.backendtask.common.security.*;
import com.example.backendtask.domain.auth.dto.AuthUser;
import com.example.backendtask.domain.user.dto.request.RoleRequest;
import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.enums.UserRole;
import com.example.backendtask.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @MockitoBean
    private JwtUtil jwtUtil;

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
                        .header("Authorization", "Bearer test-token")
                        .with(authentication(authenticationToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[0].password").value("0000"))
                .andExpect(jsonPath("$[0].nickName").value("사용자1"))
                .andExpect(jsonPath("$[0].userRoles[0]").value("ROLE_ADMIN"));
    }

    @Test
    public void 일반회원이_모든_회원정보를_조회하는데_실패하여_ACCESS_DENIED_에러가_뜬다() throws Exception {
        // given
        AuthUser authUser = new AuthUser("kyn@test.com", List.of(UserRole.ROLE_USER));
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);

        // when & then
        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authenticationToken)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.name").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }

    @Test
    public void 관리자가_일반회원에게_관리자_권한을_추가로_부여한다() throws Exception {
        // given
        AuthUser authUser = new AuthUser("kyn@test.com", List.of(UserRole.ROLE_ADMIN));
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);

        RoleRequest roleRequest = new RoleRequest();
        ReflectionTestUtils.setField(roleRequest, "email", "minji@test.com");
        ReflectionTestUtils.setField(roleRequest, "role", "ROLE_ADMIN");

        User commonUser = User.of("minji@test.com", "0000", "minji", UserRole.ROLE_USER);
        commonUser.addRole(UserRole.ROLE_ADMIN);

        BDDMockito.given(userService.grantAdmin(refEq(roleRequest))).willReturn(commonUser);

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", "Bearer test-token")
                        .with(authentication(authenticationToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("minji@test.com"))
                .andExpect(jsonPath("$.password").value("0000"))
                .andExpect(jsonPath("$.nickName").value("minji"))
                .andExpect(jsonPath("$.userRoles", hasSize(2)));
    }

    @Test
    public void 일반회원이_권한을_부여하는데_실패하여_ACCESS_DENIED_에러가_뜬다() throws Exception {
        // given
        AuthUser authUser = new AuthUser("kyn@test.com", List.of(UserRole.ROLE_USER));
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);

        RoleRequest roleRequest = new RoleRequest();
        ReflectionTestUtils.setField(roleRequest, "email", "minji@test.com");
        ReflectionTestUtils.setField(roleRequest, "role", "ROLE_ADMIN");

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", "Bearer test-token")
                        .with(authentication(authenticationToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.name").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }

}