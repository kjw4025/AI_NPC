package com.npchat.ainpc.controller;

import com.npchat.ainpc.dto.ChatRequestDto;
import com.npchat.ainpc.dto.ChatResponseDto;
import com.npchat.ainpc.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Mono<ChatResponseDto> chat(@RequestBody ChatRequestDto request) {
        return chatService.getChatResponse(request);
    }

    @GetMapping("/chat")
    public String getChat(@RequestParam String message) {
        return chatService.getChatAnswer(message);
    }
}
