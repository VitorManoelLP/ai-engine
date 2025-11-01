package com.engine.ai.app.usecase;

import com.engine.ai.app.port.in.CapabilityCommand;

public interface CapabilityUseCase {
    void addCapabilityToAgent(CapabilityCommand capabilityCommand);
    void removeCapabilityFromAgent(CapabilityCommand capabilityCommand);
}
