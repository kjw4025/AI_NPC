package com.npchat.ainpc.controller;

import com.npchat.ainpc.Character.CharacterType;
import com.npchat.ainpc.dto.ChatRequestDto;
import com.npchat.ainpc.dto.ChatResponseDto;
import com.npchat.ainpc.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Mono<ChatResponseDto> chat(@RequestBody ChatRequestDto request) {
        return chatService.getChatResponse(request);
    }

//    @GetMapping("/chat")
//    public String getChat(@RequestParam String message) {
//        return chatService.getChatAnswer(message);
//    }

    @PostMapping("/npc/{character}")
    public String chatWithCharacter(@PathVariable("character") String character, @RequestBody String message) {
        CharacterType characterType;
        try {
            characterType = CharacterType.valueOf(character.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "해당 캐릭터는 존재하지 않아요.";
        }

        return chatService.getChatAnswerWithCharacter(message, characterType);
    }

    @PostMapping("/chat/npc/{character}")
    public Mono<ChatResponseDto> chatWithNpc(
       @PathVariable CharacterType character,
       @RequestParam String sessionId,
       @RequestBody String message) {

        log.info("📩 [chatWithNpc] character={}, sessionId={}, message={}", character, sessionId, message); // 로그추가

        String systemPrompt = character.getSystemPrompt();
        ChatRequestDto chatRequestDto = chatService.createPromptWithHistory(sessionId, message, systemPrompt);

        return chatService.getChatResponse(chatRequestDto)
                .doOnNext(responseDto -> chatService.saveResponseToHistory(sessionId, message, responseDto));
    }

}
