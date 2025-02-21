package com.bamboo.log.emotion.dto;

import org.springframework.http.HttpStatus;

public record FaceDetectionResponse(
        int statusCode,
        String statusMessage,
        String message
) {
    public FaceDetectionResponse(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), httpStatus.name(), message);
    }
}