package com.engine.ai.adapter.in.web.controller;

import com.engine.ai.adapter.in.web.dto.SessionCreateResponse;
import com.engine.ai.adapter.in.web.dto.SessionResponse;
import com.engine.ai.adapter.in.web.mapper.SessionMapper;
import com.engine.ai.app.usecase.CreateSessionUseCase;
import com.engine.ai.app.usecase.GetSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final CreateSessionUseCase createSessionUseCase;
    private final GetSessionUseCase getSessionUseCase;

    @PostMapping
    public SessionCreateResponse createSession() {
        return SessionMapper.toSessionCreateResponse(createSessionUseCase.createSession());
    }

    @GetMapping
    public List<SessionResponse> getAllSessions() {
        return getSessionUseCase.getAllSessions()
                .stream()
                .map(SessionMapper::toSessionResponse)
                .toList();
    }

    @GetMapping("/{sessionId}")
    public SessionResponse getSessionById(@PathVariable UUID sessionId) {
        return SessionMapper.toSessionResponse(getSessionUseCase.getSession(sessionId));
    }

}
