package com.engine.ai.adapter.out.serializer;

import com.engine.ai.app.port.in.Partial;
import com.engine.ai.app.port.out.JsonPartialApplier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JacksonJsonPartialApplier implements JsonPartialApplier {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T applyJsonPatch(T target, List<Partial> partials) {

        final JsonNode jsonNode = objectMapper.valueToTree(target);

        partials.forEach(partial -> {

            if (jsonNode.has(partial.field())) {
                ((ObjectNode) jsonNode).putPOJO(partial.field(), partial.value());
            }

        });

        return objectMapper.readValue(jsonNode.toString(), (Class<T>) target.getClass());
    }

}
