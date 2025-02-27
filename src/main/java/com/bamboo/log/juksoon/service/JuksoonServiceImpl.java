package com.bamboo.log.juksoon.service;

import com.bamboo.log.juksoon.dto.JuksooniResponse;
import com.bamboo.log.juksoon.repository.JuksoonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JuksoonServiceImpl implements JuksoonService{

    private final JuksoonRepository juksoonRepository;

    @Override
    public JuksooniResponse checkJuksooniStatus(Long userId) {
        boolean hasJuksooni = juksoonRepository.existsByUserId(userId);
        return new JuksooniResponse(HttpStatus.OK, hasJuksooni);
    }
}
