package com.engine.ai.domain.exception;

import java.util.UUID;

public class AgentNotFoundException extends DomainException {

    public AgentNotFoundException(UUID id) {
        super("Agent not found with id: " + id);
    }

}
