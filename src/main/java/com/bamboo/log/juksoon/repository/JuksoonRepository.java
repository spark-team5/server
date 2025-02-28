package com.bamboo.log.juksoon.repository;

import com.bamboo.log.juksoon.domain.Juksoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuksoonRepository extends JpaRepository<Juksoon, Long> {
        boolean existsByUserId(Long userId);

}
