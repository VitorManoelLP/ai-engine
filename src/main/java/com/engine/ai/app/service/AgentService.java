package com.engine.ai.app.service;

import com.engine.ai.app.port.in.CapabilityCommand;
import com.engine.ai.app.port.in.Partial;
import com.engine.ai.app.port.in.CreateAgentCommand;
import com.engine.ai.app.port.out.AgentRepository;
import com.engine.ai.app.port.out.JsonPartialApplier;
import com.engine.ai.app.usecase.CapabilityUseCase;
import com.engine.ai.domain.entity.Agent;
import com.engine.ai.domain.enums.Capability;
import com.engine.ai.domain.exception.AgentNotFoundException;
import com.engine.ai.domain.valueobject.AgentName;
import com.engine.ai.domain.valueobject.Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AgentService implements AgentManagementUseCase, CapabilityUseCase {

    private final AgentRepository agentRepository;
    private final JsonPartialApplier jsonPartialApplier;

    @Override
    @Transactional
    public Agent createAgent(CreateAgentCommand command) {
        Agent newAgent = Agent.createNew(
                new AgentName(command.name()),
                new Prompt(command.prompt()),
                Set.of()
        );
        return agentRepository.save(newAgent);
    }

    @Override
    @Transactional
    public void deleteAgentById(UUID agentId) {
        agentRepository.delete(agentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agent> findById(UUID agentId) {
        return agentRepository.findById(agentId);
    }

    @Override
    @Transactional
    public void addCapabilityToAgent(CapabilityCommand capabilityCommand) {
        agentRepository.findById(capabilityCommand.id())
                .ifPresent(agent -> {
                    agent.addCapability(Capability.valueOf(capabilityCommand.capability()));
                    agentRepository.save(agent);
                });
    }

    @Override
    @Transactional
    public void removeCapabilityFromAgent(CapabilityCommand capabilityCommand) {
        agentRepository.findById(capabilityCommand.id())
                .ifPresent(agent -> {
                    agent.removeCapability(Capability.valueOf(capabilityCommand.capability()));
                    agentRepository.save(agent);
                });
    }

    @Override
    @Transactional
    public Agent updateAgentById(UUID id, List<Partial> partials) {

        final Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new AgentNotFoundException(id));

        final Agent agentUpdated = jsonPartialApplier.applyJsonPatch(agent, partials).newVersion();

        agent.deactivate();

        agentRepository.save(agent);

        return agentRepository.save(agentUpdated);
    }

}
