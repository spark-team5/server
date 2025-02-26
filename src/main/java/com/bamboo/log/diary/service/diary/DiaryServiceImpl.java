package com.bamboo.log.diary.service.diary;

import com.bamboo.log.diary.domain.Diary;
import com.bamboo.log.diary.domain.TodaySummary;
import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import com.bamboo.log.diary.dto.response.CheckDiaryResponse;
import com.bamboo.log.diary.dto.response.CreateDiaryResponse;
import com.bamboo.log.diary.repository.DiaryRepository;
import com.bamboo.log.diary.repository.TodaySummaryRepository;
import com.bamboo.log.domain.user.oauth.dto.CustomOAuth2User;
import com.bamboo.log.domain.user.oauth.repository.UserRepository;
import com.bamboo.log.domain.user.oauth.service.CustomOAuth2UserService;
import org.springframework.security.core.GrantedAuthority;

import com.bamboo.log.diary.service.summary.TodaySummaryService;
import com.bamboo.log.domain.user.jwt.service.UserContextUtil;
import com.bamboo.log.domain.user.oauth.dto.KakaoResponse;
import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import com.bamboo.log.utils.ResponseHandler;
import com.bamboo.log.utils.dto.ResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final UserContextUtil userContextUtil;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final TodaySummaryService todaySummaryService;
    private final TodaySummaryRepository todaySummaryRepository;

    @Override
    public ResponseEntity createDiary(CreateDiaryRequest createDiaryRequest) {
        // 해당 시점의 localDateTime 저장
        LocalDateTime localDateTime = LocalDateTime.now();

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

    @Override
    public ResponseEntity getDiaryByDate(LocalDateTime date) {
        UserEntity user = userRepository.findByUsername(userContextUtil.getUsername());

        try {
            List<Diary> diaries = diaryRepository.findByUserAAndCreatedAtBetween(user, date, date);
            Diary diaryByDate = diaries.get(0);
            Optional<TodaySummary> summaryImage = todaySummaryRepository.findByDiaryId(diaryByDate.getId());

            return ResponseHandler.create200Response(new ResponseForm(),
                    CheckDiaryResponse.builder()
                            .date(diaryByDate.getCreatedAt())
                            .diaryDescription(diaryByDate.getContext())
                            .summaryImage(summaryImage.get().getImageData()));

        } catch (Exception e) {
            return ResponseHandler.create404Error(new ResponseForm(), new IllegalArgumentException("해당 날짜에 작성된 일기가 없습니다."));
        }
    }

    private Diary saveDiary(CreateDiaryRequest createDiaryRequest, LocalDateTime localDateTime) {
        UserEntity user = userContextUtil.getUserEntity();

        Diary diary = Diary.builder()
                .user(user)
                .context(createDiaryRequest.diaryDetail())
                .createdAt(localDateTime)
                .build();

        return diaryRepository.save(diary);
    }

}
