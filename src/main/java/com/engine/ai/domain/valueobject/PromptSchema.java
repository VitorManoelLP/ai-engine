package com.engine.ai.domain.valueobject;

import java.util.Map;
import java.util.Objects;

public record PromptSchema(Map<String, Object> schema) {

    public PromptSchema {
        Objects.requireNonNull(schema, "Schema cannot be null");
    }

}
