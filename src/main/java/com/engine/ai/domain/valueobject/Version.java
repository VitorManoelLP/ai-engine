package com.engine.ai.domain.valueobject;

public record Version(Long value) {

    public Version {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Version cannot be null or negative");
        }
    }

    public Version increment() {
        return new Version(this.value + 1);
    }

}
