package com.bamboo.log.diary.api;

import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import com.bamboo.log.diary.service.diary.DiaryService;
import com.bamboo.log.utils.ResponseHandler;
import com.bamboo.log.utils.dto.ResponseForm;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Tag(name = "Diary API", description = "일기 관련 API")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 생성")
    @PostMapping("/create")
    public ResponseEntity createDiary(@RequestBody CreateDiaryRequest createDiaryRequest) {
        return diaryService.createDiary(createDiaryRequest);
    }

    @Operation(summary = "월별 일기 조회")
    @GetMapping("/month")
    public ResponseEntity getDiariesByMonth(@RequestParam String date) {
        return diaryService.getDiariesByMonth(date);
    }

    @Operation(summary = "날짜별 일기 조회")
    @GetMapping("/date")
    public ResponseEntity getDiaryByDate(@RequestParam LocalDateTime date) {
        return diaryService.getDiaryByDate(date);
    }

}
