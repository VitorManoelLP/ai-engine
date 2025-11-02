package com.engine.ai.adapter.in.web.dto;

import java.util.Map;

public record AssistantRequest(Map<String, Object> schema, String userMessage) {
}
