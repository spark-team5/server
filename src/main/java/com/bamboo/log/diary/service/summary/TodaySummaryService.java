package com.bamboo.log.diary.service.summary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TodaySummaryService {

    void saveTodaySummaryImage(byte[] image, Long diaryId) throws IOException;
    byte[] createTodaySummaryImage(String prompt);
}
