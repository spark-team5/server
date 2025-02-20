package com.bamboo.log;

import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GetTestController {

    @GetMapping("/jwt")
    public ResponseEntity<Map<String, Object>> getJwtInfo(@AuthenticationPrincipal CustomOAuth2User userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        Map<String, Object> userInfo = Map.of(
                "username", userDetails.getUsername(),
                "name", userDetails.getName(),  // CustomUserDetails에 name 필드가 있다고 가정
                "role", userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(Object::toString)
                        .orElse("ROLE_USER")
        );
        System.out.println("userInfo = " + userInfo);
        return ResponseEntity.ok(userInfo);
    }
}
