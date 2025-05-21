package com.example.objectdiff;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patch {
    private final List<Change> changes;

    public Patch(List<Change> changes) {
        this.changes = changes;
    }

    /**
     * Applies patch to the target object, returning a list of PatchResult for each field.
     */
    public List<PatchResult> applyTo(Object target) {
        List<PatchResult> results = new ArrayList<>();
        for (Change change : changes) {
            PatchResult result = setNestedFieldValue(target, change.getFieldPath(), change.getNewValue());
            results.add(result);
        }
        return results;
    }

    /**
     * Sets a nested field value using dot-separated field path. Returns PatchResult for error handling.
     */
    private PatchResult setNestedFieldValue(Object target, String fieldPath, Object value) {
        String[] parts = fieldPath.split("\\.");
        Object current = target;
        for (int i = 0; i < parts.length - 1; i++) {
            Field field = findField(current.getClass(), parts[i]);
            if (field == null) {
                return PatchResult.failure(fieldPath, "No such field: " + parts[i]);
            }
            field.setAccessible(true);
            try {
                Object next = field.get(current);
                if (next == null) {
                    next = field.getType().getDeclaredConstructor().newInstance();
                    field.set(current, next);
                }
                current = next;
            } catch (Exception e) {
                return PatchResult.failure(fieldPath, "Failed at " + parts[i] + ": " + e.getMessage());
            }
        }
        Field leaf = findField(current.getClass(), parts[parts.length - 1]);
        if (leaf == null) {
            return PatchResult.failure(fieldPath, "No such field: " + parts[parts.length - 1]);
        }
        leaf.setAccessible(true);
        try {
            leaf.set(current, value);
            return PatchResult.success(fieldPath);
        } catch (Exception e) {
            return PatchResult.failure(fieldPath, "Failed to set value: " + e.getMessage());
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), fieldName);
            }
            return null;
        }
    }
}