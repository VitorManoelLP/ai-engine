package com.engine.ai.app.service;

import com.engine.ai.app.usecase.CreateAgentUseCase;
import com.engine.ai.app.usecase.DeleteAgentUseCase;
import com.engine.ai.app.usecase.GetAgentUseCase;
import com.engine.ai.app.usecase.UpdateAgentUseCase;

public interface AgentManagementUseCase extends CreateAgentUseCase, DeleteAgentUseCase, GetAgentUseCase, UpdateAgentUseCase {
}
