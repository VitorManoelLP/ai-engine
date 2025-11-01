package com.engine.ai.domain.valueobject;

public record Prompt(String value) {

    public Prompt {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Prompt cannot be null or blank");
        }
    }

}
