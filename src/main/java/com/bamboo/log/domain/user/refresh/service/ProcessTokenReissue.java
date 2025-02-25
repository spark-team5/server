package com.bamboo.log.domain.user.refresh.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface ProcessTokenReissue {
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);
}