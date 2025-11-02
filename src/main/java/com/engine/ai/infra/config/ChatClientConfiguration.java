package com.engine.ai.infra.config;

import com.engine.ai.infra.object.RedisChatMemoryRepository;
import com.engine.ai.infra.properties.RedisMemoryProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class ChatClientConfiguration {

    @Bean
    @Qualifier("postgresMemoryRepository")
    public ChatMemoryRepository jdbcChatMemoryRepository(JdbcTemplate jdbcTemplate) {
        return JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new PostgresChatMemoryRepositoryDialect())
                .build();
    }

    @Bean
    @Qualifier("redisMemoryRepository")
    public ChatMemoryRepository redisMemoryRepository(RedisMemoryProperties redisMemoryProperties,
                                                      @Lazy RedisTemplate<String, String> redisTemplate) {
        return new RedisChatMemoryRepository(
                redisTemplate,
                redisMemoryProperties,
                new ObjectMapper()
        );
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

}
