package com.bamboo.log.emotion.service;

import com.bamboo.log.emotion.dto.EmotionAnalysisResponse;
import com.bamboo.log.emotion.dto.req.EmotionAnalysisRequest;

public interface EmotionAnalysisService {
    EmotionAnalysisResponse analyzeEmotion(EmotionAnalysisRequest request);
}