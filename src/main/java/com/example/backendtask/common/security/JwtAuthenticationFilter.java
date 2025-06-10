package com.example.backendtask.common.security;

import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.response.ErrorResponse;
import com.example.backendtask.domain.auth.dto.AuthUser;
import com.example.backendtask.domain.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null) {
            log.error("JWT 토큰이 없습니다.");
            setErrorResponse(httpResponse, ErrorCode.TOKEN_NOT_FOUND);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(claims);
                }
            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
                setErrorResponse(httpResponse, ErrorCode.INVALID_JWT_SIGNATURE);
                return;
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
                setErrorResponse(httpResponse, ErrorCode.EXPIRED_JWT_TOKEN);
                return;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
                setErrorResponse(httpResponse, ErrorCode.UNSUPPORTED_JWT_TOKEN);
                return;
            } catch (Exception e) {
                log.error("Internal server error, 서버 에러가 발생했습니다.", e);
                setErrorResponse(httpResponse, ErrorCode.INTERNAL_SERVER_ERROR);
                return;
            }
        }
        filterChain.doFilter(httpRequest, httpResponse);
    }

    private void setAuthentication(Claims claims) {
        String email = claims.getSubject();

        List<String> roleList = claims.get("userRole", List.class);

        List<UserRole> userRoles = roleList.stream()
                .map(UserRole::of)
                .toList();

        AuthUser authUser = new AuthUser(email, userRoles);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ErrorResponse.of(code));
        response.getWriter().write(json);
    }
}
