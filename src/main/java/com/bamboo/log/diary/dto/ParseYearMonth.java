package com.bamboo.log.diary.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Getter
@Builder
public class ParseYearMonth {

    private YearMonth parsedYearMonth;
    private LocalDateTime startOfMonth;
    private LocalDateTime endOfMonth;

}
