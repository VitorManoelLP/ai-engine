package com.engine.ai.domain.entity;

import com.engine.ai.domain.valueobject.ConversationId;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
public class Session {

    private UUID id;
    private ConversationId conversationId;
    private ZonedDateTime createdAt;
    private ZonedDateTime expiresAt;

}
