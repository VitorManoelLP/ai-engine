package com.engine.ai.app.service;

import com.engine.ai.app.port.in.AssistantAnswerCommand;
import com.engine.ai.app.port.in.AssistantAnswerSessionCommand;
import com.engine.ai.app.port.out.AgentCall;
import com.engine.ai.app.port.out.AgentRepository;
import com.engine.ai.app.port.out.SessionRepository;
import com.engine.ai.app.usecase.AssistantAnswerSessionUseCase;
import com.engine.ai.app.usecase.AssistantAnswerUseCase;
import com.engine.ai.domain.entity.Agent;
import com.engine.ai.domain.entity.Session;
import com.engine.ai.domain.enums.AssistantMemoryStrategy;
import com.engine.ai.domain.exception.SessionNotFoundException;
import com.engine.ai.domain.valueobject.Assistant;
import com.engine.ai.domain.valueobject.AssistantAnswer;
import com.engine.ai.domain.exception.AgentNotFoundException;
import com.engine.ai.domain.valueobject.ConversationId;
import com.engine.ai.domain.valueobject.PromptSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssistantService implements AssistantAnswerUseCase, AssistantAnswerSessionUseCase {

    private final AgentRepository agentRepository;
    private final AgentCall agentCall;
    private final SessionRepository sessionRepository;

    @Override
    public AssistantAnswer answer(AssistantAnswerCommand command) {

        final Agent agent = agentRepository.findById(command.agentId())
                .orElseThrow(() -> new AgentNotFoundException(command.agentId()));

        return agentCall.callAgent(Assistant.builder()
                .assistantMemoryStrategy(AssistantMemoryStrategy.REDIS)
                .assistantPrompt(agent.getPrompt())
                .schema(new PromptSchema(command.schema()))
                .message(command.userMessage())
                .conversationId(new ConversationId(command.conversationId()))
                .capabilities(agent.getCapabilities())
                .build());
    }

    @Override
    public AssistantAnswer answer(AssistantAnswerSessionCommand command) {

        final Agent agent = agentRepository.findById(command.agentId())
                .orElseThrow(() -> new AgentNotFoundException(command.agentId()));

        final Session session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.agentId()));

        return agentCall.callAgent(Assistant.builder()
                .assistantMemoryStrategy(AssistantMemoryStrategy.JDBC)
                .assistantPrompt(agent.getPrompt())
                .schema(new PromptSchema(command.schema()))
                .message(command.userMessage())
                .conversationId(session.getConversationId())
                .capabilities(agent.getCapabilities())
                .build());
    }

}
