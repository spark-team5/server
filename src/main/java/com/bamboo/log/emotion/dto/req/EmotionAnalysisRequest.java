package com.bamboo.log.emotion.dto.req;

import com.bamboo.log.emotion.dto.BoundingBox;
import org.springframework.web.multipart.MultipartFile;

public record EmotionAnalysisRequest(MultipartFile image, BoundingBox faceBox) {}
