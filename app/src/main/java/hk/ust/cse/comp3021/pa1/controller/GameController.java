package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * Controller for {@link hk.ust.cse.comp3021.pa1.InertiaTextGame}.
 *
 * <p>
 * All game state mutations should be performed by this class.
 * </p>
 */
public class GameController {

    @NotNull
    private final GameState gameState;

    /**
     * Creates an instance.
     *
     * @param gameState The instance of {@link GameState} to control.
     */
    public GameController(@NotNull final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Processes a Move action performed by the player.
     *
     * @param direction The direction the player wants to move to.
     * @return An instance of {@link MoveResult} indicating the result of the action.
     */
    public MoveResult processMove(@NotNull final Direction direction) {
        var result = gameState.getGameBoardController().makeMove(direction);
        if (result instanceof MoveResult.Valid) {
            gameState.incrementNumMoves();
            if (result instanceof MoveResult.Valid.Alive) {
                // we only push Valid.Alive movements into MoveStack.
                gameState.getMoveStack().push(result);
                // add extra lives
                gameState.increaseNumLives(((MoveResult.Valid.Alive) result).collectedExtraLives.size());
                // add gems is not necessary since its value is re-calculated every time
            } else {
                // for Dead movements, decrease lives.
                gameState.decrementNumLives();
                gameState.incrementNumDeaths();
            }
        }
        return result;
    }


    /**
     * Processes an Undo action performed by the player.
     *
     * @return {@code false} if there are no steps to undo.
     */
    public boolean processUndo() {
        if (gameState.getMoveStack().isEmpty()) {
            return false;
        } else {
            // to undo, we pop a move from moveStack,
            // and recover gameBoard from undoStack(by calling GameBoardController)
            var move = (MoveResult.Valid.Alive) gameState.getMoveStack().pop();
            gameState.getGameBoardController().undoMove(move);
            gameState.decreaseNumLives(move.collectedExtraLives.size());
            return true;
        }
    }
}
