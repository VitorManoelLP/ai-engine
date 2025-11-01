package com.engine.ai.adapter.out.persistence.jpa.mapper;

import com.engine.ai.adapter.out.persistence.jpa.entity.AgentEntity;
import com.engine.ai.domain.entity.Agent;
import com.engine.ai.domain.enums.Capability;
import com.engine.ai.domain.valueobject.AgentName;
import com.engine.ai.domain.valueobject.Prompt;
import com.engine.ai.domain.valueobject.Version;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AgentMapper {

    public static AgentEntity toEntity(Agent domain) {
        if (domain == null) {
            return null;
        }

        return AgentEntity.builder()
                .id(domain.getId())
                .name(domain.getName().value())
                .prompt(domain.getPrompt().value())
                .version(domain.getVersion().value())
                .isActive(domain.isActive())
                .parentId(domain.getParentId())
                .capabilities(mapCapabilitiesToStrings(domain.getCapabilities()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public static Agent toDomain(AgentEntity entity) {
        if (entity == null) {
            return null;
        }

        return Agent.builder()
                .id(entity.getId())
                .name(new AgentName(entity.getName()))
                .prompt(new Prompt(entity.getPrompt()))
                .version(new Version(entity.getVersion()))
                .isActive(entity.isActive())
                .capabilities(mapStringsToCapabilities(entity.getCapabilities()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private static Set<String> mapCapabilitiesToStrings(Set<Capability> capabilities) {
        if (capabilities == null) {
            return Set.of();
        }
        return capabilities.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    private static Set<Capability> mapStringsToCapabilities(@NotNull Set<String> capabilities) {
        if (capabilities == null) {
            return Set.of();
        }
        return capabilities.stream()
                .map(Capability::valueOf)
                .collect(Collectors.toSet());
    }

}
