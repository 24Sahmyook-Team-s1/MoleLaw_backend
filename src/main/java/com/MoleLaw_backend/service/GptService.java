package com.MoleLaw_backend.service;

import com.MoleLaw_backend.dto.response.AnswerResponse;
import com.MoleLaw_backend.exception.GptApiException;
import com.MoleLaw_backend.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    public AnswerResponse generateAnswer(String userMessage) {
        String prompt = """
        당신은 법률 전문가 어시스턴트입니다. 사용자의 질문에 자연스럽고 유익한 대화를 이어가세요.
        사용자 질문: %s
        """.formatted(userMessage);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 법률 대화 어시스턴트입니다."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        try {
            String response = webClientBuilder.build()
                    .post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return new AnswerResponse(root.path("choices").get(0).path("message").path("content").asText(), "");

        } catch (WebClientResponseException e) {
            log.error("GPT 응답 실패: {}", e.getResponseBodyAsString());
            throw new GptApiException(ErrorCode.GPT_API_FAILURE, e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("GPT 일반 대화 처리 중 예외", e);
            throw new RuntimeException("GPT 일반 응답 파싱 실패", e);
        }
    }
}
