package com.engine.ai.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assistant {

    private ConversationId conversationId;
    private Prompt assistantPrompt;
    private PromptSchema schema;
    private String message;

}
