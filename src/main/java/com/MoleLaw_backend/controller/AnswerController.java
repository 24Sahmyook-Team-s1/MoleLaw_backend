package com.MoleLaw_backend.controller;

import com.MoleLaw_backend.dto.QueryRequest;
import com.MoleLaw_backend.service.FinalAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final FinalAnswer finalAnswer;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getAnswer(@RequestBody QueryRequest request) {
        Map<String, Object> response = finalAnswer.getFinalAnswer(request.getQuery());
        return ResponseEntity.ok(response);
    }
}
