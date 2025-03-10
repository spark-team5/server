package com.bamboo.log.emotion.service.impl;

import com.bamboo.log.emotion.domain.EmotionType;
import com.bamboo.log.emotion.dto.BoundingBox;
import com.bamboo.log.emotion.dto.EmotionAnalysisResponse;
import com.bamboo.log.emotion.dto.req.EmotionAnalysisRequest;
import com.bamboo.log.emotion.service.EmotionAnalysisService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmotionAnalysisServiceImpl implements EmotionAnalysisService {
    @Value("${emotion.api.url}")
    private String emotionApiUrl;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public EmotionAnalysisResponse analyzeEmotion(EmotionAnalysisRequest request) {
        MultipartFile image = request.image();
        BoundingBox faceBox = request.faceBox();

        log.info("Received EmotionAnalysisRequest: {}", request);
        log.info("Received BoundingBox: {}", (faceBox != null) ? faceBox.toString() : "NULL");

        if (faceBox == null) {
            log.warn("얼굴 영역이 감지되지 않음. 감정 분석 불가능");
            return new EmotionAnalysisResponse(HttpStatus.OK, EmotionType.NONE);
        }

        EmotionAnalysisResponse response = callFastAPI(image, request);

        log.info("Received response: {}", response);
        return response;
    }

    private EmotionAnalysisResponse callFastAPI(MultipartFile image, EmotionAnalysisRequest request) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", image.getOriginalFilename(),
                            RequestBody.create(image.getBytes(), MediaType.parse(image.getContentType())))
                    .addFormDataPart("x1", String.valueOf(request.faceBox().x1()))
                    .addFormDataPart("y1", String.valueOf(request.faceBox().y1()))
                    .addFormDataPart("x2", String.valueOf(request.faceBox().x2()))
                    .addFormDataPart("y2", String.valueOf(request.faceBox().y2()))
                    .build();

            Request fastApiRequest = new Request.Builder()
                    .url(emotionApiUrl)
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .build();

            try (Response response = client.newCall(fastApiRequest).execute()) {
                if (!response.isSuccessful()) {
                    return new EmotionAnalysisResponse(HttpStatus.valueOf(response.code()), EmotionType.NONE);
                }

                String responseBody = response.body().string();
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
                String emotionString = jsonNode.get(0).get("emotion").asText();
                EmotionType emotionType = EmotionType.valueOf(emotionString.toUpperCase());

                log.info("Received response about api: {}", response);
                return new EmotionAnalysisResponse(HttpStatus.OK, emotionType);
            }
        } catch (IOException e) {
            log.error("FASTAPI 호출 중 오류 발생", e);
            return new EmotionAnalysisResponse(HttpStatus.INTERNAL_SERVER_ERROR, EmotionType.NONE);
        }
    }
}