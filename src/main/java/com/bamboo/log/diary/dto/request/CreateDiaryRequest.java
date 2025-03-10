package com.bamboo.log.diary.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateDiaryRequest(@NotEmpty(message = "Diary Description shouldn't be empty") String diaryDetail) {
}
