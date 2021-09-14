package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import hk.ust.cse.comp3021.pa1.util.GameBoardUtils;
import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GameBoardControllerTest {

    private GameBoard gameBoard = null;
    private GameBoardController controller = null;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = GameBoardController.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(1, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(GameBoard.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = GameBoardController.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(2, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getMethod("makeMove", Direction.class));
        assertDoesNotThrow(() -> clazz.getMethod("undoMove", MoveResult.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = GameBoardController.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    // For UP and LEFT:
    // P.*
    // ...
    // ...
    //
    // For DOWN and RIGHT:
    // ..*
    // ...
    // ..P
    @ParameterizedTest
    @Tag("provided")
    @EnumSource(value = Direction.class)
    @DisplayName("Make Move - Move to Adjacent Border")
    void testMakeMoveToBorder(final Direction direction) {
        final Position expectedPos;
        if (direction == Direction.UP || direction == Direction.LEFT) {
            expectedPos = new Position(0, 0);
        } else {
            expectedPos = new Position(2, 2);
        }

        gameBoard = GameBoardUtils.createGameBoard(3, 3, (pos) -> {
            if (pos.equals(expectedPos)) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 2))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(expectedPos).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(expectedPos).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Gem);

        final var moveResult = controller.makeMove(direction);

        assertTrue(moveResult instanceof MoveResult.Invalid);

        final var invalidResult = (MoveResult.Invalid) moveResult;

        // Result Assertions
        assertEquals(expectedPos, invalidResult.newPosition);

        // Non-Mutation Assertions
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(expectedPos).getEntity());
        assertEquals(1, gameBoard.getNumGems());
        assertTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Gem);
    }

    // P.W*
    // ....
    @Test
    @Tag("provided")
    @DisplayName("Make Move - Move to Wall")
    void testMakeValidMoveToWall() {
        gameBoard = GameBoardUtils.createGameBoard(2, 4, (pos) -> {
            if (pos.equals(new Position(0, 0))) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 2))) {
                return new Wall(pos);
            } else if (pos.equals(new Position(0, 3))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 0).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 0).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(0, 3).getEntity() instanceof Gem);

        final var moveResult = controller.makeMove(Direction.RIGHT);

        assertTrue(moveResult instanceof MoveResult.Valid.Alive);

        final var aliveResult = (MoveResult.Valid.Alive) moveResult;

        // Result Assertions
        assertEquals(new Position(0, 1), aliveResult.newPosition);
        assertEquals(new Position(0, 0), aliveResult.origPosition);
        assertTrue(aliveResult.collectedGems.isEmpty());
        assertTrue(aliveResult.collectedExtraLives.isEmpty());

        // GameBoard Mutation Assertions
        assertNull(gameBoard.getEntityCell(0, 0).getEntity());
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 1).getEntity());

        // Non-Mutation Assertions
        assertEquals(1, gameBoard.getNumGems());
        assertTrue(gameBoard.getEntityCell(0, 3).getEntity() instanceof Gem);
    }

    // P.#.
    // ...*
    @Test
    @Tag("provided")
    @DisplayName("Make Move - Move to StopCell")
    void testMakeValidMoveToStopCell() {
        gameBoard = GameBoardUtils.createGameBoard(2, 4, (pos) -> {
            if (pos.equals(new Position(0, 0))) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 2))) {
                return new StopCell(pos);
            } else if (pos.equals(new Position(1, 3))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 0).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 0).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(1, 3).getEntity() instanceof Gem);

        final var moveResult = controller.makeMove(Direction.RIGHT);

        assertTrue(moveResult instanceof MoveResult.Valid.Alive);

        final var aliveResult = (MoveResult.Valid.Alive) moveResult;

        // Result Assertions
        assertEquals(new Position(0, 2), aliveResult.newPosition);
        assertEquals(new Position(0, 0), aliveResult.origPosition);
        assertTrue(aliveResult.collectedGems.isEmpty());
        assertTrue(aliveResult.collectedExtraLives.isEmpty());

        // GameBoard Mutation Assertions
        assertNull(gameBoard.getEntityCell(0, 0).getEntity());
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 2).getEntity());

        // Non-Mutation Assertions
        assertEquals(1, gameBoard.getNumGems());
        assertTrue(gameBoard.getEntityCell(1, 3).getEntity() instanceof Gem);
    }

    // P**#
    @Test
    @Tag("provided")
    @DisplayName("Make Move - Move passes Gem")
    void testMakeValidMovePassingGem() {
        gameBoard = GameBoardUtils.createGameBoard(1, 4, (pos) -> {
            if (pos.equals(new Position(0, 0))) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 1)) || pos.equals(new Position(0, 2))) {
                return new EntityCell(pos, new Gem());
            } else if (pos.equals(new Position(0, 3))) {
                return new StopCell(pos);
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 0).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 0).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 2);
        assumeTrue(gameBoard.getEntityCell(0, 1).getEntity() instanceof Gem);
        assumeTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Gem);

        final var moveResult = controller.makeMove(Direction.RIGHT);

        assertTrue(moveResult instanceof MoveResult.Valid.Alive);

        final var aliveResult = (MoveResult.Valid.Alive) moveResult;

        // Result Assertions
        assertEquals(new Position(0, 3), aliveResult.newPosition);
        assertEquals(new Position(0, 0), aliveResult.origPosition);
        assertEquals(2, aliveResult.collectedGems.size());
        assertTrue(aliveResult.collectedGems.contains(new Position(0, 1)));
        assertTrue(aliveResult.collectedGems.contains(new Position(0, 2)));
        assertTrue(aliveResult.collectedExtraLives.isEmpty());

        // GameBoard Mutation Assertions
        assertNull(gameBoard.getEntityCell(0, 0).getEntity());
        assertNull(gameBoard.getEntityCell(0, 1).getEntity());
        assertNull(gameBoard.getEntityCell(0, 2).getEntity());
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 3).getEntity());

        // Non-Mutation Assertions
        assertEquals(0, gameBoard.getNumGems());
    }

    // PLL#
    // ...*
    @Test
    @Tag("provided")
    @DisplayName("Make Move - Move passes ExtraLives")
    void testMakeValidMovePassingExtraLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 4, (pos) -> {
            if (pos.equals(new Position(0, 0))) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 1)) || pos.equals(new Position(0, 2))) {
                return new EntityCell(pos, new ExtraLife());
            } else if (pos.equals(new Position(0, 3))) {
                return new StopCell(pos);
            } else if (pos.equals(new Position(1, 3))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 0).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 0).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(1, 3).getEntity() instanceof Gem);
        assumeTrue(gameBoard.getEntityCell(0, 1).getEntity() instanceof ExtraLife);
        assumeTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof ExtraLife);

        final var moveResult = controller.makeMove(Direction.RIGHT);

        assertTrue(moveResult instanceof MoveResult.Valid.Alive);

        final var aliveResult = (MoveResult.Valid.Alive) moveResult;

        // Result Assertions
        assertEquals(new Position(0, 3), aliveResult.newPosition);
        assertEquals(new Position(0, 0), aliveResult.origPosition);
        assertTrue(aliveResult.collectedGems.isEmpty());
        assertEquals(2, aliveResult.collectedExtraLives.size());
        assertTrue(aliveResult.collectedExtraLives.contains(new Position(0, 1)));
        assertTrue(aliveResult.collectedExtraLives.contains(new Position(0, 2)));

        // GameBoard Mutation Assertions
        assertNull(gameBoard.getEntityCell(0, 0).getEntity());
        assertNull(gameBoard.getEntityCell(0, 1).getEntity());
        assertNull(gameBoard.getEntityCell(0, 2).getEntity());
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 3).getEntity());

        // Non-Mutation Assertions
        assertEquals(1, gameBoard.getNumGems());
        assertTrue(gameBoard.getEntityCell(1, 3).getEntity() instanceof Gem);
    }

    // P.M
    // ..*
    @Test
    @Tag("provided")
    @DisplayName("Make Move - Move hits Mine")
    void testMakeValidMoveToMine() {
        gameBoard = GameBoardUtils.createGameBoard(2, 3, (pos) -> {
            if (pos.equals(new Position(0, 0))) {
                return new EntityCell(pos, new Player());
            } else if (pos.equals(new Position(0, 2))) {
                return new EntityCell(pos, new Mine());
            } else if (pos.equals(new Position(1, 2))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 0).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 0).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(1, 2).getEntity() instanceof Gem);
        assumeTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Mine);

        final var moveResult = controller.makeMove(Direction.RIGHT);

        assertTrue(moveResult instanceof MoveResult.Valid.Dead);

        final var deadResult = (MoveResult.Valid.Dead) moveResult;

        // Result Assertions
        assertEquals(new Position(0, 0), deadResult.newPosition);
        assertEquals(new Position(0, 0), deadResult.origPosition);

        // Non-Mutation Assertions
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 0).getEntity());
        assertTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Mine);
        assertEquals(1, gameBoard.getNumGems());
        assertTrue(gameBoard.getEntityCell(1, 2).getEntity() instanceof Gem);
    }

    // Undoes the following move:
    // P.# -> ..P
    // ..*    ..*
    @Test
    @Tag("provided")
    @DisplayName("Undo Move - Simple")
    void testUndoMoveTrivial() {
        gameBoard = GameBoardUtils.createGameBoard(2, 3, (pos) -> {
            if (pos.equals(new Position(0, 2))) {
                return new StopCell(pos, new Player());
            } else if (pos.equals(new Position(1, 2))) {
                return new EntityCell(pos, new Gem());
            } else {
                return new EntityCell(pos);
            }
        });
        controller = new GameBoardController(gameBoard);

        assumeTrue(gameBoard.getEntityCell(0, 2).getEntity() instanceof Player);
        assumeTrue(gameBoard.getPlayer().equals(gameBoard.getEntityCell(0, 2).getEntity()));
        assumeTrue(gameBoard.getNumGems() == 1);
        assumeTrue(gameBoard.getEntityCell(1, 2).getEntity() instanceof Gem);

        final var moveToUndo = new MoveResult.Valid.Alive(
                new Position(0, 2),
                new Position(0, 0),
                Collections.emptyList(),
                Collections.emptyList()
        );

        controller.undoMove(moveToUndo);

        // Mutation Assertions
        assertNull(gameBoard.getEntityCell(0, 2).getEntity());
        assertEquals(gameBoard.getPlayer(), gameBoard.getEntityCell(0, 0).getEntity());

        // Non-Mutation Assertions
        assertTrue(gameBoard.getEntityCell(0, 2) instanceof StopCell);
        assertTrue(gameBoard.getEntityCell(1, 2).getEntity() instanceof Gem);
    }

    @AfterEach
    void tearDown() {
        controller = null;
        gameBoard = null;
    }
}
