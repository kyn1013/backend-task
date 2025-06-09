package com.example.backendtask.common.security;

import com.example.backendtask.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 2 * 60 * 60 * 1000L; // 2시간

    @Value("${jwt.secret.key}")
    private String secretkey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /*
     * 시크릿 키 초기화
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretkey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /*
     * access 토큰 생성
     */
    public String createAccessToken(String email, Set<UserRole> userRoles) {
        Date date = new Date();

        List<String> roles = userRoles.stream()
                .map(UserRole::getUserRole)
                .toList();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim("userRole", roles)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /*
     * 헤더에서 prefix 제거
     */
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("토큰을 찾을 수 없습니다.");
        throw new NullPointerException("토큰을 찾을 수 없습니다.");
    }

    /*
     * payload 에 있는 데이터 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
