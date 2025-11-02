package com.engine.ai.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.ai.chat.memory.repository.redis")
public class RedisMemoryProperties {

    private String keyPrefix;
    private Duration timeToLive;

}
