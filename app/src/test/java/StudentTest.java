import hk.ust.cse.comp3021.pa1.controller.GameController;
import hk.ust.cse.comp3021.pa1.model.*;
import hk.ust.cse.comp3021.pa1.util.GameBoardUtils;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private GameBoard gameBoard = null;
    private GameState gameState = null;
    private GameController controller = null;

    //   01234567
    // 0 MM#.MWM#
    // 1 W.W.P.MW
    // 2 **.W*.#W
    // 3 *MM#W##.
    // 4 W***M##.
    // 5 ..*..*W#
    private final ArrayList<Position> minePositions = new ArrayList<>(Arrays.asList(
            new Position(0, 0),
            new Position(0, 1),
            new Position(0, 4),
            new Position(0, 6),
            new Position(1, 6),
            new Position(3, 1),
            new Position(3, 2),
            new Position(4, 4)
    ));

    private final ArrayList<Position> gemPositions = new ArrayList<>(Arrays.asList(
            new Position(2, 0),
            new Position(2, 1),
            new Position(2, 4),
            new Position(3, 0),
            new Position(4, 1),
            new Position(4, 2),
            new Position(4, 3),
            new Position(5, 2),
            new Position(5, 5)
    ));

    private final ArrayList<Position> stopCells = new ArrayList<>(Arrays.asList(
            new Position(0, 2),
            new Position(0, 7),
            new Position(2, 6),
            new Position(3, 3),
            new Position(3, 5),
            new Position(3, 6),
            new Position(4, 5),
            new Position(4, 6),
            new Position(5, 7)
    ));

    private final ArrayList<Position> wallCells = new ArrayList<>(Arrays.asList(
            new Position(0, 5),
            new Position(1, 0),
            new Position(1, 2),
            new Position(1, 7),
            new Position(2, 3),
            new Position(2, 7),
            new Position(3, 4),
            new Position(4, 0),
            new Position(5, 6)
    ));

    @Test
    @Tag("student")
    @DisplayName("Complicated Movement - No Extra Life")
    void testComplicatedMoveWithoutExtraLife() {
        gameBoard = GameBoardUtils.createGameBoard(6, 8, (pos) -> {
            if (pos.equals(new Position(1, 4))) {
                return new StopCell(pos, new Player());
            } else if (minePositions.contains(pos)) {
                return new EntityCell(pos, new Mine());
            } else if (gemPositions.contains(pos)) {
                return new EntityCell(pos, new Gem());
            } else if (stopCells.contains(pos)) {
                return new StopCell(pos);
            } else if (wallCells.contains(pos)) {
                return new Wall(pos);
            } else {
                return new EntityCell(pos);
            }
        });
        gameState = new GameState(gameBoard, 1);
        controller = new GameController(gameState);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertFalse(gameState.hasUnlimitedLives());
        assertEquals(1, gameState.getNumLives());
        assertEquals(9, gameState.getNumGems());
        assertTrue(gameState.getMoveStack().isEmpty());

        var moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(3, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(4, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(5, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(6, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(7, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(8, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(8, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(8, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(9, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(10, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());

        moveResult = controller.processMove(Direction.UP);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(11, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(12, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());

        assertEquals(0, gameState.getNumLives());
        assertEquals(1, gameState.getNumDeaths());
        assertTrue(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(48 + 30 - 12 - 4, gameState.getScore());
    }

    //   01234567
    // 0 ML#.LWM#
    // 1 W.W.P.MW
    // 2 **.W*.#W
    // 3 *LM#W##.
    // 4 W***M##.
    // 5 W.*..*W#
    ArrayList<Position> lifePos = new ArrayList<>(Arrays.asList(
            new Position(0, 1),
            new Position(0, 4),
            new Position(3, 1)
    ));
    @Test
    @Tag("student")
    @DisplayName("Complicated Movement - With Extra Life")
    void testComplicatedMoveWithExtraLife() {
        gameBoard = GameBoardUtils.createGameBoard(6, 8, (pos) -> {
            if (pos.equals(new Position(1, 4))) {
                return new StopCell(pos, new Player());
            } else if (lifePos.contains(pos)) {
                return new EntityCell(pos, new ExtraLife());
            } else if (minePositions.contains(pos)) {
                return new EntityCell(pos, new Mine());
            } else if (gemPositions.contains(pos)) {
                return new EntityCell(pos, new Gem());
            } else if (stopCells.contains(pos)) {
                return new StopCell(pos);
            } else if (wallCells.contains(pos) || pos.equals(new Position(5, 0))) {
                return new Wall(pos);
            } else {
                return new EntityCell(pos);
            }
        });
        gameState = new GameState(gameBoard, 1);
        controller = new GameController(gameState);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertFalse(gameState.hasUnlimitedLives());
        assertEquals(1, gameState.getNumLives());
        assertEquals(9, gameState.getNumGems());
        assertTrue(gameState.getMoveStack().isEmpty());

        var moveResult = controller.processMove(Direction.UP);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(3, gameState.getNumMoves());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.RIGHT);
        assertTrue(moveResult instanceof MoveResult.Invalid);
        assertEquals(3, gameState.getNumMoves());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(4, gameState.getNumMoves());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(5, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(6, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(7, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(14, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(15, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        assertEquals(48 + 20 - 15, gameState.getScore());

        // player at (4, 5)
        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(16, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(48 + 20 - 16 - 4, gameState.getScore());

        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(17, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(18, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.UP);  // now player at (0, 1)
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(19, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(3, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(20, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        assertEquals(2, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(21, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(3, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(48 + 50 - 21 - 3 * 4, gameState.getScore());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(22, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(0, gameState.getNumLives());

        assertEquals(4, gameState.getNumDeaths());
        assertTrue(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(48 + 50 - 22 - 4 * 4, gameState.getScore());
    }

    @Test
    @Tag("student")
    @DisplayName("Complicated Undo - 1 life")
    void testComplicatedUndoOneLife() {
        gameBoard = GameBoardUtils.createGameBoard(6, 8, (pos) -> {
            if (pos.equals(new Position(1, 4))) {
                return new StopCell(pos, new Player());
            } else if (lifePos.contains(pos)) {
                return new EntityCell(pos, new ExtraLife());
            } else if (minePositions.contains(pos)) {
                return new EntityCell(pos, new Mine());
            } else if (gemPositions.contains(pos)) {
                return new EntityCell(pos, new Gem());
            } else if (stopCells.contains(pos)) {
                return new StopCell(pos);
            } else if (wallCells.contains(pos) || pos.equals(new Position(5, 0))) {
                return new Wall(pos);
            } else {
                return new EntityCell(pos);
            }
        });
        gameState = new GameState(gameBoard, 1);
        controller = new GameController(gameState);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertFalse(gameState.hasUnlimitedLives());
        assertEquals(1, gameState.getNumLives());
        assertEquals(9, gameState.getNumGems());
        assertTrue(gameState.getMoveStack().isEmpty());

        // undo while empty
        assertTrue(gameState.getMoveStack().isEmpty());
        assertFalse(controller.processUndo());
        assertFalse(controller.processUndo());

        // first move
        var moveResult = controller.processMove(Direction.UP);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        // undo first move
        assertTrue(controller.processUndo());
        assertEquals(1, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        // undo once more
        assertFalse(controller.processUndo());
        assertEquals(1, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        // redo first step
        controller.processMove(Direction.UP);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        // invalid move
        controller.processMove(Direction.UP);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        // undo after invalid move (undo first step)
        assertTrue(controller.processUndo());
        assertEquals(2, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        // undo once more
        assertFalse(controller.processUndo());
        assertEquals(2, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        // redo first step
        controller.processMove(Direction.UP);
        assertEquals(3, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        assertEquals(3, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.LEFT);
        assertEquals(4, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        assertEquals(5, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        assertEquals(5, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.DOWN);
        assertEquals(6, gameState.getNumMoves());
        assertEquals(9, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.DOWN);
        assertEquals(7, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.UP);
        assertEquals(8, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.DOWN);
        assertEquals(9, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        // undo previous step
        assertTrue(controller.processUndo());
        assertEquals(9, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        // redo previous step
        controller.processMove(Direction.DOWN);
        assertEquals(10, gameState.getNumMoves());
        assertEquals(2, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        moveResult = controller.processMove(Direction.DOWN);
        assertEquals(17, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);
        assertEquals(18, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        assertEquals(48 + 20 - 18 - 3 * 2, gameState.getScore());

        // player at (4, 5)
        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(19, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 20 - 19 - 4 - 3 * 2, gameState.getScore());


        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(20, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(21, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);  // now player at (0, 1)
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(22, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(3, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(23, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(2, gameState.getNumLives());

        assertEquals(2, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(24, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(1, gameState.getNumLives());

        assertEquals(3, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 50 - 24 - 3 * 4 - 3 * 2, gameState.getScore());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(25, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(0, gameState.getNumLives());

        assertEquals(4, gameState.getNumDeaths());
        assertTrue(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 50 - 25 - 4 * 4 - 3 * 2, gameState.getScore());

    }

    @Test
    @Tag("student")
    @DisplayName("Complicated Undo - Unlimited life")
    void testComplicatedUndoUnlimitedLife() {
        gameBoard = GameBoardUtils.createGameBoard(6, 8, (pos) -> {
            if (pos.equals(new Position(1, 4))) {
                return new StopCell(pos, new Player());
            } else if (lifePos.contains(pos)) {
                return new EntityCell(pos, new ExtraLife());
            } else if (minePositions.contains(pos)) {
                return new EntityCell(pos, new Mine());
            } else if (gemPositions.contains(pos)) {
                return new EntityCell(pos, new Gem());
            } else if (stopCells.contains(pos)) {
                return new StopCell(pos);
            } else if (wallCells.contains(pos) || pos.equals(new Position(5, 0))) {
                return new Wall(pos);
            } else {
                return new EntityCell(pos);
            }
        });
        gameState = new GameState(gameBoard);
        controller = new GameController(gameState);

        assertFalse(gameState.hasWon());
        assertFalse(gameState.hasLost());
        assertEquals(0, gameState.getNumDeaths());
        assertEquals(0, gameState.getNumMoves());
        assertTrue(gameState.hasUnlimitedLives());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());
        assertEquals(9, gameState.getNumGems());
        assertTrue(gameState.getMoveStack().isEmpty());

        // undo while empty
        assertTrue(gameState.getMoveStack().isEmpty());
        assertFalse(controller.processUndo());
        assertFalse(controller.processUndo());

        // first move
        var moveResult = controller.processMove(Direction.UP);
        assertEquals(1, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // undo first move
        assertTrue(controller.processUndo());
        assertEquals(1, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // undo once more
        assertFalse(controller.processUndo());
        assertEquals(1, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // redo first step
        controller.processMove(Direction.UP);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // invalid move
        controller.processMove(Direction.UP);
        assertEquals(2, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // undo after invalid move (undo first step)
        assertTrue(controller.processUndo());
        assertEquals(2, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // undo once more
        assertFalse(controller.processUndo());
        assertEquals(2, gameState.getNumMoves());
        assertTrue(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // redo first step
        controller.processMove(Direction.UP);
        assertEquals(3, gameState.getNumMoves());
        assertFalse(gameState.getMoveStack().isEmpty());
        assertEquals(9, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        assertEquals(3, gameState.getNumMoves());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        controller.processMove(Direction.LEFT);
        assertEquals(4, gameState.getNumMoves());

        controller.processMove(Direction.RIGHT);
        assertEquals(5, gameState.getNumMoves());

        controller.processMove(Direction.RIGHT);
        assertEquals(5, gameState.getNumMoves());

        controller.processMove(Direction.DOWN);
        assertEquals(6, gameState.getNumMoves());
        assertEquals(9, gameState.getNumGems());

        controller.processMove(Direction.DOWN);
        assertEquals(7, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());

        controller.processMove(Direction.UP);
        assertEquals(8, gameState.getNumMoves());

        controller.processMove(Direction.DOWN);
        assertEquals(9, gameState.getNumMoves());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // undo previous step
        assertTrue(controller.processUndo());
        assertEquals(9, gameState.getNumMoves());
        assertEquals(8, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        // redo previous step
        controller.processMove(Direction.DOWN);
        assertEquals(10, gameState.getNumMoves());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        controller.processMove(Direction.RIGHT);
        controller.processMove(Direction.DOWN);
        controller.processMove(Direction.LEFT);
        moveResult = controller.processMove(Direction.DOWN);
        assertEquals(17, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);
        assertEquals(18, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(48 + 20 - 18 - 3 * 2, gameState.getScore());

        // player at (4, 5)
        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(19, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 20 - 19 - 4 - 3 * 2, gameState.getScore());


        moveResult = controller.processMove(Direction.DOWN);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(20, gameState.getNumMoves());
        assertEquals(7, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(21, gameState.getNumMoves());
        assertEquals(6, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        moveResult = controller.processMove(Direction.UP);  // now player at (0, 1)
        assertTrue(moveResult instanceof MoveResult.Valid.Alive);
        assertEquals(22, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(1, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(23, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(2, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(24, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(3, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 50 - 24 - 3 * 4 - 3 * 2, gameState.getScore());

        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(25, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(4, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 50 - 25 - 4 * 4 - 3 * 2, gameState.getScore());

        // AGAIN!!
        moveResult = controller.processMove(Direction.LEFT);
        assertTrue(moveResult instanceof MoveResult.Valid.Dead);
        assertEquals(26, gameState.getNumMoves());
        assertEquals(4, gameState.getNumGems());
        assertEquals(Integer.MAX_VALUE, gameState.getNumLives());

        assertEquals(5, gameState.getNumDeaths());
        assertFalse(gameState.hasLost());
        assertFalse(gameState.hasWon());
        assertFalse(gameState.getMoveStack().isEmpty());

        assertEquals(48 + 50 - 26 - 5 * 4 - 3 * 2, gameState.getScore());

    }

    @AfterEach
    void tearDown() {
        controller = null;
        gameState = null;
        gameBoard = null;
    }
}
