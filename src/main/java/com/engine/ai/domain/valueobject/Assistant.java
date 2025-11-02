package com.engine.ai.domain.valueobject;

import com.engine.ai.domain.enums.AssistantMemoryStrategy;
import com.engine.ai.domain.enums.Capability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assistant {

    private AssistantMemoryStrategy assistantMemoryStrategy;
    private ConversationId conversationId;
    private Prompt assistantPrompt;
    private PromptSchema schema;
    private String message;
    private Set<Capability> capabilities;

    public boolean isRedisMemoryStrategy() {
        return AssistantMemoryStrategy.REDIS.equals(this.assistantMemoryStrategy);
    }

}
