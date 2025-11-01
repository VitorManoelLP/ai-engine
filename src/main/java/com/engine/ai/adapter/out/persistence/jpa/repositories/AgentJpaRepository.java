package com.engine.ai.adapter.out.persistence.jpa.repositories;

import com.engine.ai.adapter.out.persistence.jpa.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentJpaRepository extends JpaRepository<AgentEntity, UUID> {
}
