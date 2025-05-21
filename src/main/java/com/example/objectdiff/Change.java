package com.example.objectdiff;

public class Change {
    private final String fieldPath;
    private final Object oldValue;
    private final Object newValue;

    public Change(String fieldPath, Object oldValue, Object newValue) {
        this.fieldPath = fieldPath;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getFieldPath() { return fieldPath; }
    public Object getOldValue() { return oldValue; }
    public Object getNewValue() { return newValue; }
}