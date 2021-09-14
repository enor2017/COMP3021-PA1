package hk.ust.cse.comp3021.pa1.model;

import hk.ust.cse.comp3021.pa1.util.GameBoardUtils;
import hk.ust.cse.comp3021.pa1.util.ReflectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GameStateTest {

    private GameBoard gameBoard = null;
    private GameState gameState = null;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Constructors")
    void testConstructors() {
        final var clazz = GameState.class;
        final var ctors = ReflectionUtils.getPublicConstructors(clazz);

        assertEquals(2, ctors.length);

        assertDoesNotThrow(() -> clazz.getConstructor(GameBoard.class));
        assertDoesNotThrow(() -> clazz.getConstructor(GameBoard.class, int.class));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Methods")
    void testPublicMethods() {
        final var clazz = GameState.class;
        final var publicMethods = ReflectionUtils.getPublicInstanceMethods(clazz);

        assertEquals(17, publicMethods.length);

        assertDoesNotThrow(() -> clazz.getDeclaredMethod("hasWon"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("hasLost"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("increaseNumLives", int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("decreaseNumLives", int.class));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("decrementNumLives"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("incrementNumMoves"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("incrementNumDeaths"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumDeaths"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumMoves"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("hasUnlimitedLives"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumLives"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getNumGems"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getScore"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getGameBoardController"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getGameBoardView"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getGameBoard"));
        assertDoesNotThrow(() -> clazz.getDeclaredMethod("getMoveStack"));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity Test - Public Fields")
    void testPublicFields() {
        final var clazz = GameState.class;
        final var publicFields = ReflectionUtils.getPublicInstanceFields(clazz);

        assertEquals(0, publicFields.length);
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Unlimited Lives using One-Arg constructor")
    void testCreationUnlimitedLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertTrue(gameState.hasUnlimitedLives());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());
        assertEquals(1, gameState.getNumGems());
        assertEquals(4, gameState.getScore());
        assertSame(gameBoard, gameState.getGameBoard());
    }

    @Test
    @Tag("provided")
    @DisplayName("Instance Creation - Limited Lives")
    void testCreationLimitedLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard, 3);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertFalse(gameState.hasUnlimitedLives());
        assertEquals(3, gameState.getNumLives());
        assertEquals(1, gameState.getNumGems());
        assertEquals(4, gameState.getScore());
        assertSame(gameBoard, gameState.getGameBoard());
    }

    @Test
    @Tag("provided")
    @DisplayName("Test Win Condition")
    void testWinsWhenNoGems() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        gameBoard.getEntityCell(0, 1).setEntity(null);

        assertEquals(0, gameState.getNumGems());
        assertTrue(gameState.hasWon());
    }

    @Test
    @Tag("provided")
    @DisplayName("Test Lose Condition")
    void testLosesWhenNoLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard, 0);

        assertEquals(0, gameState.getNumLives());
        assertTrue(gameState.hasLost());
    }

    @Test
    @Tag("provided")
    @DisplayName("Lives Increase - Limited Lives")
    void testIncreaseNumLivesWhenNotUnlimitedLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard, 3);

        assumeFalse(gameState.hasUnlimitedLives());

        final var newNumLives = gameState.increaseNumLives(1);
        assertEquals(4, newNumLives);
        assertEquals(4, gameState.getNumLives());
    }

    @Test
    @Tag("provided")
    @DisplayName("Lives Decrease - Limited Lives")
    void testDecreaseNumLivesWhenNotUnlimitedLives() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard, 3);

        assumeFalse(gameState.hasUnlimitedLives());

        final var newNumLives = gameState.decreaseNumLives(1);
        assertEquals(2, newNumLives);
        assertEquals(2, gameState.getNumLives());
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Score - Initial Always Zero")
    void testInitialScoreAlwaysZero() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        assertEquals(4, gameState.getScore());

        gameBoard = GameBoardUtils.createGameBoard(10, 10, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        assertEquals(100, gameState.getScore());
    }

    @Test
    @Tag("provided")
    @DisplayName("Get Score - Score Increases When Gem Picked Up")
    void testScoreScalesWhenGemPickedUp() {
        gameBoard = GameBoardUtils.createGameBoard(2, 2, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else if (pos.equals(new Position(1, 0))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        assumeTrue(gameState.getScore() == 4);

        gameBoard.getEntityCell(new Position(1, 0)).setEntity(null);

        assertEquals(14, gameState.getScore());

        gameBoard = GameBoardUtils.createGameBoard(10, 10, (pos) -> {
            final Entity entity;
            if (pos.equals(new Position(0, 0))) {
                entity = new Player();
            } else if (pos.equals(new Position(0, 1))) {
                entity = new Gem();
            } else if (pos.equals(new Position(0, 2))) {
                entity = new Gem();
            } else if (pos.equals(new Position(0, 3))) {
                entity = new Gem();
            } else if (pos.equals(new Position(0, 4))) {
                entity = new Gem();
            } else {
                entity = null;
            }

            return new EntityCell(pos, entity);
        });

        gameState = new GameState(gameBoard);

        assumeTrue(gameState.getScore() == 100);

        gameBoard.getEntityCell(new Position(0, 2)).setEntity(null);

        assertEquals(110, gameState.getScore());
    }

    @AfterEach
    void tearDown() {
        gameState = null;
        gameBoard = null;
    }
}
