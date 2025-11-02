package com.engine.ai.adapter.out.persistence.jpa.mapper;

import com.engine.ai.adapter.out.persistence.jpa.entity.SessionEntity;
import com.engine.ai.domain.entity.Session;
import com.engine.ai.domain.valueobject.ConversationId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionMapper {

    public static SessionEntity toEntity(Session session) {
        return SessionEntity.builder()
                .id(session.getId())
                .conversationId(session.getConversationId().id())
                .expiresAt(session.getExpiresAt())
                .createdAt(session.getCreatedAt())
                .build();
    }

    public static Session toDomain(SessionEntity entity) {
        return Session.builder()
                .id(entity.getId())
                .conversationId(new ConversationId(entity.getConversationId()))
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
