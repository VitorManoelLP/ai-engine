package com.engine.ai.adapter.out.agent;

import com.engine.ai.app.port.out.AgentCall;
import com.engine.ai.domain.valueobject.Assistant;
import com.engine.ai.domain.valueobject.AssistantAnswer;
import com.engine.ai.domain.valueobject.PromptSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIAgentCall implements AgentCall  {

    private final ChatClient client;

    private final ChatMemoryRepository redisMemoryRepository;

    @Override
    public AssistantAnswer callAgent(Assistant assistant) {

        final MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisMemoryRepository)
                .build();

        final MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(memory)
                .conversationId(assistant.getConversationId().id())
                .build();

        final Map<String, Object> response = client.prompt()
                .system(assistant.getAssistantPrompt().value())
                .user(assistant.getMessage())
                .advisors(ad -> ad.param(
                        ChatMemory.CONVERSATION_ID,
                        assistant.getConversationId().id()
                ))
                .advisors(new SimpleLoggerAdvisor(), memoryAdvisor)
                .call()
                .entity(new MapStructuredConverter(assistant.getSchema().schema()));

        return new AssistantAnswer(assistant.getConversationId(), new PromptSchema(response));

    }

}
