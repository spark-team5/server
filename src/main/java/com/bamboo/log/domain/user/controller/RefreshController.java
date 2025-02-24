package com.bamboo.log.domain.user.controller;

import com.bamboo.log.domain.user.refresh.service.ProcessTokenReissue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 Access Token을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token"),
            @ApiResponse(responseCode = "403", description = "Refresh Token이 만료됨")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> reissue(
            HttpServletRequest request,
            HttpServletResponse response) {
        return processTokenReissue.reissue(request, response);
    }
}

