package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    private Position position = null;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Class Is Record")
    void testClassIsRecord() {
        assertTrue(Position.class.isRecord());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = Position.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(int.class, int.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = Position.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        // Records have numFields + 3 (equals, hashCode, toString) public non-static declared methods
        assertEquals(9, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getMethod("offsetBy", int.class, int.class));
        assertDoesNotThrow(() -> clazz.getMethod("offsetBy", PositionOffset.class));
        assertDoesNotThrow(() -> clazz.getMethod("offsetByOrNull", int.class, int.class, int.class, int.class));
        assertDoesNotThrow(() -> clazz.getMethod("offsetByOrNull", PositionOffset.class, int.class, int.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = Position.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Creation with Valid Values")
    void testCreation() {
        assertDoesNotThrow(() -> position = new Position(0, 0));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Creation with Invalid Values")
    void testCreationFailure() {
        assertThrows(IllegalArgumentException.class, () -> position = new Position(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> position = new Position(0, -1));
        assertThrows(IllegalArgumentException.class, () -> position = new Position(-1, -1));
    }

    @AfterEach
    void tearDown() {
        position = null;
    }
}
