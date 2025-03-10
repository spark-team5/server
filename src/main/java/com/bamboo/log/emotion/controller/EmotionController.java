package com.bamboo.log.emotion.controller;

import com.bamboo.log.emotion.dto.BoundingBox;
import com.bamboo.log.emotion.dto.EmotionAnalysisResponse;
import com.bamboo.log.emotion.dto.req.EmotionAnalysisRequest;
import com.bamboo.log.emotion.service.EmotionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/emotion")
@RequiredArgsConstructor
@Tag(name = "Emotion Analysis", description = "얼굴 감정(표정)분석 API")
public class EmotionController {

    private final EmotionAnalysisService emotionAnalysisService;

    @PostMapping(value = "/result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "감정 분석",
            description = "반환받은 얼굴 영역 좌표와 이미지를 분석하여 감정을 반환합니다."
    )
    public ResponseEntity<EmotionAnalysisResponse> analyzeEmotion(

            @RequestPart("image")
            @Parameter(description = "감정 분석할 얼굴 이미지", required = true)
            MultipartFile image,

            @RequestPart("faceBox")
            @Parameter(description = "얼굴 영역 좌표 (JSON 형식)", required = true)
            List<BoundingBox> faceBoxList) {

        BoundingBox faceBox = faceBoxList.get(0); // 첫 번째 얼굴만 사용
        EmotionAnalysisRequest request = new EmotionAnalysisRequest(image, faceBox);
        EmotionAnalysisResponse response = emotionAnalysisService.analyzeEmotion(request);

        HttpStatus status = HttpStatus.resolve(response.status().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }
}
