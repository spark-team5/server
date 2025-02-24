package com.bamboo.log.domain.user.oauth.repository;

import com.bamboo.log.domain.user.oauth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByToken(String token);
    void deleteByToken(String token);
    RefreshToken findByToken(String token);
}
