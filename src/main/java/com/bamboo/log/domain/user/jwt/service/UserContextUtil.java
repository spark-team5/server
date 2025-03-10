package com.bamboo.log.domain.user.jwt.service;

import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import com.bamboo.log.domain.user.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserContextUtil {

    private final UserRepository userRepository;

    public Optional<CustomOAuth2User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User)) {
            return Optional.empty();
        }
        return Optional.of((CustomOAuth2User) authentication.getPrincipal());
    }

    public String getUsername() {
        return getCurrentUser()
                .map(CustomOAuth2User::getUsername)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }

    public String getName() {
        return getCurrentUser()
                .map(CustomOAuth2User::getName)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }

    public UserEntity getUserEntity() {
        String username = getUsername();

        return userRepository.findByUsername(username);
    }

}