package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoveStackTest {

    private MoveStack moveStack;

    @BeforeEach
    void setUp() {
        moveStack = new MoveStack();
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = MoveStack.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = MoveStack.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(5, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getDeclaredMethod("push", MoveResult.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("isEmpty"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("pop"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getPopCount"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("peek"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = MoveStack.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("provided")
    @DisplayName("Initial State")
    void testInitialState() {
        assertEquals(0, moveStack.getPopCount());
        assertTrue(moveStack.isEmpty());
    }

    @Test
    @Tag("provided")
    @DisplayName("Push Move")
    void testPush() {
        final var move = new MoveResult.Valid.Alive(
                new Position(1, 0),
                new Position(0, 0)
        );

        moveStack.push(move);

        assertFalse(moveStack.isEmpty());
        assertEquals(moveStack.peek(), move);
    }

    @Test
    @Tag("provided")
    @DisplayName("Pop Move")
    void testPop() {
        final var move = new MoveResult.Valid.Alive(
                new Position(1, 0),
                new Position(0, 0)
        );

        moveStack.push(move);

        final var poppedElem = assertDoesNotThrow(() -> moveStack.pop());

        assertTrue(moveStack.isEmpty());
        assertEquals(move, poppedElem);
        assertEquals(1, moveStack.getPopCount());
    }

    @AfterEach
    void tearDown() {
        moveStack = null;
    }
}
