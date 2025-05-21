package com.example.objectdiff;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PatchNestedTest {

    public static class Address {
        private String city;
        private String street;
        public Address() {}
        public Address(String city, String street) { this.city = city; this.street = street; }
        public String getCity() { return city; }
        public String getStreet() { return street; }
    }

    public static class Profile {
        private int age;
        private Address address;
        public Profile() {}
        public Profile(int age, Address address) { this.age = age; this.address = address; }
        public int getAge() { return age; }
        public Address getAddress() { return address; }
    }

    public static class Person {
        private String name;
        private Profile profile;
        public Person() {}
        public Person(String name, Profile profile) { this.name = name; this.profile = profile; }
        public String getName() { return name; }
        public Profile getProfile() { return profile; }
    }

    @Test
    void testPatchNestedField() {
        Person person = new Person("Alice", new Profile(25, new Address("London", "Baker St")));
        Change cityChange = new Change("profile.address.city", "London", "Paris");
        Change streetChange = new Change("profile.address.street", "Baker St", "Champs-Élysées");
        Change ageChange = new Change("profile.age", 25, 30);
        Patch patch = new Patch(List.of(cityChange, streetChange, ageChange));

        List<PatchResult> results = patch.applyTo(person);

        assertTrue(results.stream().allMatch(PatchResult::isSuccess));
        assertEquals("Paris", person.getProfile().getAddress().getCity());
        assertEquals("Champs-Élysées", person.getProfile().getAddress().getStreet());
        assertEquals(30, person.getProfile().getAge());
    }

    @Test
    void testPatchDeepNullIntermediate() {
        // profile is null, patch creates it
        Person person = new Person("Bob", null);
        Change cityChange = new Change("profile.address.city", null, "Berlin");
        Patch patch = new Patch(List.of(cityChange));
        List<PatchResult> results = patch.applyTo(person);

        assertTrue(results.get(0).isSuccess());
        assertNotNull(person.getProfile());
        assertNotNull(person.getProfile().getAddress());
        assertEquals("Berlin", person.getProfile().getAddress().getCity());
    }

    @Test
    void testPatchInvalidField() {
        Person person = new Person("Eve", new Profile(40, null));
        Change badChange = new Change("profile.address.zipcode", null, "12345");
        Patch patch = new Patch(List.of(badChange));
        List<PatchResult> results = patch.applyTo(person);

        assertFalse(results.get(0).isSuccess());
        assertTrue(results.get(0).getError().orElse("").contains("No such field"));
    }

    @Test
    void testPatchFailsGracefullyOnNullRoot() {
        Person person = null;
        Change change = new Change("profile.age", null, 33);
        Patch patch = new Patch(List.of(change));
        try {
            patch.applyTo(person);
            fail("Should throw NullPointerException");
        } catch (NullPointerException ignored) {}
    }
}