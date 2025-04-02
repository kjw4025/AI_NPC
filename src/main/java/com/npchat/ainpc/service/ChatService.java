package com.npchat.ainpc.service;

import com.npchat.ainpc.Character.CharacterType;
import com.npchat.ainpc.dto.ChatRequestDto;
import com.npchat.ainpc.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    private final ChatHistoryService chatHistoryService;

    //원래방식
    public Mono<ChatResponseDto> getChatResponse(ChatRequestDto request) {

        log.info("📨 GPT 요청 보냄: {}", request);

        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        return client.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponseDto.class);
    }

    //단순 테스트용 -get
//    public String getChatAnswer(String message) {
//        ChatRequestDto requestDto = new ChatRequestDto(message);
//
//        try {
//            ChatResponseDto response = WebClient.builder()
//                    .baseUrl(apiUrl)
//                    .defaultHeader("Authorization", "Bearer " + apiKey)
//                    .build()
//                    .post()
//                    .bodyValue(requestDto)
//                    .retrieve()
//                    .bodyToMono(ChatResponseDto.class)
//                    .block();
//
//            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
//                return response.getChoices().get(0).getMessage().getContent();
//            } else {
//                return "응답이 없어요.";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "에러가 발생했어요: " + e.getMessage();
//        }
//    }

    //직접전달
    public String getChatAnswerWithCharacter(String userMessage, CharacterType characterType) {
        ChatRequestDto requestDto = new ChatRequestDto(characterType.getSystemPrompt(), userMessage);

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
            return "에러 발생: " + e.getMessage();
        }
    }

    //히스토리 프롬포트
    public ChatRequestDto createPromptWithHistory(String sessionId, String userMessage, String systemPrompt){
        List<ChatRequestDto.Message> messages = new ArrayList<>();
        messages.add(new ChatRequestDto.Message("system", systemPrompt)); //WorldViewPrompt
        messages.addAll(chatHistoryService.getHistory(sessionId));
        messages.add(new ChatRequestDto.Message("user", userMessage)); //temp
        return new ChatRequestDto(messages);

    }

    //히스토리 저장
    public void saveResponseToHistory(String sessionId, String userMessage, ChatResponseDto responseDto){
        chatHistoryService.addMessage(sessionId, new ChatRequestDto.Message("user", userMessage));
        if(responseDto.getChoices() != null && !responseDto.getChoices().isEmpty()) {
            ChatResponseDto.Message assistantMessage = responseDto.getChoices().get(0).getMessage();
            chatHistoryService.addMessage(sessionId, new ChatRequestDto.Message(
                assistantMessage.getRole(),
                assistantMessage.getContent()
            ));
            log.info("✅ [saveResponseToHistory] Saved history for sessionId={}", sessionId);
        }
    }



}
