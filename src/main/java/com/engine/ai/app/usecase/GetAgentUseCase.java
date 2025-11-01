package com.engine.ai.app.usecase;

import com.engine.ai.domain.entity.Agent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetAgentUseCase {
    List<Agent> findAll();
    Optional<Agent> findById(UUID agentId);
}
