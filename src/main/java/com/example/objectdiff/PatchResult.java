package com.example.objectdiff;

import java.util.Optional;

public class PatchResult {
    private final boolean success;
    private final String fieldPath;
    private final Optional<String> error;

    private PatchResult(boolean success, String fieldPath, String error) {
        this.success = success;
        this.fieldPath = fieldPath;
        this.error = Optional.ofNullable(error);
    }

    public static PatchResult success(String fieldPath) {
        return new PatchResult(true, fieldPath, null);
    }

    public static PatchResult failure(String fieldPath, String error) {
        return new PatchResult(false, fieldPath, error);
    }

    public boolean isSuccess() { return success; }
    public String getFieldPath() { return fieldPath; }
    public Optional<String> getError() { return error; }

    @Override
    public String toString() {
        return success ? "Success: " + fieldPath : "Failure: " + fieldPath + " (" + error.orElse("") + ")";
    }
}