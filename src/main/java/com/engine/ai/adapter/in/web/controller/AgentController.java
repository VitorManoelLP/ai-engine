package com.engine.ai.adapter.in.web.controller;

import com.engine.ai.adapter.in.web.dto.*;
import com.engine.ai.adapter.in.web.mapper.AgentMapper;
import com.engine.ai.app.port.in.CreateAgentCommand;
import com.engine.ai.app.port.in.Partial;
import com.engine.ai.app.usecase.*;
import com.engine.ai.domain.entity.Agent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agents")
public class AgentController {

    private final CreateAgentUseCase createAgentUseCase;
    private final DeleteAgentUseCase deleteAgentUseCase;
    private final UpdateAgentUseCase updateAgentUseCase;
    private final GetAgentUseCase getAgentUseCase;
    private final CapabilityUseCase capabilityUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgentResponse createAgent(@RequestBody AgentRequest request) {
        final CreateAgentCommand command = AgentMapper.toCreateAgentCommand(request);
        final Agent agent = createAgentUseCase.createAgent(command);
        return AgentMapper.toAgentResponse(agent);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgentNewVersionResponse updateAgent(@RequestBody List<Partial> partials, @PathVariable UUID id) {
        return new AgentNewVersionResponse(updateAgentUseCase.updateAgentById(id, partials).getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteAgentUseCase.deleteAgentById(id);
    }

    @GetMapping("/{id}")
    public Optional<GetAgentResponse> findById(@PathVariable UUID id) {
        final Optional<Agent> agent = getAgentUseCase.findById(id);
        return agent.map(AgentMapper::toGetAgentResponse);
    }

    @GetMapping
    public List<GetAgentResponse> findAll() {
        return getAgentUseCase.findAll()
                .stream()
                .map(AgentMapper::toGetAgentResponse)
                .toList();
    }

    @PutMapping("/capability/{id}")
    public void addCapability(@RequestBody String capability, @PathVariable UUID id) {
        capabilityUseCase.addCapabilityToAgent(AgentMapper.toCapabilityCommand(id, capability));
    }

    @DeleteMapping("/remove/capability/{id}")
    public void removeCapability(@RequestBody String capability, @PathVariable UUID id) {
        capabilityUseCase.removeCapabilityFromAgent(AgentMapper.toCapabilityCommand(id, capability));
    }

}
