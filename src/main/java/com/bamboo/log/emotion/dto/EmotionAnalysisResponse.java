package com.bamboo.log.emotion.dto;

import com.bamboo.log.emotion.domain.EmotionType;
import org.springframework.http.HttpStatus;

public record EmotionAnalysisResponse (HttpStatus status, EmotionType emotion) {}
