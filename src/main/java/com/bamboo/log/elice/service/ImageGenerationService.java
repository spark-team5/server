package com.bamboo.log.elice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageGenerationService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;

    public ImageGenerationService(RestTemplate restTemplate,
                                  @Value("${elice.api.token}") String apiKey,
                                  @Value("${elice.api.url.img}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    public byte[] generateImage(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = String.format("{\"prompt\":\"%s\",\"style\":\"polaroid\"}", prompt);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("요청 실패: " + response.code());
        }

        byte[] imageBytes = response.body().bytes();  // ✅ 이미지 데이터를 byte[]로 변환
        response.close();
        return imageBytes;
    }
}

