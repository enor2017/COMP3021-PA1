package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

/**
 * A mine entity on a game board.
 *
 * <p>
 * A mine, when touched by the {@link Player} entity, will blow up and deduct a life from the player.
 * </p>
 */
public final class Mine extends Entity {

    /**
     * Creates an instance of {@link Mine}, initially not present on any {@link EntityCell}.
     */
    public Mine() {
        super();
    }

    /**
     * Creates an instance of {@link Mine}.
     *
     * @param owner The initial {@link EntityCell} the mine belongs to.
     */
    public Mine(@NotNull final EntityCell owner) {
        super(owner);
    }

    @Override
    public char toUnicodeChar() {
        return '\u26A0';
    }

    @Override
    public char toASCIIChar() {
        return 'X';
    }
}
