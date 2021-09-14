package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityCellTest {

    private Position position;
    private EntityCell cell;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = EntityCell.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(2, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(Position.class));
        assertDoesNotThrow(() -> clazz.getConstructor(Position.class, Entity.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = EntityCell.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(4, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getDeclaredMethod("setEntity", Entity.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getEntity"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("toUnicodeChar"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("toASCIIChar"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = EntityCell.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Position Only")
    void testInstanceCreationWithPos() {
        position = new Position(0, 0);
        cell = new EntityCell(position);

        assertEquals(0, cell.getPosition().row());
        assertEquals(0, cell.getPosition().col());
        assertNull(cell.entity);
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Position and Null Entity")
    void testInstanceCreationWithPosAndNullEntity() {
        position = new Position(0, 0);
        cell = new EntityCell(position, null);

        assertEquals(0, cell.getPosition().row());
        assertEquals(0, cell.getPosition().col());
        assertNull(cell.entity);
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Position and Not-Null Entity")
    void testInstanceCreationWithPosAndNotNullEntity() {
        final var player = new Player();
        position = new Position(0, 0);
        cell = new EntityCell(position, player);

        assertEquals(0, cell.getPosition().row());
        assertEquals(0, cell.getPosition().col());
        assertSame(player, cell.entity);
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Entity - Null Entity")
    void testGetEntityNull() {
        position = new Position(0, 0);
        cell = new EntityCell(position);

        assertNull(cell.getEntity());
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Entity - Not-Null Entity")
    void testGetEntityNotNull() {
        final var player = new Player();
        position = new Position(0, 0);
        cell = new EntityCell(position, player);

        assertNotNull(cell.getEntity());
    }

    @Test
    @Tag("provided")
    @DisplayName("Set Entity - Null to Not-Null")
    void testSetEntityFromNotNullToNotNull() {
        final var origPlayer = new Player();
        position = new Position(0, 0);
        cell = new EntityCell(position, origPlayer);

        final var newPlayer = new Player();
        final var prev = cell.setEntity(newPlayer);

        assertSame(newPlayer, cell.entity);
        assertSame(cell, newPlayer.getOwner());
        assertNull(origPlayer.getOwner());
        assertSame(origPlayer, prev);
    }

    @AfterEach
    void tearDown() {
        cell = null;
        position = null;
    }
}
