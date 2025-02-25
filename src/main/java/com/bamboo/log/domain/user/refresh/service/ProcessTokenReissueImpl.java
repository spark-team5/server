package com.bamboo.log.domain.user.refresh.service;

import com.bamboo.log.domain.user.jwt.service.JWTUtil;
import com.bamboo.log.domain.user.oauth.entity.RefreshToken;
import com.bamboo.log.domain.user.oauth.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProcessTokenReissueImpl implements ProcessTokenReissue {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            log.error("리프레시 토큰이 없습니다.");
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.error("리프레시 토큰이 만료되었습니다. token: {}", refresh);
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            log.error("유효하지 않은 리프레시 토큰입니다. token: {}", refresh);
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        if (!refreshRepository.existsByToken(refresh)) {
            log.error("리프레시 토큰이 존재하지 않습니다. token: {}", refresh);
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String name = jwtUtil.getName(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", name, username , role, 1800000L);
        String newRefresh = jwtUtil.createJwt("refresh", name, username, role, 1209600000L);

        refreshRepository.deleteByToken(refresh);
        addRefreshEntity(name,username,role,1209600000L);

        log.info("새로운 액세스 토큰과 리프레시 토큰을 발급했습니다. 새로운 리프레시 토큰: {}", newRefresh);

        response.setHeader("Authorization", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        log.info("리프레시 토큰 재발급 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        log.info("쿠키 생성 시작");
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60 * 14);  // 14일 유효
        cookie.setHttpOnly(true); // 클라이언트 측에서 접근 불가
        log.info("쿠키 생성 끝");
        return cookie;
    }
    private void addRefreshEntity(String name, String username, String refresh, Long expiredMs) {
        Date CrDate = new Date(System.currentTimeMillis());
        Date ExDate = new Date(System.currentTimeMillis() + expiredMs);
        refreshRepository.save(
                RefreshToken.builder()
                        .name(name)
                        .username(username)
                        .token(refresh)
                        .createdAt(CrDate)
                        .expiresAt(ExDate)
                        .build()
        );
    }
}