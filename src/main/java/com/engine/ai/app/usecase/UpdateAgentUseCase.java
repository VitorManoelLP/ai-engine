package com.engine.ai.app.usecase;

import com.engine.ai.app.port.in.Partial;
import com.engine.ai.domain.entity.Agent;

import java.util.List;
import java.util.UUID;

public interface UpdateAgentUseCase {
    Agent updateAgentById(UUID id, List<Partial> partials);
}
