package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * Controller for {@link GameBoard}.
 *
 * <p>
 * This class is responsible for providing high-level operations to mutate a {@link GameBoard}. This should be the only
 * class which mutates the game board; Other classes should use this class to mutate the game board.
 * </p>
 */
public class GameBoardController {

    @NotNull
    private final GameBoard gameBoard;

    /**
     * Creates an instance.
     *
     * @param gameBoard The instance of {@link GameBoard} to control.
     */
    public GameBoardController(@NotNull final GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Moves the player in the given direction.
     *
     * <p>
     * You should ensure that the game board is only mutated if the move is valid and results in the player still being
     * alive. If the player dies after moving or the move is invalid, the game board should remain in the same state as
     * before this method was called.
     * </p>
     *
     * @param direction Direction to move the player in.
     * @return An instance of {@link MoveResult} representing the result of this action.
     */
    @NotNull
    public MoveResult makeMove(@NotNull final Direction direction) {
        var playerPos = getPlayerPos();
        var newPos = checkCanMove(playerPos, direction);
        if (newPos == null) {
            return new MoveResult.Invalid(playerPos);
        } else {
            MoveResult result;
            // record prevPos during the process of consecutive moving
            Position prevPos = null;
            // record collected Gems and ExtraLives
            LinkedList<Position> collectedGems = new LinkedList<>();
            LinkedList<Position> collectedLives = new LinkedList<>();
            // check if the player can continue to move.
            while(true) {
                // cannot move on, since this pos is wall or out of bound
                // we just stop at previous position
                // (this cannot happen in first loop)
                if (newPos == null) {
                    result = new MoveResult.Valid.Alive(prevPos, playerPos,
                            collectedGems, collectedLives);
                    // set new position for player
                    var newPlayerCell = gameBoard.getEntityCell(prevPos);
                    newPlayerCell.setEntity(gameBoard.getPlayer());
                    break;
                } else if (isMine(newPos)) {
                    // if player hit mine this step
                    result = new MoveResult.Valid.Dead(playerPos, newPos);
                    break;
                } else if (isStop(newPos)) {
                    // or, if player reach a stop cell this step
                    result = new MoveResult.Valid.Alive(newPos, playerPos,
                            collectedGems, collectedLives);
                    // set new position for player
                    var newPlayerCell = gameBoard.getEntityCell(newPos);
                    newPlayerCell.setEntity(gameBoard.getPlayer());
                    break;
                } else {
                    // otherwise, player can continue moving,
                    // and check if current pos is gem/extraLife
                    // ATTENTION: just set the entity to null, rather than modify the values.
                    if (containsGem(newPos)) {
                        gameBoard.getEntityCell(newPos).setEntity(null);
                        collectedGems.push(newPos);
                    }
                    if (containsExtraLife(newPos)) {
                        gameBoard.getEntityCell(newPos).setEntity(null);
                        collectedLives.push(newPos);
                    }
                    prevPos = newPos;
                    newPos = checkCanMove(newPos, direction);
                }
            }
            return result;
        }
    }

    /**
     * helper function: check if player can move to given direction
     * some invalid movements:
     * (1) if out of bound after movement
     * (2) if in the wall after movement
     * @param pos the current position of player
     * @param direction the direction player want to move
     * @return new position after movement, if can move; null if cannot
     */
    private Position checkCanMove(@NotNull final Position pos, @NotNull final Direction direction) {
        var offSet = direction.getOffset();
        int rows = gameBoard.getNumRows();
        int cols = gameBoard.getNumCols();
        var newPos = pos.offsetByOrNull(offSet, rows, cols);
        // if out of bound or is a wall cell
        if (newPos == null || getCell(newPos) instanceof Wall) {
            return null;
        }
        return newPos;
    }

    /**
     * check if given position is a mine cell.
     * @param pos the position to check
     * @return true if is a mine, false otherwise.
     */
    private boolean isMine(@NotNull final Position pos) {
        Cell currCell = getCell(pos);
        if (currCell instanceof EntityCell) {
            return ((EntityCell) currCell).getEntity() instanceof Mine;
        }
        return false;
    }

    /**
     * check if given position is a stop cell.
     * @param pos the position to check
     * @return true if is a stop Cell, false otherwise.
     */
    private boolean isStop(@NotNull final Position pos) {
        Cell currCell = getCell(pos);
        return currCell instanceof StopCell;
    }

    /**
     * check if given position contains a gem.
     * @param pos the position to check
     * @return true if contains a gem, false otherwise.
     */
    private boolean containsGem(@NotNull final Position pos) {
        Cell currCell = getCell(pos);
        if (currCell instanceof EntityCell) {
            return ((EntityCell) currCell).getEntity() instanceof Gem;
        }
        return false;
    }

    /**
     * check if given position contains an ExtraLife entity.
     * @param pos the position to check
     * @return true if contains an ExtraLife entity, false otherwise.
     */
    private boolean containsExtraLife(@NotNull final Position pos) {
        Cell currCell = getCell(pos);
        if (currCell instanceof EntityCell) {
            return ((EntityCell) currCell).getEntity() instanceof ExtraLife;
        }
        return false;
    }

    /**
     * helper method: get the cell object at given position
     * @param pos given position
     * @return the cell at that position.
     */
    private Cell getCell(@NotNull final Position pos) {
        return gameBoard.getCell(pos);
    }

    /**
     * helper method: return the player's position
     * @return the player's position, in a 'Position' object.
     */
    private Position getPlayerPos() {
        var ownerCell = gameBoard.getPlayer().getOwner();
        if (ownerCell == null) {
            throw new IllegalArgumentException("How come? player has no owner??");
        }
        return ownerCell.getPosition();
    }

    /**
     * Undoes a move by reverting all changes performed by the specified move.
     *
     * <p>
     * Hint: Undoing a move is effectively the same as reversing everything you have done to make a move.
     * </p>
     *
     * @param prevMove The {@link MoveResult} object to revert.
     */
    public void undoMove(@NotNull final MoveResult prevMove) {
        if (!(prevMove instanceof MoveResult.Valid.Alive)) {
            throw new IllegalArgumentException("The previous move to undo must be valid and alive.");
        }
        var move = (MoveResult.Valid.Alive) prevMove;
        // reset the position of Player, Gems and ExtraLives
        gameBoard.getEntityCell(move.origPosition).setEntity(gameBoard.getPlayer());
        for (var gemPos : move.collectedGems) {
            gameBoard.getEntityCell(gemPos).setEntity(new Gem());
        }
        for (var lifePos : move.collectedExtraLives) {
            gameBoard.getEntityCell(lifePos).setEntity(new ExtraLife());
        }
    }
}
