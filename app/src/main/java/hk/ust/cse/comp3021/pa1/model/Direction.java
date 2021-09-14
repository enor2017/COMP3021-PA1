package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of directions which a player can move in.
 */
public enum Direction {
    /**
     * Up, i.e. moving in decreasing row values.
     */
    UP,

    /**
     * Down, i.e. moving in increasing row values.
     */
    DOWN,
    /**
     * Left, i.e. moving in decreasing column values.
     */
    LEFT,
    /**
     * Right, i.e. moving in increasing column values.
     */
    RIGHT;

    /**
     * @return An instance of {@link PositionOffset} representing a single-cell offset in this direction.
     */
    @NotNull
    public PositionOffset getOffset() {
        return new PositionOffset(getRowOffset(), getColOffset());
    }

    /**
     * @return The single-cell row offset of this direction.
     */
    public int getRowOffset() {
        return switch (this) {
            case UP -> -1;
            case DOWN -> 1;
            case LEFT, RIGHT -> 0;
        };
    }

    /**
     * @return The single-cell column offset of this direction.
     */
    public int getColOffset() {
        return switch (this) {
            case LEFT -> -1;
            case RIGHT -> 1;
            case UP, DOWN -> 0;
        };
    }
}
