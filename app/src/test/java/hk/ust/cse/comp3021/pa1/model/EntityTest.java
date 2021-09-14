package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private Entity entity = null;

    @BeforeEach
    void setUp() {
        entity = new Player();
    }

    @SuppressWarnings({"Since15", "preview"})
    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Class is Sealed")
    void testClassIsSealed() {
        assertTrue(Entity.class.isSealed());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = Entity.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(0, ctors.length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = Entity.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(2, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getDeclaredMethod("setOwner", EntityCell.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getOwner"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = Entity.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Owner - Test Initially Null")
    void testGetOwnerNull() {
        assertNull(entity.getOwner());
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Owner - Test Not-Null after creation with cell")
    void testGetOwnerNotNull() {
        final var entityCell = new EntityCell(new Position(0, 0));

        entity = new Player(entityCell);

        assertEquals(entityCell, entity.getOwner());
    }

    @Test
    @Tag("provided")
    @DisplayName("Set Owner - Not-Null to Not-Null")
    void testSetOwnerNotNullToNotNull() {
        final var prevEntityCell = new EntityCell(new Position(0, 0));
        final var newEntityCell = new EntityCell(new Position(1, 0));

        entity = new Player(prevEntityCell);

        final var prevOwner = entity.setOwner(newEntityCell);
        assertEquals(newEntityCell, entity.getOwner());
        assertEquals(prevEntityCell, prevOwner);
    }

    @AfterEach
    void tearDown() {
        entity = null;
    }
}
