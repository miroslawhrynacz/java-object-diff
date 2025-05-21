package com.example.objectdiff;

import java.lang.reflect.Field;
import java.util.List;

public class Patch {
    private final List<Change> changes;

    public Patch(List<Change> changes) {
        this.changes = changes;
    }

    public void applyTo(Object target) {
        for (Change change : changes) {
            try {
                Field field = findField(target.getClass(), change.getFieldPath());
                if (field != null) {
                    field.setAccessible(true);
                    field.set(target, change.getNewValue());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to apply patch to field: " + change.getFieldPath(), e);
            }
        }
    }

    private Field findField(Class<?> clazz, String fieldPath) {
        try {
            return clazz.getDeclaredField(fieldPath);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), fieldPath);
            }
            return null;
        }
    }
}