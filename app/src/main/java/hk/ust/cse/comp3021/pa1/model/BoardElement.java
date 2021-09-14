package hk.ust.cse.comp3021.pa1.model;

/**
 * Represents a single element on the {@link GameBoard}.
 */
public interface BoardElement {

    /**
     * @return A Unicode character representing this game element on the game board.
     */
    char toUnicodeChar();

    /**
     * @return An ASCII character representing this game element on the game board.
     */
    char toASCIIChar();
}
