package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionOffsetTest {

    private PositionOffset posOffset = null;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Class Is Record")
    void testClassIsRecord() {
        assertTrue(PositionOffset.class.isRecord());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = PositionOffset.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(int.class, int.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = PositionOffset.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        // Records have numFields + 3 (equals, hashCode, toString) public non-static declared methods
        assertEquals(5, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = PositionOffset.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Creation")
    void testCreation() {
        assertDoesNotThrow(() -> posOffset = new PositionOffset(0, 0));
        assertDoesNotThrow(() -> posOffset = new PositionOffset(1, 0));
        assertDoesNotThrow(() -> posOffset = new PositionOffset(1, -1));
    }
}
