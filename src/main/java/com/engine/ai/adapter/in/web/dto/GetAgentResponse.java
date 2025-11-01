package com.engine.ai.adapter.in.web.dto;

import java.util.UUID;

public record GetAgentResponse(UUID id, String name, String prompt) {
}
