package com.engine.ai.adapter.in.web.mapper;

import com.engine.ai.adapter.in.web.dto.AgentRequest;
import com.engine.ai.adapter.in.web.dto.AgentResponse;
import com.engine.ai.adapter.in.web.dto.GetAgentResponse;
import com.engine.ai.app.port.in.CapabilityCommand;
import com.engine.ai.app.port.in.CreateAgentCommand;
import com.engine.ai.domain.entity.Agent;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AgentMapper {

    public CreateAgentCommand toCreateAgentCommand(AgentRequest request) {
        return new CreateAgentCommand(
                request.name(),
                request.prompt()
        );
    }

    public AgentResponse toAgentResponse(Agent agent) {
        return new AgentResponse(agent.getId());
    }

    public GetAgentResponse toGetAgentResponse(Agent agent) {
        return new GetAgentResponse(
                agent.getId(),
                agent.getName().value(),
                agent.getPrompt().value()
        );
    }

    public CapabilityCommand toCapabilityCommand(UUID id, String capability) {
        return new CapabilityCommand(id, capability);
    }

}
