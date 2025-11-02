package com.engine.ai.app.usecase;

import com.engine.ai.domain.entity.Session;

import java.util.List;
import java.util.UUID;

public interface GetSessionUseCase {
    Session getSession(UUID sessionId);
    List<Session> getAllSessions();
}
