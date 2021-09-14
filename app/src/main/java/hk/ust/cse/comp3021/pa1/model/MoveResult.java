package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * The result after moving a player.
 */
public abstract sealed class MoveResult {

    /**
     * The {@link Position} of the player after moving.
     */
    @NotNull
    public final Position newPosition;

    /**
     * Represents a valid move.
     */
    public static sealed class Valid extends MoveResult {

        /**
         * The original position of the player before the move.
         */
        @NotNull
        public final Position origPosition;

        /**
         * Creates an instance of {@link MoveResult.Valid}, indicating that the move is valid.
         *
         * @param newPosition  The new {@link Position} of the player after moving.
         * @param origPosition The original {@link Position} of the player before moving.
         */
        private Valid(@NotNull final Position newPosition, @NotNull final Position origPosition) {
            super(newPosition);
            this.origPosition = origPosition;
        }

        /**
         * Represents a valid move, and the player is alive after making the move.
         */
        public static final class Alive extends Valid {

            /**
             * List of positions representing the location of {@link Gem} collected in this move.
             */
            @NotNull
            public final List<Position> collectedGems;
            /**
             * List of positions representing the location of {@link ExtraLife} collected in this move.
             */
            @NotNull
            public final List<Position> collectedExtraLives;

            /**
             * Creates an instance of {@link MoveResult.Valid.Alive}, indicating that the move is valid and the player
             * is still alive after the move.
             *
             * <p>
             * {@link Alive#collectedGems} and {@link Alive#collectedExtraLives} will be initialized to an empty list
             * using this constructor.
             * </p>
             *
             * @param newPosition  The new {@link Position} of the player after the move.
             * @param origPosition The original {@link Position} of the player before the move.
             */
            public Alive(@NotNull final Position newPosition, @NotNull final Position origPosition) {
                this(newPosition, origPosition, Collections.emptyList(), Collections.emptyList());
            }

            /**
             * Creates an instance of {@link MoveResult.Valid.Alive}, indicating that the move is valid and the player
             * is still alive after the move.
             *
             * @param newPosition         The new {@link Position} of the player after the move.
             * @param origPosition        The original {@link Position} of the player before the move.
             * @param collectedGems       The list of positions of all gems collected in this move.
             * @param collectedExtraLives The list of positions of all extra lives collected in this move.
             */
            public Alive(@NotNull final Position newPosition,
                         @NotNull final Position origPosition,
                         @NotNull final List<Position> collectedGems,
                         @NotNull final List<Position> collectedExtraLives
            ) {
                super(newPosition, origPosition);
                this.collectedGems = collectedGems;
                this.collectedExtraLives = collectedExtraLives;
            }
        }

        /**
         * Represents a valid move, but the player has died after making the move.
         *
         * <p>
         * A valid-but-dead move occurs when a player is able to move one or more tiles, but hits a mine while
         * moving in the direction.
         * </p>
         */
        public static final class Dead extends Valid {

            /**
             * The {@link Position} of the {@link Mine} which the player encounters and dies from.
             */
            @NotNull
            public final Position minePosition;

            /**
             * Creates an instance of {@link MoveResult.Valid.Dead}, indicating that the move was valid, but results in
             * the player dying.
             *
             * @param newPosition  The {@link Position} of the player after this move. This should be the same as the
             *                     original position before the move.
             * @param minePosition The {@link Position} of the mine which the player encounters during the move (and
             *                     dies from).
             */
            public Dead(@NotNull final Position newPosition, @NotNull final Position minePosition) {
                super(newPosition, newPosition);    // newPosition is the same as originalPosition

                this.minePosition = minePosition;
            }
        }
    }

    /**
     * Represents an invalid move.
     *
     * <p>
     * An invalid move occurs when a player has made a move which immediately hits a wall or goes out of the game board
     * bounds.
     * </p>
     */
    public static final class Invalid extends MoveResult {

        /**
         * Creates an instance of {@link MoveResult.Invalid}, indicating the move was invalid.
         *
         * @param newPosition The {@link Position} of the player after this move. This should be the same as the
         *                    original position before the move.
         */
        public Invalid(@NotNull final Position newPosition) {
            super(newPosition);
        }
    }

    /**
     * Creates an instance of {@link MoveResult}.
     *
     * @param newPosition The new {@link Position} of the player after making the move.
     */
    private MoveResult(@NotNull final Position newPosition) {
        this.newPosition = newPosition;
    }
}

