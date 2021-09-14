package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.GameBoardUtils;
import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private GameBoard gameBoard;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = GameBoard.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(int.class, int.class, Cell[][].class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = GameBoard.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(10, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getRow", int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getCol", int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getCell", int.class, int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getCell", Position.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getEntityCell", int.class, int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getEntityCell", int.class, int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumRows"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumCols"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getPlayer"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumGems"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = GameBoard.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    // P*.
    // ...
    // ...
    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Valid")
    void testBasicGameBoardCreation() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[0][1]).setEntity(new Gem());

        assertDoesNotThrow(() -> gameBoard = new GameBoard(rows, cols, cells));
        assertTrue(((EntityCell) gameBoard.getCell(0, 0)).getEntity() instanceof Player);
        assertTrue(((EntityCell) gameBoard.getCell(0, 1)).getEntity() instanceof Gem);
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Bad Row")
    void testGameBoardCreationBadRowCount() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(2, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[0][1]).setEntity(new Gem());

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Bad Column")
    void testGameBoardCreationBadColCount() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, 2, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[0][1]).setEntity(new Gem());

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    // .*.
    // ...
    // ...
    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Missing Player")
    void testGameBoardCreationMissingPlayer() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][1]).setEntity(new Gem());

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    // PP*
    // ...
    // ...
    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Too Many Players")
    void testGameBoardCreationMissingTooManyPlayers() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[0][1]).setEntity(new Player());
        ((EntityCell) cells[0][2]).setEntity(new Gem());

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    // P..
    // ...
    // ...
    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - No Gems")
    void testGameBoardCreationMissingGem() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    // P....
    // WWWW.
    // .....
    // .WWWW
    // ...W*
    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Unreachable Gem in Linear Path")
    void testGameBoardCreationUnreachableGemLinear() {
        final var rows = 5;
        final var cols = 5;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[4][4]).setEntity(new Gem());

        for (int c = 0; c < cols - 1; ++c) {
            cells[1][c] = new Wall(new Position(1, c));
        }
        for (int c = 1; c < cols; ++c) {
            cells[3][c] = new Wall(new Position(3, c));
        }
        cells[4][3] = new Wall(new Position(4, 3));

        assertThrows(IllegalArgumentException.class, () -> gameBoard = new GameBoard(rows, cols, cells));
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Cell - int-overload")
    void testGetCellWithInts() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[0][2]).setEntity(new Gem());

        gameBoard = new GameBoard(rows, cols, cells);

        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                assertEquals(cells[r][c], gameBoard.getCell(r, c), "Mismatch for r=" + r + " c=" + c);
            }
        }
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Entity Cell - Invalid using int-overload")
    void testGetEntityCellWithIntsInvalid() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        ((EntityCell) cells[0][0]).setEntity(new Player());
        cells[0][1] = new Wall(new Position(0, 1));
        ((EntityCell) cells[0][2]).setEntity(new Gem());

        gameBoard = new GameBoard(rows, cols, cells);

        assertThrows(IllegalArgumentException.class, () -> gameBoard.getEntityCell(0, 1));
    }

    // P**
    // *.*
    // ***
    @Test
    @Tag("provided")
    @DisplayName("Get Number of Gems - Filled")
    void testGetNumGemsAll() {
        final var rows = 3;
        final var cols = 3;
        final var cells = GameBoardUtils.createEmptyCellArray(rows, cols, EntityCell::new);

        for (final var row : cells) {
            for (final var c : row) {
                ((EntityCell) c).setEntity(new Gem());
            }
        }
        ((EntityCell) cells[0][0]).setEntity(new Player());
        ((EntityCell) cells[1][1]).setEntity(null);

        gameBoard = new GameBoard(rows, cols, cells);

        assertEquals(7, gameBoard.getNumGems());
    }

    @AfterEach
    void tearDown() {
        gameBoard = null;
    }
}
