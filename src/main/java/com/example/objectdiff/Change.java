package com.example.objectdiff;

public class Change {
    public enum Type { ADDED, REMOVED, MODIFIED }

    private final String fieldPath;
    private final Type type;
    private final Object oldValue;
    private final Object newValue;

    public Change(String fieldPath, Type type, Object oldValue, Object newValue) {
        this.fieldPath = fieldPath;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getFieldPath() { return fieldPath; }
    public Type getType() { return type; }
    public Object getOldValue() { return oldValue; }
    public Object getNewValue() { return newValue; }

    @Override
    public String toString() {
        return String.format("%s: %s changed from %s to %s", type, fieldPath, oldValue, newValue);
    }
}