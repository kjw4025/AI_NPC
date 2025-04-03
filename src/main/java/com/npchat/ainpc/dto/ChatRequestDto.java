package com.npchat.ainpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequestDto {
    private String model = "gpt-3.5-turbo";
    private List<Message> messages;

    public ChatRequestDto(String userMessage) {
        this.messages = List.of(new Message("user", userMessage));
    }

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
