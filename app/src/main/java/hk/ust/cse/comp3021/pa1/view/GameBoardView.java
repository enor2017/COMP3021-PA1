package hk.ust.cse.comp3021.pa1.view;

import hk.ust.cse.comp3021.pa1.model.GameBoard;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A read-only view of a {@link GameBoard}.
 */
public class GameBoardView {

    @NotNull
    private final GameBoard gameBoard;

    /**
     * Creates a view of the provided game board.
     *
     * @param gameBoard Game board instance to create a view from.
     */
    public GameBoardView(@NotNull final GameBoard gameBoard) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
    }

    /**
     * Outputs the textual representation of the game board to {@link System#out}.
     *
     * @param useUnicodeChars If {@code true}, outputs the board elements using Unicode characters (as opposed to ASCII
     *                        characters).
     */
    public void output(final boolean useUnicodeChars) {
        for (int r = 0; r < gameBoard.getNumRows(); ++r) {
            for (int c = 0; c < gameBoard.getNumCols(); ++c) {
                final var cell = gameBoard.getCell(r, c);
                final var ch = useUnicodeChars ? cell.toUnicodeChar() : cell.toASCIIChar();

                System.out.print(ch);
            }
            System.out.println();
        }
    }
}
