package com.engine.ai.adapter.in.web.controller;

import com.engine.ai.adapter.in.web.dto.AssistantRequest;
import com.engine.ai.adapter.in.web.dto.AssistantResponse;
import com.engine.ai.app.port.in.AssistantAnswerCommand;
import com.engine.ai.app.port.in.AssistantAnswerSessionCommand;
import com.engine.ai.app.usecase.AssistantAnswerSessionUseCase;
import com.engine.ai.app.usecase.AssistantAnswerUseCase;
import com.engine.ai.domain.valueobject.AssistantAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assistants")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantAnswerUseCase assistantAnswerUseCase;
    private final AssistantAnswerSessionUseCase assistantAnswerSessionUseCase;

    @PostMapping("/answer/{agentId}/conversation/{conversationId}")
    public AssistantResponse answer(@PathVariable UUID agentId,
                                    @PathVariable String conversationId,
                                    @RequestBody AssistantRequest request) {

        final AssistantAnswer answer = assistantAnswerUseCase.answer(new AssistantAnswerCommand(
                agentId,
                request.schema(),
                conversationId,
                request.userMessage()
        ));

        return new AssistantResponse(answer.getOutput().schema(), answer.getConversationId().id());
    }

    @PostMapping("/answer/{agentId}/session/{sessionId}")
    public AssistantResponse answer(@PathVariable UUID agentId,
                                    @PathVariable UUID sessionId,
                                    @RequestBody AssistantRequest request) {

        final AssistantAnswer answer = assistantAnswerSessionUseCase.answer(new AssistantAnswerSessionCommand(
                agentId,
                request.schema(),
                sessionId,
                request.userMessage()
        ));

        return new AssistantResponse(answer.getOutput().schema(), answer.getConversationId().id());
    }


}
