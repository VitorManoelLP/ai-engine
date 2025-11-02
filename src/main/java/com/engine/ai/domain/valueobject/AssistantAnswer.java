package com.engine.ai.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantAnswer {

    private ConversationId conversationId;
    private PromptSchema output;

}
