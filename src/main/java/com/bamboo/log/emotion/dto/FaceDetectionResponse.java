package com.bamboo.log.emotion.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record FaceDetectionResponse(
        int statusCode,
        String statusMessage,
        List<BoundingBox> faceBox
) {
    public FaceDetectionResponse(HttpStatus httpStatus, List<BoundingBox> faceBox) {
        this(httpStatus.value(), httpStatus.name(), faceBox);
    }
}