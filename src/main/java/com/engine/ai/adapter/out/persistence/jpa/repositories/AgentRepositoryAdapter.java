package com.engine.ai.adapter.out.persistence.jpa.repositories;

import com.engine.ai.adapter.out.persistence.jpa.entity.AgentEntity;
import com.engine.ai.adapter.out.persistence.jpa.mapper.AgentMapper;
import com.engine.ai.app.port.out.AgentRepository;
import com.engine.ai.domain.entity.Agent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentRepositoryAdapter implements AgentRepository {

    private final AgentJpaRepository agentJpaRepository;

    @Override
    @Transactional
    public Agent save(Agent agent) {
        final AgentEntity entity = AgentMapper.toEntity(agent);
        final AgentEntity savedEntity = agentJpaRepository.save(entity);
        return AgentMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Optional<Agent> findById(UUID id) {
        return agentJpaRepository.findById(id)
                .map(AgentMapper::toDomain);
    }

    @Override
    @Transactional
    public List<Agent> findAll() {
        return agentJpaRepository.findAll().stream()
                .map(AgentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        agentJpaRepository.deleteById(id);
    }

}
