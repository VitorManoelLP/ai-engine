package com.engine.ai.adapter.in.web.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record SessionResponse(UUID id, String conversationId, ZonedDateTime expiresAt) {
}
