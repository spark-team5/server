package com.bamboo.log.diary.service.diary;

import com.bamboo.log.diary.domain.Diary;
import com.bamboo.log.diary.domain.TodaySummary;
import com.bamboo.log.diary.dto.ParseYearMonth;
import com.bamboo.log.diary.dto.request.CreateDiaryRequest;
import com.bamboo.log.diary.dto.response.CheckDiaryResponse;
import com.bamboo.log.diary.dto.response.CreateDiaryResponse;
import com.bamboo.log.diary.dto.response.GetDiariesOfMonthResponse;
import com.bamboo.log.diary.dto.response.GetDiariesOfMonthResponse.DiaryOfMonth;
import com.bamboo.log.diary.repository.DiaryRepository;
import com.bamboo.log.diary.repository.TodaySummaryRepository;
import com.bamboo.log.diary.service.summary.TodaySummaryService;
import com.bamboo.log.domain.user.jwt.service.UserContextUtil;
import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import com.bamboo.log.domain.user.oauth.repository.UserRepository;
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
import java.util.Optional;
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
        LocalDateTime localDateTime = LocalDateTime.now();
        Diary diary = saveDiary(createDiaryRequest, localDateTime);
        byte[] todayImage = todaySummaryService.createTodaySummaryImage(createDiaryRequest.diaryDetail());

        try {
            todaySummaryService.saveTodaySummaryImage(todayImage, diary.getId());
        } catch (RuntimeException e) {
            return ResponseHandler.create404Error(new ResponseForm(), e);
        } catch (IOException e) {
            return ResponseHandler.create500Error(new ResponseForm(), e);
        }

        return ResponseHandler.create201Response(new ResponseForm(), new CreateDiaryResponse(localDateTime));
    }

    @Override
    public ResponseEntity getDiariesByMonth(String date) {
        ParseYearMonth parseYearMonth = getParsedDate(date);
        UserEntity user = userRepository.findByUsername(userContextUtil.getUsername());

        try {
            List<Diary> diaries = diaryRepository.findByUserAAndCreatedAtBetween(user,
                    parseYearMonth.getStartOfMonth(), parseYearMonth.getEndOfMonth());

            List<Long> diaryIds = diaries.stream().map(Diary::getId).toList();
            Map<Long, byte[]> summaryImageMap = todaySummaryRepository.findByDiaryIdIn(diaryIds)
                    .stream()
                    .collect(Collectors.toMap(TodaySummary::getDiaryId, TodaySummary::getImageData));

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

    @Override
    public ResponseEntity getDiaryByDate(LocalDateTime date) {
        UserEntity user = userRepository.findByUsername(userContextUtil.getUsername());

        try {
            List<Diary> diaries = diaryRepository.findByUserAAndCreatedAtBetween(user, date, date);
            if (diaries.isEmpty()) {
                throw new RuntimeException("해당 날짜에 작성된 일기가 없습니다.");
            }

            Diary diaryByDate = diaries.get(0);
            Optional<TodaySummary> summaryImage = todaySummaryRepository.findByDiaryId(diaryByDate.getId());

            return ResponseHandler.create200Response(new ResponseForm(),
                    CheckDiaryResponse.builder()
                            .date(diaryByDate.getCreatedAt())
                            .diaryDescription(diaryByDate.getContext())
                            .summaryImage(summaryImage.map(TodaySummary::getImageData).orElse(null))
                            .build());

        } catch (RuntimeException e) {
            return ResponseHandler.create404Error(new ResponseForm(), new IllegalArgumentException("해당 날짜에 작성된 일기가 없습니다."));
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
