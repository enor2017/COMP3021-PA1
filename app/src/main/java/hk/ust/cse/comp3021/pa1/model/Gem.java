package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

/**
 * An gem entity on a game board.
 *
 * <p>
 * A gem is the item which the player needs to collect in the game.
 * </p>
 */
public final class Gem extends Entity {

    /**
     * Creates an instance of {@link Gem}, initially not present on any {@link EntityCell}.
     */
    public Gem() {
        super();
    }

    /**
     * Creates an instance of {@link Gem}.
     *
     * @param owner The initial {@link EntityCell} the gem belongs to.
     */
    public Gem(@NotNull final EntityCell owner) {
        super(owner);
    }

    @Override
    public char toUnicodeChar() {
        return '\u25C7';
    }

    @Override
    public char toASCIIChar() {
        return '*';
    }
}
