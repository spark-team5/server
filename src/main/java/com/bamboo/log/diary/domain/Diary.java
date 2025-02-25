package com.bamboo.log.diary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diaries")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String context;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
