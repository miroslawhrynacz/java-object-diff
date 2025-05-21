package com.example.objectdiff;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectDiffer {
    public static DiffResult diff(Object oldObj, Object newObj) {
        List<Change> changes = new ArrayList<>();
        diffRecursive("", oldObj, newObj, changes);
        return new DiffResult(changes);
    }

    private static void diffRecursive(String path, Object oldObj, Object newObj, List<Change> changes) {
        if (oldObj == null && newObj == null) return;
        if (oldObj == null) {
            changes.add(new Change(path, Change.Type.ADDED, null, newObj));
            return;
        }
        if (newObj == null) {
            changes.add(new Change(path, Change.Type.REMOVED, oldObj, null));
            return;
        }
        if (!oldObj.getClass().equals(newObj.getClass())) {
            changes.add(new Change(path, Change.Type.MODIFIED, oldObj, newObj));
            return;
        }
        if (isPrimitiveOrWrapperOrString(oldObj.getClass())) {
            if (!Objects.equals(oldObj, newObj)) {
                changes.add(new Change(path, Change.Type.MODIFIED, oldObj, newObj));
            }
            return;
        }

        for (Field field : oldObj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object oldVal = field.get(oldObj);
                Object newVal = field.get(newObj);
                String subPath = path.isEmpty() ? field.getName() : path + "." + field.getName();
                diffRecursive(subPath, oldVal, newVal, changes);
            } catch (IllegalAccessException ignored) {}
        }
    }

    private static boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class);
    }
}