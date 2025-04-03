package com.npchat.ainpc.service;

import com.npchat.ainpc.dto.ChatRequestDto;
import com.npchat.ainpc.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    //원래방식
    public Mono<ChatResponseDto> getChatResponse(ChatRequestDto request) {
        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        return client.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponseDto.class);
    }

    //단순 테스트용
    public String getChatAnswer(String message) {
        ChatRequestDto requestDto = new ChatRequestDto(message);

        try {
            ChatResponseDto response = WebClient.builder()
                    .baseUrl(apiUrl)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .build()
                    .post()
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(ChatResponseDto.class)
                    .block();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            } else {
                return "응답이 없어요.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "에러가 발생했어요: " + e.getMessage();
        }
    }
}
