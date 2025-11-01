package com.engine.ai.domain.entity;

import com.engine.ai.domain.enums.Capability;
import com.engine.ai.domain.valueobject.AgentName;
import com.engine.ai.domain.valueobject.Prompt;
import com.engine.ai.domain.valueobject.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    private UUID id;
    private AgentName name;
    private Prompt prompt;
    private Version version;
    private boolean isActive;
    private Set<Capability> capabilities;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private UUID parentId;

    public static Agent createNew(AgentName name, Prompt prompt, Set<Capability> capabilities) {
        return Agent.builder()
                .name(name)
                .prompt(prompt)
                .version(new Version(1L))
                .isActive(true)
                .capabilities(capabilities != null ? new HashSet<>(capabilities) : new HashSet<>())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = ZonedDateTime.now();
    }

    public void addCapability(Capability capability) {

        if (capability == null) {
            throw new IllegalArgumentException("Capability cannot be null");
        }

        if (this.capabilities == null) {
            this.capabilities = new HashSet<>();
        }

        this.capabilities.add(capability);

        this.updatedAt = ZonedDateTime.now();
    }

    public void removeCapability(Capability capability) {
        if (this.capabilities != null) {
            this.capabilities.remove(capability);
            this.updatedAt = ZonedDateTime.now();
        }
    }

    public Agent newVersion() {
        return Agent.builder()
                .name(this.name)
                .prompt(this.prompt)
                .version(this.version.increment())
                .parentId(id)
                .isActive(true)
                .capabilities(new HashSet<>(this.capabilities))
                .build();
    }

}
