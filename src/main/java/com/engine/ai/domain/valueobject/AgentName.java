package com.engine.ai.domain.valueobject;

public record AgentName(String value) {

    public AgentName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Agent name cannot be null or blank");
        }
    }

}
