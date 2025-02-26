package com.bamboo.log.diary.repository;

import com.bamboo.log.diary.domain.TodaySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodaySummaryRepository extends JpaRepository<TodaySummary, Long> {

    Optional<TodaySummary> findByDiaryId(Long diaryId);

}
