package com.engine.ai.app.port.out;

import com.engine.ai.domain.entity.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findById(UUID id);
    List<Session> findAll();
}
