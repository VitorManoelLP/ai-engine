package com.engine.ai.app.port.in;

import java.util.Map;
import java.util.UUID;

public record AssistantAnswerSessionCommand(UUID agentId,
                                            Map<String, Object> schema,
                                            UUID sessionId,
                                            String userMessage) {
}
