package com.engine.ai.app.usecase;

import com.engine.ai.app.port.in.AssistantAnswerCommand;
import com.engine.ai.domain.valueobject.AssistantAnswer;

public interface AssistantAnswerUseCase {
    AssistantAnswer answer(AssistantAnswerCommand command);
}
