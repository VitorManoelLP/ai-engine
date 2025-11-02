package com.engine.ai.adapter.in.web.dto;

import java.util.Map;

public record AssistantResponse(Map<String, Object> response, String conversationId) {
}
