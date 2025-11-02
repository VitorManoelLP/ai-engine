package com.engine.ai.adapter.out.agent;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.converter.MapOutputConverter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MapStructuredConverter extends MapOutputConverter {

    private final Map<String, Object> schema;

    @Override
    public String getFormat() {

        final String raw = """ 
            Your response should be in JSON format.
            The data structure for the JSON should match this Java class: %s
            Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
            Remove the ```json markdown surrounding the output including the trailing ```.
            The structure is as follows:
            %s
        """;

        return raw.formatted(HashMap.class.getName(), formatToJsonString(schema));
    }

    public String formatToJsonString(Map<String, Object> schema) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Map.Entry<String, Object> entry : schema.entrySet()) {
            sb.append("  \"").append(entry.getKey()).append("\": ");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\"");
            } else {
                sb.append(entry.getValue());
            }
            sb.append(",\n");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("\n}");
        return sb.toString();
    }

}
