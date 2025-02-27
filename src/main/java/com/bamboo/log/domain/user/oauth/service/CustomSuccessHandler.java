package com.bamboo.log.domain.user.oauth.service;

import com.bamboo.log.domain.user.jwt.service.JWTUtil;
import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import com.bamboo.log.domain.user.oauth.entity.RefreshToken;
import com.bamboo.log.domain.user.oauth.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = customUserDetails.getId();
        String name = customUserDetails.getName();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String refreshToken = jwtUtil.createJwt(userId, "refresh", name, username, role, 1800000L);
        String accessToken = jwtUtil.createJwt(userId, "access", name, username, role, 1209600000L);
        addRefreshEntity(name, username, refreshToken, 1209600000L);

        // SameSite 속성을 추가한 쿠키 설정
        response.addHeader("Set-Cookie", createCookie("refresh", refreshToken));
        response.addHeader("Set-Cookie", UnScretCreateCookie("access", accessToken));
        response.sendRedirect("http://localhost:3000/welcome");
    }

    private String createCookie(String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .maxAge(24 * 60 * 60 * 14)  // 14일
                .path("/")
                .httpOnly(true)
                .secure(true)  // HTTPS에서만 전송하도록 설정
                .sameSite("None")  // SameSite=None 설정
                .build();
        return cookie.toString();
    }

    private String UnScretCreateCookie(String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .maxAge(60 * 60*12)  // 12시간
                .path("/")
                .secure(true)  // HTTPS에서만 전송하도록 설정
                .sameSite("None")  // SameSite=None 설정
                .build();
        return cookie.toString();
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
