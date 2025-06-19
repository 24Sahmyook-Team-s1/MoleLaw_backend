package com.example.LawMate_backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExtractKeyword {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;

    public List<String> extractKeywords(String userInput) {
        WebClient webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String prompt = String.format("""
            다음 문장에서 법적 판례 검색에 유용한 핵심 키워드 3~5개를 JSON 배열로 출력하세요.
            문장: "%s"
            예시 출력: ["부당해고", "근로자 권리"]
            """, userInput);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 법률 키워드 추출 도우미입니다."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.3
        );

        Map<String, Object> response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        try {
            String content = (String) ((Map<String, Object>) ((Map<String, Object>) ((List<?>) response.get("choices")).get(0)).get("message")).get("content");

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

