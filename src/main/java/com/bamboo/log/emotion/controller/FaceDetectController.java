package com.bamboo.log.emotion.controller;

import com.bamboo.log.emotion.dto.FaceDetectionResponse;
import com.bamboo.log.emotion.service.FaceDetectionService;
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
@RequestMapping("/api/emotion")
public class FaceDetectController {
    private final FaceDetectionService faceDetectionService;

    @PostMapping("/detect")
    public ResponseEntity<FaceDetectionResponse> detectFace(@RequestParam("image") MultipartFile image) {
        FaceDetectionResponse response = faceDetectionService.detectFace(image);
        return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).body(response);
    }
}