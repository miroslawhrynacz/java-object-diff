package com.example.objectdiff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectDifferTest {
    static class Person {
        String name;
        int age;
        public Person(String name, int age) { this.name = name; this.age = age; }
    }

    @Test
    void testDiff() {
        Person a = new Person("Alice", 30);
        Person b = new Person("Alice", 31);

        DiffResult result = ObjectDiffer.diff(a, b);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getChanges().size());
        assertEquals("age", result.getChanges().get(0).getFieldPath());
        assertEquals(30, result.getChanges().get(0).getOldValue());
        assertEquals(31, result.getChanges().get(0).getNewValue());

        // Patch test
        Person c = new Person("Alice", 30);
        result.toPatch().applyTo(c);
        assertEquals(31, c.age);
    }
}