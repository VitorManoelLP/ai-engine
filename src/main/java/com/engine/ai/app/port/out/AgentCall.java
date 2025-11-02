package com.engine.ai.app.port.out;

import com.engine.ai.domain.valueobject.Assistant;
import com.engine.ai.domain.valueobject.AssistantAnswer;

public interface AgentCall {
    AssistantAnswer callAgent(Assistant assistant);
}
