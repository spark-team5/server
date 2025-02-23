package com.bamboo.log.diary.service.diary;

import com.bamboo.log.diary.domain.Diary;
import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import com.bamboo.log.diary.dto.response.CreateDiaryResponse;
import com.bamboo.log.diary.repository.DiaryRepository;
import com.bamboo.log.diary.repository.TodaySummaryRepository;
import com.bamboo.log.diary.service.mock.UserService;
import com.bamboo.log.diary.service.summary.TodaySummaryService;
import com.bamboo.log.utils.ResponseHandler;
import com.bamboo.log.utils.dto.ResponseForm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final UserService userService;
    private final DiaryRepository diaryRepository;
    private final TodaySummaryService todaySummaryService;

    @Override
    public ResponseEntity createDiary(CreateDiaryRequest createDiaryRequest) {

        // User 객체 검증 로직
        // 해당 User 객체 찾을 수 없을 경우, 에러 반환
        if(userService.existsById(createDiaryRequest.userId())) {
            return ResponseHandler.create404Error(new ResponseForm(), new EntityNotFoundException("잘못된 유저 정보입니다."));
        }

        // 해당 시점의 localDateTime 저장
        LocalDateTime localDateTime = LocalDateTime.now();

        // alice api 연결 함수 호출

        // 데이터베이스에 저장
        Diary diary = saveDiary(createDiaryRequest, localDateTime);

        byte[] todayImage = todaySummaryService.createTodaySummaryImage(createDiaryRequest.diaryDetail());

        // todaySummaryImage 데이터베이스에 저장
        try {
            todaySummaryService.saveTodaySummaryImage(todayImage, diary.getId());
        } catch (IOException e) {
            return ResponseHandler.create500Error(new ResponseForm(), e);
        }

        // 201 code 반환
        return ResponseHandler.create201Response(new ResponseForm(), new CreateDiaryResponse(localDateTime));
    }

    private Diary saveDiary(CreateDiaryRequest createDiaryRequest, LocalDateTime localDateTime) {
        Diary diary = Diary.builder()
                .userId(createDiaryRequest.userId())
                .context(createDiaryRequest.diaryDetail())
                .createdAt(localDateTime)
                .build();

        return diaryRepository.save(diary);
    }

}
