package com.npchat.ainpc.service;

import com.npchat.ainpc.dto.ChatRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatHistoryService {
    private final Map<String, List<ChatRequestDto.Message>> historyMap = new ConcurrentHashMap<>();

    public List<ChatRequestDto.Message> getHistory(String sessionId) {
        List<ChatRequestDto.Message> history = historyMap.getOrDefault(sessionId, new ArrayList<>());
        log.info("📜 [getHistory] sessionId={}, history size={}, messages={}", sessionId, history.size(), history);
        return history;
    }

    public void addMessage(String sessionId, ChatRequestDto.Message message) {
        historyMap.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(message);
        log.info("💾 [addMessage] sessionId={}, role={}, content={}", sessionId, message.getRole(), message.getContent());
    }

    public void clearHistory(String sessionId) {
        historyMap.remove(sessionId);
    }
}
