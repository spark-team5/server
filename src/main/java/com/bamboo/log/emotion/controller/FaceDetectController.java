package com.bamboo.log.emotion.controller;

import com.bamboo.log.emotion.dto.FaceDetectionResponse;
import com.bamboo.log.emotion.service.FaceDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/face")
@Tag(name = "Face Detection", description = "얼굴 인식 API")
public class FaceDetectController {
    private final FaceDetectionService faceDetectionService;

    @PostMapping("/detect")
    @Operation(
            summary = "얼굴 인식",
            description = "이미지를 업로드하면 얼굴 인식 여부를 반환합니다."
    )
    public ResponseEntity<FaceDetectionResponse> detectFace(@RequestParam("image") MultipartFile image) {
        FaceDetectionResponse response = faceDetectionService.detectFace(image);
        return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).body(response);
    }
}