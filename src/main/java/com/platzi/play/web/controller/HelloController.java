package com.platzi.play.web.controller;

import com.platzi.play.domain.service.PlatziPlayAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final String plataform;
    private final PlatziPlayAiService aiService;

    public HelloController(@Value("${spring.application.name}")String plataform, PlatziPlayAiService aiService) {
        this.plataform = plataform;
        this.aiService = aiService;
    }

    @GetMapping("/hello")
    public String hello() {
        return this.aiService.generateGreeting(plataform);
    }



}