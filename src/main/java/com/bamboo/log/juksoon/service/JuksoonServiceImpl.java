package com.bamboo.log.juksoon.service;

import com.bamboo.log.domain.user.oauth.entity.UserEntity;
import com.bamboo.log.domain.user.oauth.repository.UserRepository;
import com.bamboo.log.juksoon.domain.Juksoon;
import com.bamboo.log.juksoon.dto.JuksooniResponse;
import com.bamboo.log.juksoon.repository.JuksoonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JuksoonServiceImpl implements JuksoonService{

    private final JuksoonRepository juksoonRepository;
    private final UserRepository userRepository;

    // 죽순이 분양 로직
    @Override
    @Transactional
    public JuksooniResponse adoptJuksooni(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("유저 ID가 null입니다.");
        }

        // 이미 죽순이를 분양받았는지 확인
        if (juksoonRepository.existsByUserId(userId)) {
            return new JuksooniResponse(HttpStatus.CONFLICT, false);
        }

        // 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 죽순이 생성 및 저장
        Juksoon juksoon = Juksoon.builder()
                .user(user)
                .build();
        juksoonRepository.save(juksoon);

        return new JuksooniResponse(HttpStatus.CREATED, true);
    }

    // 죽순이 분양 여부 조회 로직
    @Override
    @Transactional(readOnly = true)
    public JuksooniResponse checkJuksooniStatus(Long userId) {
        boolean hasJuksooni = juksoonRepository.existsByUserId(userId);

        return new JuksooniResponse(HttpStatus.OK, hasJuksooni);
    }
}
