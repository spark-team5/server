package com.bamboo.log.diary.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetDiariesOfMonthResponse(
        int diariesCount,
        String date,
        List<DiaryOfMonth> diaries
) {
    @Builder
    public record DiaryOfMonth(
            LocalDateTime createdAt,
            String context,
            String summaryImage
    ) {}
}
