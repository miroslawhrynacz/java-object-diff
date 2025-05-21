# Java Object Diff & Patch Utility

Minimal, dependency-free utility to compute the diff and patch between two Java objects.

## Features

- Compute field-level differences between two POJOs.
- Generate a summary of changes.
- Apply a patch to an existing object.

## Usage Example

```java
Person oldPerson = new Person("Alice", 30);
Person newPerson = new Person("Alice", 31);

DiffResult diff = ObjectDiffer.diff(oldPerson, newPerson);
System.out.println(diff.toSummary()); // e.g. "MODIFIED: age changed from 30 to 31"

Patch patch = diff.toPatch();
patch.applyTo(oldPerson); // oldPerson.age is now 31
```

## Testing

Run tests with:

```bash
./gradlew test
```

## License

MIT