package com.engine.ai.app.usecase;

import com.engine.ai.app.port.in.CreateAgentCommand;
import com.engine.ai.domain.entity.Agent;

public interface CreateAgentUseCase {

    Agent createAgent(CreateAgentCommand command);

}
