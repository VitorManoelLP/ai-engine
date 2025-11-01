package com.engine.ai.app.port.out;

import com.engine.ai.domain.entity.Agent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentRepository {
    Agent save(Agent agent);
    Optional<Agent> findById(UUID id);
    List<Agent> findAll();
    void delete(UUID id);
}
