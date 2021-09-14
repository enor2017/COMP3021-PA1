package hk.ust.cse.comp3021.pa1.view;

import hk.ust.cse.comp3021.pa1.model.GameState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A read-only view of {@link GameState}.
 */
public class GameView {

    @NotNull
    private final GameState gameState;

    @NotNull
    private final GameBoardView gameBoardView;

    /**
     * Creates an instance.
     *
     * @param gameState The instance of {@link GameState} to view.
     */
    public GameView(@NotNull final GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
        this.gameBoardView = gameState.getGameBoardView();
    }

    /**
     * @return {@code true} if the game is won.
     */
    public boolean hasWon() {
        return gameState.hasWon();
    }

    /**
     * @return {@code true} if the game is lost.
     */
    public boolean hasLost() {
        return gameState.hasLost();
    }

    /**
     * Outputs the current state of the game to {@link System#out}.
     *
     * @param useUnicodeChars If {@code true}, outputs the board elements using Unicode characters (as opposed to ASCII
     *                        characters).
     */
    public void output(final boolean useUnicodeChars) {
        gameBoardView.output(useUnicodeChars);

        System.out.println();
        System.out.println("Number of Gems  : " + gameState.getNumGems());
        System.out.println("Number of Moves : " + gameState.getNumMoves());
        System.out.println("Number of Undoes: " + gameState.getMoveStack().getPopCount());
        if (!gameState.hasUnlimitedLives()) {
            System.out.println("Number of Lives : " + gameState.getNumLives());
        }
        System.out.println("Number of Deaths: " + gameState.getNumDeaths());
        System.out.println();
        System.out.println("Score           : " + gameState.getScore());
    }
}
