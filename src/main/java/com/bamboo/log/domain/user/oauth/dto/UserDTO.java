package com.bamboo.log.domain.user.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserDTO {
    private String name;
    private String username;
    private String role;
}