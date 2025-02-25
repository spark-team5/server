package com.bamboo.log.domain.user.oauth.repository;

import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}