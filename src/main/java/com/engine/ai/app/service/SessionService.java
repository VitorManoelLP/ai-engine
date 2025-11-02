package com.engine.ai.app.service;

import com.engine.ai.app.port.out.SessionRepository;
import com.engine.ai.app.usecase.CreateSessionUseCase;
import com.engine.ai.app.usecase.GetSessionUseCase;
import com.engine.ai.domain.entity.Session;
import com.engine.ai.domain.exception.SessionNotFoundException;
import com.engine.ai.domain.valueobject.ConversationId;
import com.engine.ai.infra.properties.SessionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService implements CreateSessionUseCase, GetSessionUseCase {

    private final SessionRepository sessionRepository;
    private final SessionProperties sessionProperties;

    @Override
    public Session createSession() {
        return sessionRepository.save(Session.builder()
                .conversationId(new ConversationId(UUID.randomUUID().toString()))
                .expiresAt(ZonedDateTime.now().plus(sessionProperties.getTtl()))
                .build());
    }

    @Override
    public Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
}
