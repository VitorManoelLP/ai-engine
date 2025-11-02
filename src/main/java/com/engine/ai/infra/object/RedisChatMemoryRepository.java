package com.engine.ai.infra.object;

import com.engine.ai.infra.properties.RedisMemoryProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public final class RedisChatMemoryRepository implements ChatMemoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisChatMemoryRepository.class);

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisMemoryProperties properties;

    private final ObjectMapper objectMapper;

    @Override
    public List<String> findConversationIds() {
        return Objects.requireNonNull(redisTemplate.execute(
                (RedisCallback<List<String>>)
                        connection -> {
                            var keys = new HashSet<String>();
                            ScanOptions options =
                                    ScanOptions.scanOptions()
                                            .match(String.format("*%s*", properties.getKeyPrefix()))
                                            .count(Integer.MAX_VALUE)
                                            .build();

                            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                                while (cursor.hasNext()) {
                                    String[] key =
                                            new String(cursor.next(), StandardCharsets.UTF_8)
                                                    .split(":");
                                    if (key.length > 0) {
                                        keys.add(key[key.length - 1]);
                                    }
                                }
                            }
                            return new ArrayList<>(keys);
                        }));
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");

        String key = properties.getKeyPrefix() + conversationId;
        List<String> messageStrings = redisTemplate.opsForList().range(key, 0, -1);
        if (messageStrings == null) {
            logger.debug("No messages found for conversationId: " + conversationId);
            return List.of();
        }

        List<Message> messages = new ArrayList<>();
        for (String messageString : messageStrings) {
            try {
                JsonNode jsonNode = objectMapper.readTree(messageString);
                messages.add(getMessage(jsonNode));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing message", e);
            }
        }
        return messages;
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");

        String key = properties.getKeyPrefix() + conversationId;

        deleteByConversationId(conversationId);

        List<String> messageJsons =
                messages.stream()
                        .map(
                                message -> {
                                    try {
                                        message.getMetadata()
                                                .put("timestamp", Instant.now().toString());
                                        return objectMapper.writeValueAsString(message);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException("Error serializing message", e);
                                    }
                                })
                        .toList();

        redisTemplate.opsForList().rightPushAll(key, messageJsons);
        redisTemplate.expire(key, properties.getTimeToLive());
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        String key = properties.getKeyPrefix() + conversationId;
        redisTemplate.delete(key);
    }

    private Message getMessage(JsonNode jsonNode) {
        String type =
                Optional.ofNullable(jsonNode)
                        .map(node -> node.get("messageType"))
                        .map(JsonNode::asText)
                        .orElse(MessageType.USER.getValue());
        MessageType messageType = MessageType.valueOf(type.toUpperCase());

        String textContent =
                Optional.ofNullable(jsonNode)
                        .map(node -> node.get("text"))
                        .map(JsonNode::asText)
                        .orElseGet(
                                () ->
                                        (messageType == MessageType.SYSTEM
                                                || messageType == MessageType.USER)
                                                ? ""
                                                : null);

        Map<String, Object> metadata =
                Optional.ofNullable(jsonNode)
                        .map(node -> node.get("metadata"))
                        .map(
                                node ->
                                        objectMapper.convertValue(
                                                node, new TypeReference<Map<String, Object>>() {}))
                        .orElse(new HashMap<>());
        metadata.put("timestamp", Instant.now().toString());

        return switch (messageType) {
            case ASSISTANT -> new AssistantMessage(textContent, metadata);
            case USER -> UserMessage.builder().text(textContent).metadata(metadata).build();
            case SYSTEM -> SystemMessage.builder().text(textContent).metadata(metadata).build();
            case TOOL -> new ToolResponseMessage(List.of(), metadata);
        };
    }

}