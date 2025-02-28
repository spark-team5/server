//package com.bamboo.log.common.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        corsRegistry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
//                .allowedHeaders("*") // 모든 헤더 허용
//                .allowCredentials(true) // 쿠키 허용
//                .exposedHeaders("Set-Cookie");
//    }
//}
