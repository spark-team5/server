package com.bamboo.log.diary.dto.response;

import jakarta.validation.constraints.NotEmpty;

public record CheckDiaryResponse(@NotEmpty(message = "Date shouldn't be empty") String date,
                                 @NotEmpty(message = "Diary Description shouldn't be empty") String diaryDescription,
                                 @NotEmpty(message = "Summary Image shouldn't be empty") byte[] summaryImage
                                 ) {
}
