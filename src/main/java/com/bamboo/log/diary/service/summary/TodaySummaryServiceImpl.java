package com.bamboo.log.diary.service.summary;

import com.bamboo.log.diary.domain.TodaySummary;
import com.bamboo.log.diary.repository.TodaySummaryRepository;
import com.bamboo.log.elice.service.ImageGenerationService;
import com.bamboo.log.utils.ResponseHandler;
import com.bamboo.log.utils.dto.ResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodaySummaryServiceImpl implements TodaySummaryService {

    private final TodaySummaryRepository todaySummaryRepository;
    private final ImageGenerationService imageGenerationService;

    @Override
    public void saveTodaySummaryImage(byte[] image, Long diaryId) throws IOException {
        TodaySummary todaySummary = TodaySummary.builder()
                .diaryId(diaryId)
                .imageData(image)
                .build();

        todaySummaryRepository.save(todaySummary);
    }

    @Override
    public byte[] createTodaySummaryImage(String prompt) {
        try {
            byte[] imageBytes = imageGenerationService.generateImage(prompt);

            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("이미지 생성 중에 문제가 발생했습니다.");
        }

    }

}
