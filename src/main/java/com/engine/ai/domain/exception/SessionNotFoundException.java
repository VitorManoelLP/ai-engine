package com.engine.ai.domain.exception;

import java.util.UUID;

public class SessionNotFoundException extends DomainException {

    public SessionNotFoundException(UUID id) {
        super("Agent not found with id: " + id);
    }

}
