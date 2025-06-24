package com.MoleLaw_backend.service;

import com.MoleLaw_backend.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FinalAnswer {

    private final ExtractKeyword extractKeyword;
    private final LawSearchService lawSearchService;
    private final CaseSearchService caseSearchService;
    private final ObjectMapper objectMapper;

    public Map<String, Object> getFinalAnswer(String query) {
        // 1. 키워드 추출
        List<String> keywords = extractKeyword.extractKeywords(query);

        // 2. 법령 검색 결과 및 판례 검색 결과 초기화
        List<Map<String, Object>> lawResults = new ArrayList<>();
        List<PrecedentInfo> precedentResults = new ArrayList<>();

        // 3. 법령명 저장용 집합
        Set<String> uniqueLawNames = new LinkedHashSet<>();

        // 4. 키워드 기반 법령 검색
        for (String keyword : keywords) {
            try {
                String rawResult = lawSearchService.searchLawByKeyword(keyword);
                JsonNode root = objectMapper.readTree(rawResult);
                JsonNode lawArray = root.path("LawSearch").path("law");

                if (lawArray.isArray()) {
                    for (JsonNode law : lawArray) {
                        String lawName = law.path("법령명한글").asText();
                        // 5개까지만 수집
                        if (uniqueLawNames.size() < 5 && uniqueLawNames.add(lawName)) {
                            Map<String, Object> lawItem = objectMapper.convertValue(law, new TypeReference<>() {});
                            lawResults.add(lawItem);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // 검색 실패 시 무시하고 계속 진행
            }
        }

        // 5. 수집된 법령명(최대 5개)으로 판례 검색
        for (String lawName : uniqueLawNames) {
            try {
                PrecedentSearchRequest req = new PrecedentSearchRequest();
                req.setQuery(lawName); // 오직 한글 법령명만으로 검색
                List<PrecedentInfo> precedents = caseSearchService.searchCases(req);
                precedentResults.addAll(precedents);
            } catch (Exception e) {
                e.printStackTrace(); // 검색 실패 시 무시하고 계속
            }
        }

        // 6. 최종 결과 구성
        Map<String, Object> result = new HashMap<>();
        result.put("keywords", keywords);
        result.put("laws", lawResults);
        result.put("precedents", precedentResults);

        return result;
    }
}
