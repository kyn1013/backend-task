package com.example.backendtask.common.security;

import com.example.backendtask.domain.auth.dto.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    public JwtAuthenticationToken(AuthUser authUser) {
        // security에 권한 정보 전달
        super(authUser.getAuthorities());
        this.authUser = authUser;

        // 인증 완료 표시
        setAuthenticated(true);
    }

    // 인증 수단 반환, jwt로 이미 필터에서 검증하기 때문에 필요없음
    @Override
    public Object getCredentials() {
        return null;
    }

    // 사용자 객체 반환
    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
