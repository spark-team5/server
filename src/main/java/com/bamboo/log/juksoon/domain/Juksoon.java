package com.bamboo.log.juksoon.domain;

import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "juksooni")
@Builder
public class Juksoon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    public static Juksoon create(UserEntity user) {
        return Juksoon.builder().user(user).build();
    }
}
