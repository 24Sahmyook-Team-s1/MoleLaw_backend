package com.MoleLaw_backend.controller;

import com.MoleLaw_backend.dto.QueryRequest;
import com.MoleLaw_backend.service.FinalAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {

    private final FinalAnswer finalAnswer;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getFinalAnswer(@RequestBody QueryRequest request) {
        Map<String, Object> result = finalAnswer.processQuery(request.getQuery());
        return ResponseEntity.ok(result);
    }
}
