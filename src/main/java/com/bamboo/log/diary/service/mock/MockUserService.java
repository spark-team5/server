package com.bamboo.log.diary.service.mock;

import org.springframework.stereotype.Service;

@Service
public class MockUserService implements UserService {

    @Override
    public boolean existsById(String userId) {
        return true;
    }
}
