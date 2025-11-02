package com.engine.ai.app.usecase;

import com.engine.ai.app.port.in.AssistantAnswerSessionCommand;
import com.engine.ai.domain.valueobject.AssistantAnswer;

public interface AssistantAnswerSessionUseCase {
    AssistantAnswer answer(AssistantAnswerSessionCommand command);
}
