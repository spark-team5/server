package com.bamboo.log.diary.service.diary;

import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface DiaryService {

    ResponseEntity createDiary(CreateDiaryRequest createDiaryRequest);

    ResponseEntity getDiaryByDate(LocalDateTime date);

}
