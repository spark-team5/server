package com.bamboo.log.diary.dto.response;

import com.bamboo.log.diary.domain.Diary;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Base64;

@Builder
public record CheckDiaryResponse(
        @NotEmpty(message = "Date shouldn't be empty") LocalDateTime date,
        @NotEmpty(message = "Diary Description shouldn't be empty") String diaryDescription,
        String summaryImage  // 👈 Base64 문자열로 변경 (byte[] 대신 String)
) {
    public static CheckDiaryResponse from(Diary diary, byte[] imageData) {
        return new CheckDiaryResponse(
                diary.getCreatedAt(),
                diary.getContext(),
                imageData != null ? Base64.getEncoder().encodeToString(imageData) : null
        );
    }
}
