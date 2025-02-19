package com.bamboo.log.domain.user.controller;

import com.bamboo.log.domain.user.refresh.service.ProcessTokenReissue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final ProcessTokenReissue processTokenReissue;


    @PostMapping("/refresh")
    public ResponseEntity<?> reissue(
            HttpServletRequest request,
            HttpServletResponse response) {
        return processTokenReissue.reissue(request, response);
    }
}

