package com.example.LawMate_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class test {

    @Operation(summary = "GPT 상담 요청", description = "질문을 입력하면 GPT가 법률 상담을 반환합니다.")
    @PostMapping("/ask")
    public String ask(@RequestBody String request) {
        return "예시 응답입니다";
    }
}

