package com.bamboo.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class MyController {
    @GetMapping("/my")
    @ResponseBody
    public String myPI() {
        log.info("myPI");
        return "my route";
    }
}