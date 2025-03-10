package com.bamboo.log.juksoon.dto;

import org.springframework.http.HttpStatus;

public record JuksooniResponse(
        HttpStatus status,
        boolean hasJuksooni
) { }
