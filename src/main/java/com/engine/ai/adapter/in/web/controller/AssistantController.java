package com.engine.ai.adapter.in.web.controller;

import com.engine.ai.adapter.in.web.dto.AssistantRequest;
import com.engine.ai.adapter.in.web.dto.AssistantResponse;
import com.engine.ai.app.port.in.AssistantAnswerCommand;
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

    @PostMapping("/answer/{agentId}")
    public AssistantResponse answer(@PathVariable UUID agentId, @RequestBody AssistantRequest request) {

        final AssistantAnswer answer = assistantAnswerUseCase.answer(new AssistantAnswerCommand(
                agentId,
                request.schema(),
                request.conversationId(),
                request.userMessage()
        ));

        return new AssistantResponse(answer.getOutput().schema(), answer.getConversationId().id());
    }


}
