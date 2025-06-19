package com.example.LawMate_backend.controller;

import com.example.LawMate_backend.ExtractKeyword;
import com.example.LawMate_backend.QueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class KeywordTestController {

    private final ExtractKeyword extractKeyword;

    @PostMapping("/keywords")
    public ResponseEntity<List<String>> getKeywords(@RequestBody QueryRequest request) {
        List<String> keywords = extractKeyword.extractKeywords(request.getQuery());
        return ResponseEntity.ok(keywords);
    }
}
