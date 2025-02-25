package com.bamboo.log.diary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "today_summaries")
public class TodaySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long diaryId;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imageData;

}

