package com.bamboo.log.diary.dto.response;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record CheckDiaryResponse(@NotEmpty(message = "Date shouldn't be empty") LocalDateTime date,
                                 @NotEmpty(message = "Diary Description shouldn't be empty") String diaryDescription,
                                 @NotEmpty(message = "Summary Image shouldn't be empty") byte[] summaryImage
                                 ) {
}
