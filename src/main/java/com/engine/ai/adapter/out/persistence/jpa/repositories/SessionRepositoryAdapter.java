package com.engine.ai.adapter.out.persistence.jpa.repositories;

import com.engine.ai.adapter.out.persistence.jpa.entity.SessionEntity;
import com.engine.ai.adapter.out.persistence.jpa.mapper.SessionMapper;
import com.engine.ai.app.port.out.SessionRepository;
import com.engine.ai.domain.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionRepository {

    private final SessionJpaRepository sessionJpaRepository;

    @Override
    @Transactional
    public Session save(Session session) {
        final SessionEntity entity = SessionMapper.toEntity(session);
        final SessionEntity saved = sessionJpaRepository.save(entity);
        return SessionMapper.toDomain(saved);
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return sessionJpaRepository.findById(id)
                .map(SessionMapper::toDomain);
    }

    @Override
    public List<Session> findAll() {
        return sessionJpaRepository.findAll().stream()
                .map(SessionMapper::toDomain)
                .toList();
    }

}
