package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

/**
 * A wall in the game board.
 */
public final class Wall extends Cell {

    /**
     * Creates an instance of {@link Wall} at the given game board position.
     *
     * @param position The position where this cell belongs at.
     */
    public Wall(@NotNull final Position position) {
        super(position);
    }

    @Override
    public char toUnicodeChar() {
        return '\u2588';
    }

    @Override
    public char toASCIIChar() {
        return 'W';
    }
}
