package com.bamboo.log.utils;

import com.bamboo.log.utils.dto.DetailResponse;
import com.bamboo.log.utils.dto.ResponseForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity create500Error(ResponseForm response, Exception e) {
        response.of("result", "FAIL");
        response.of("error", DetailResponse.builder().code(500).message(e.getMessage()).build());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity create400Error(ResponseForm response, Exception e) {
        response.of("result", "FAIL");
        response.of("error", DetailResponse.builder().code(400).message(e.getMessage()).build());

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity create404Error(ResponseForm response, Exception e) {
        response.of("result", "FAIL");
        response.of("error", DetailResponse.builder().code(404).message(e.getMessage()).build());

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity create204Response(ResponseForm response, String message) {
        response.of("result", "SUCCESS");
        response.of("code", DetailResponse.builder().code(204).message(message).build());

        return new ResponseEntity(response, HttpStatus.NO_CONTENT);
    }

    public static ResponseEntity create200Response(ResponseForm response, Object object) {
        response.of("result", "SUCCESS");
        response.of("code", object);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    public static ResponseEntity create201Response(ResponseForm response, Object object) {
        response.of("result", "SUCCESS");
        response.of("code", object);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

}
