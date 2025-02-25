package com.bamboo.log.emotion.service;

import com.bamboo.log.emotion.dto.FaceDetectionResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FaceDetectionService {
    FaceDetectionResponse detectFace(MultipartFile image);
}