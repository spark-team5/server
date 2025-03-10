package com.bamboo.log.utils.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ResponseForm {

    public ResponseForm() {
        response = new HashMap<>();
    }

    public void of(String value1, Object value2) {
        response.put(value1, value2);
    }

    private Map<String, Object> response;

}