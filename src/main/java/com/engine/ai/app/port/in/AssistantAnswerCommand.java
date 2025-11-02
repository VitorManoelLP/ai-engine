package com.engine.ai.app.port.in;

import java.util.Map;
import java.util.UUID;

public record AssistantAnswerCommand(UUID agentId,
                                     Map<String, Object> schema,
                                     String conversationId,
                                     String userMessage) {
}
