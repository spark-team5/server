package com.bamboo.log.diary.service.diary;

import com.bamboo.log.diary.domain.Diary;
import com.bamboo.log.diary.dto.ParseYearMonth;
import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import com.bamboo.log.diary.dto.response.CreateDiaryResponse;
import com.bamboo.log.diary.dto.response.GetDiariesOfMonthResponse;
import com.bamboo.log.diary.dto.response.GetDiariesOfMonthResponse.DiaryOfMonth;
import com.bamboo.log.diary.repository.DiaryRepository;
import com.bamboo.log.diary.repository.TodaySummaryRepository;
import com.bamboo.log.domain.user.oauth.repository.UserRepository;

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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        } catch (RuntimeException e) {
            return ResponseHandler.create404Error(new ResponseForm(), e);
        } catch (IOException e) {
            return ResponseHandler.create500Error(new ResponseForm(), e);
        }

        // 201 code 반환
        return ResponseHandler.create201Response(new ResponseForm(), new CreateDiaryResponse(localDateTime));
    }

    @Override
    public ResponseEntity getDiariesByMonth(String date) {
        ParseYearMonth parseYearMonth = getParsedDate(date);
        UserEntity user = userRepository.findByUsername(userContextUtil.getUsername());

        try {
            List<Diary> diaries =
                    diaryRepository.findByUserAndCreatedAtBetween(user,
                            parseYearMonth.getStartOfMonth(), parseYearMonth.getEndOfMonth());

            List<Long> diaryIds = diaries.stream().map(Diary::getId).toList();
            Map<Long, byte[]> summaryImageMap = todaySummaryRepository.findByDiaryIdIn(diaryIds)
                    .stream()
                    .collect(Collectors.toMap(summary -> summary.getDiaryId(), summary -> summary.getImageData()));

            List<DiaryOfMonth> diaryOfMonthList = diaries.stream()
                    .map(diary -> new DiaryOfMonth(
                            diary.getCreatedAt(),
                            diary.getContext(),
                            summaryImageMap.getOrDefault(diary.getId(), null)
                    ))
                    .toList();

            return ResponseHandler.create200Response(new ResponseForm(),
                    GetDiariesOfMonthResponse.builder()
                            .diariesCount(diaryOfMonthList.size())
                            .date(date)
                            .diaries(diaryOfMonthList)
                            .build());
        } catch (RuntimeException e) {
            return ResponseHandler.create404Error(new ResponseForm(), e);
        } catch (Exception e) {
            return ResponseHandler.create500Error(new ResponseForm(), e);
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

    private ParseYearMonth getParsedDate(String date) {
        YearMonth parsedYearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDateTime startOfMonth = parsedYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = parsedYearMonth.atEndOfMonth().atTime(23, 59, 59);

        return ParseYearMonth.builder()
                .parsedYearMonth(parsedYearMonth)
                .startOfMonth(startOfMonth)
                .endOfMonth(endOfMonth)
                .build();
    }

}
