package com.example.backendtask.common.security;

import com.example.backendtask.common.response.ErrorResponse;
import com.example.backendtask.domain.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtAuthenticationFilterTest {

    private JwtUtil jwtUtil;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    private static final String SECRET_KEY = "uRjbG5MzPcLvZy4J8tvSZOjgrpqETrD5sRjbG5MzPcLvZy4J8tvSZOjgrpqETrD5";

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        Field secretKeyField = JwtUtil.class.getDeclaredField("secretkey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, SECRET_KEY);

        jwtUtil.init();
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    public void 인증이_필요한_API에_JWT가_없으면_BAD_REQUEST_에러가_뜬다() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/admin/users");
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

        String responseBody = response.getContentAsString();
        ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertEquals("TOKEN_NOT_FOUND", error.getName());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("JWT 토큰이 없습니다.", error.getMessage());
    }

    @Test
    public void 유효하지_않은_JWT는_INVALID_JWT_SIGNATURE_에러가_뜬다() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/admin/users");
        request.addHeader("Authorization", "Bearer InvalidToken");
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        String responseBody = response.getContentAsString();
        ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertEquals("INVALID_JWT_SIGNATURE", error.getName());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), error.getStatusCode());
        assertEquals("유효하지 않는 JWT 서명입니다.", error.getMessage());
    }

    @Test
    public void 만료된_JWT는_EXPIRED_JWT_TOKEN_에러가_뜬다() throws ServletException, IOException {
        // given
        // 토큰의 유효시간을 만료되게 설정
        Date now = new Date();
        Date tokenTime = new Date(now.getTime() - 1000L);

        List<String> roles = List.of("ROLE_USER");
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));


        String token =  "Bearer " +
                Jwts.builder()
                        .setSubject("user1@test.com")
                        .claim("userRole", roles)
                        .setExpiration(tokenTime)
                        .setIssuedAt(now)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        request.setRequestURI("/api/v1/admin/users");
        request.addHeader("Authorization", token);
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        String responseBody = response.getContentAsString();
        ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertEquals("EXPIRED_JWT_TOKEN", error.getName());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), error.getStatusCode());
        assertEquals("만료된 JWT 토큰입니다.", error.getMessage());
    }

    @Test
    public void 지원되지_않는_JWT_토큰은_UNSUPPORTED_JWT_TOKEN_에러를_던진다() throws Exception {
        // given
        // RS256 알고리즘은 jWT에서 지원하지 않는 알고리즘
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"RS256\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"user1@test.com\"}".getBytes());
        String token = "Bearer " + header + "." + payload + ".signature";

        request.setRequestURI("/api/v1/admin/users");
        request.addHeader("Authorization", token);

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

        String responseBody = response.getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertEquals("UNSUPPORTED_JWT_TOKEN", error.getName());
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals("지원되지 않는 JWT 토큰입니다.", error.getMessage());
    }

}