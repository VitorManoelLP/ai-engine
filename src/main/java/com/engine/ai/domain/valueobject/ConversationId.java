package com.engine.ai.domain.valueobject;

public record ConversationId(String id) {

    public ConversationId(String id) {
        if (id == null || id.isBlank()) {
            this.id = java.util.UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

}
