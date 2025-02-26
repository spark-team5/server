package com.bamboo.log.diary.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CheckDiaryRequest(@NotEmpty(message = "Date shouldn't be empty") String date) {
}
