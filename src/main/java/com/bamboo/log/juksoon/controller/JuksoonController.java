package com.bamboo.log.juksoon.controller;

import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import com.bamboo.log.juksoon.dto.JuksooniResponse;
import com.bamboo.log.juksoon.service.JuksoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/juksooni")
@RequiredArgsConstructor
public class JuksoonController {
    private final JuksoonService juksoonService;

    @GetMapping("/status")
    public ResponseEntity<JuksooniResponse> checkJuksooniStatus(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Long userId = customOAuth2User.getId();
        JuksooniResponse response = juksoonService.checkJuksooniStatus(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}