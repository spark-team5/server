package com.bamboo.log.juksoon.service;

import com.bamboo.log.juksoon.dto.JuksooniResponse;

public interface JuksoonService {
    JuksooniResponse checkJuksooniStatus(Long userId);
}
