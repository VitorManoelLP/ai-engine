package com.engine.ai.adapter.in.web.mapper;

import com.engine.ai.adapter.in.web.dto.SessionCreateResponse;
import com.engine.ai.adapter.in.web.dto.SessionResponse;
import com.engine.ai.domain.entity.Session;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionMapper {

    public SessionCreateResponse toSessionCreateResponse(Session session) {
        return new SessionCreateResponse(session.getId());
    }

    public SessionResponse toSessionResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getConversationId().id(),
                session.getExpiresAt()
        );
    }


}
