package com.bamboo.log.emotion.service.impl;

import com.bamboo.log.emotion.dto.BoundingBox;
import com.bamboo.log.emotion.dto.FaceDetectionResponse;
import com.bamboo.log.emotion.service.FaceDetectionService;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaceDetectionServiceImpl implements FaceDetectionService {

    @Value("${elice.api.url.face}")
    private String faceApiUrl;

    @Value("${elice.api.token}")
    private String faceToken;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public FaceDetectionResponse detectFace(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            log.error("파일이 전달되지 않음.");
            return new FaceDetectionResponse(HttpStatus.BAD_REQUEST, List.of());
        }
        log.info("파일 이름: {}", image.getOriginalFilename());
        log.info("파일 크기: {} bytes", image.getSize());

        try {
            String contentType = image.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream"; // 기본값 설정
            }

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", image.getOriginalFilename(),
                            RequestBody.create(image.getBytes(), MediaType.parse(contentType)))
                    .build();

            // API 요청 생성 (토큰 포함)
            Request request = new Request.Builder()
                    .url(faceApiUrl)
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + faceToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                log.info("API 응답 코드: {}", response.code());
                log.info("API 응답 메시지: {}", response.message());

                if (!response.isSuccessful()) {
                    return new FaceDetectionResponse(HttpStatus.valueOf(response.code()),  List.of());
                }

                String responseBody = response.body().string();
                log.info("API 응답 본문: {}", responseBody);

                // JSON 파싱
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
                JsonNode results = jsonNode.get("results");

                // 얼굴 인식 여부 확인
                List<BoundingBox> boxList = new ArrayList<>();
                if (results != null && results.isArray()) {
                    for (JsonNode result : results) {
                        if ("face".equals(result.get("name").asText())) {
                            JsonNode boxNode = result.get("box");
                            if (boxNode != null) {
                                boxList.add(new BoundingBox(
                                        boxNode.get("x1").asDouble(),
                                        boxNode.get("y1").asDouble(),
                                        boxNode.get("x2").asDouble(),
                                        boxNode.get("y2").asDouble()
                                ));
                            }
                        }
                    }
                }
                return new FaceDetectionResponse(HttpStatus.OK, boxList);
            }
        } catch (IOException e) {
            log.error("API 요청 중 예외 발생", e);
            return new FaceDetectionResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of());
        }
    }

}