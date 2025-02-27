package com.bamboo.log.juksoon.controller;

import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import com.bamboo.log.juksoon.dto.JuksooniResponse;
import com.bamboo.log.juksoon.service.JuksoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/juksooni")
@RequiredArgsConstructor
@Tag(name = "Juksooni", description = "죽순이 분양 관련 API")
public class JuksoonController {
    private final JuksoonService juksoonService;

    @PostMapping("/adopt")
    @Operation(summary = "죽순이 분양", description = "JWT 토큰이 필요합니다.")
    public ResponseEntity<JuksooniResponse> adoptJuksooni(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long userId = customOAuth2User.getId();
        JuksooniResponse response = juksoonService.adoptJuksooni(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/status")
    @Operation(summary = "죽순이 분양 여부 조회", description = "JWT 토큰이 필요합니다.")
    public ResponseEntity<JuksooniResponse> checkJuksooniStatus(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long userId = customOAuth2User.getId();
        JuksooniResponse response = juksoonService.checkJuksooniStatus(userId);

        return ResponseEntity.ok(response);

    }
}