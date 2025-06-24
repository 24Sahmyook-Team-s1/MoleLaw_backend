package com.MoleLaw_backend.service;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> processQuery(String query) {
        List<String> keywords = extractKeyword.extractKeywords(query);
        List<JsonNode> lawResults = new ArrayList<>();

        for (String keyword : keywords) {
            try {
                String response = lawSearchService.searchLawByKeyword(keyword);
                JsonNode root = objectMapper.readTree(response);

                if (root.has("LawSearch") && root.get("LawSearch").has("law")) {
                    JsonNode laws = root.get("LawSearch").get("law");
                    if (laws.isArray()) {
                        laws.forEach(lawResults::add);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // continue on error
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("keywords", keywords);
        result.put("laws", lawResults);

        return result;
    }
}
