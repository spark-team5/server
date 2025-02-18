package com.bamboo.log.diary.dto.response;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record CreateDiaryResponse(@NotEmpty(message = "created shouldn't be empty") LocalDateTime created) {
}
