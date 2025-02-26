package com.bamboo.log.diary.repository;

import com.bamboo.log.diary.domain.Diary;
import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUserAAndCreatedAtBetween(UserEntity user, LocalDateTime start, LocalDateTime end);

}
