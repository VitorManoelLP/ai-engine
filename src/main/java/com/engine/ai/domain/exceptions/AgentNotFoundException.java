package com.engine.ai.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AgentNotFoundException extends EntityNotFoundException {
    public AgentNotFoundException(String message) {
        super(message);
    }
}
