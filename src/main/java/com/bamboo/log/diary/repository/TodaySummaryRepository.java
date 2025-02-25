package com.bamboo.log.diary.repository;

import com.bamboo.log.diary.domain.TodaySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodaySummaryRepository extends JpaRepository<TodaySummary, Long> {
}
