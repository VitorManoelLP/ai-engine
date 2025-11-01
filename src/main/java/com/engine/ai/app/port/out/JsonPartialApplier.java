package com.engine.ai.app.port.out;

import com.engine.ai.app.port.in.Partial;

import java.util.List;

public interface JsonPartialApplier {
    <T> T applyJsonPatch(T target, List<Partial> partials);
}
