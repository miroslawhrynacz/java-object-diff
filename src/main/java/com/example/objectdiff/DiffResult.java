package com.example.objectdiff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiffResult {
    private final List<Change> changes;

    public DiffResult(List<Change> changes) {
        this.changes = changes;
    }

    public List<Change> getChanges() {
        return Collections.unmodifiableList(changes);
    }

    public boolean isEmpty() {
        return changes.isEmpty();
    }

    public String toSummary() {
        if (changes.isEmpty()) return "No changes";
        return changes.stream()
            .map(Change::toString)
            .collect(Collectors.joining("; "));
    }

    public Patch toPatch() {
        return new Patch(changes);
    }
}