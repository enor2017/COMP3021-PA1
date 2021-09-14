package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveResultTest {

    @SuppressWarnings({"Since15", "preview"})
    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Class is Sealed")
    void testClassIsSealed() {
        assertTrue(MoveResult.class.isSealed());
        assertTrue(MoveResult.Valid.class.isSealed());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = MoveResult.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(0, ctors.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = MoveResult.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(0, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = MoveResult.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(1, publicFields.length);

        assertDoesNotThrow(() -> clazz.getDeclaredField("newPosition"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors for Invalid")
    void testInvalidConstructors() {
        final var clazz = MoveResult.Invalid.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(Position.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods for Invalid")
    void testInvalidPublicMethods() {
        final var clazz = MoveResult.Invalid.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(0, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields for Invalid")
    void testInvalidPublicFields() {
        final var clazz = MoveResult.Invalid.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors for Valid")
    void testValidConstructors() {
        final var clazz = MoveResult.Valid.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(0, ctors.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods for Valid")
    void testValidPublicMethods() {
        final var clazz = MoveResult.Valid.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(0, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields for Valid")
    void testValidPublicFields() {
        final var clazz = MoveResult.Valid.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(1, publicFields.length);

        assertDoesNotThrow(() -> clazz.getDeclaredField("origPosition"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors for Valid.Alive")
    void testValidAliveConstructors() {
        final var clazz = MoveResult.Valid.Alive.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(2, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(Position.class, Position.class));
        assertDoesNotThrow(() -> clazz.getConstructor(Position.class, Position.class, List.class, List.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods for Valid.Alive")
    void testValidAlivePublicMethods() {
        final var clazz = MoveResult.Valid.Alive.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(0, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields for Valid.Alive")
    void testValidAlivePublicFields() {
        final var clazz = MoveResult.Valid.Alive.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(2, publicFields.length);

        assertDoesNotThrow(() -> clazz.getDeclaredField("collectedGems"));
        assertDoesNotThrow(() -> clazz.getDeclaredField("collectedExtraLives"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors for Valid.Dead")
    void testValidDeadConstructors() {
        final var clazz = MoveResult.Valid.Dead.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(Position.class, Position.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods for Valid.Dead")
    void testValidDeadPublicMethods() {
        final var clazz = MoveResult.Valid.Dead.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(0, publicMethods.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields for Valid.Dead")
    void testValidDeadPublicFields() {
        final var clazz = MoveResult.Valid.Dead.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(1, publicFields.length);

        assertDoesNotThrow(() -> clazz.getDeclaredField("minePosition"));
    }
}
