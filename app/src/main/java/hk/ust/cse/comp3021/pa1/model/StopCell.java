package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link EntityCell} which stops the {@link Player} from sliding further.
 */
public final class StopCell extends EntityCell {

    /**
     * Creates an instance of {@link StopCell} at the given game board position.
     *
     * @param position The position where this cell belongs at.
     */
    public StopCell(@NotNull final Position position) {
        super(position);
    }

    /**
     * Creates an instance of {@link StopCell} at the given game board position.
     *
     * @param position      The position where this cell belongs at.
     * @param initialEntity The initial entity present in this cell.
     */
    public StopCell(@NotNull final Position position, @Nullable final Entity initialEntity) {
        super(position, initialEntity);
    }

    /**
     * Same as {@link EntityCell#setEntity(Entity)}, with additional checking.
     *
     * @throws IllegalArgumentException if the entity is not {@code null} and not an instance of {@link Player}.
     */
    @Nullable
    @Override
    public Entity setEntity(@Nullable final Entity newEntity) {
        if (newEntity != null && !(newEntity instanceof Player)) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        // below same as EntityCell.setEntity()
        var prevEntity = this.entity;
        // unset the owner of previous entity
        if (prevEntity != null) {
            prevEntity.setOwner(null);
        }
        if (newEntity != null) {
            // unset the EntityCell.entity of the previous cell owning newEntity.
            var prevOwner = newEntity.getOwner();
            if (prevOwner != null) {
                prevOwner.setEntity(null);
            }
            // set the new owner of newEntity to this instance
            newEntity.setOwner(this);
        }
        this.entity = newEntity;
        return prevEntity;
    }

    /**
     * Replaces the player on this {@link StopCell} with {@code newPlayer}.
     *
     * <p>
     * This method should perform the same action as {@link EntityCell#setEntity(Entity)}, except that the parameter and
     * return value are both changed to {@link Player}.
     * </p>
     *
     * @param newPlayer The new player of this cell.
     * @return The previous player occupying this cell, or {@code null} if no player was previously occupying this cell.
     */
    @Nullable
    public Player setPlayer(@Nullable final Player newPlayer) {
        var prevEntity = this.setEntity(newPlayer);
        return prevEntity == null ? null : (Player) prevEntity;
    }

    @Override
    public char toUnicodeChar() {
        return getEntity() != null ? getEntity().toUnicodeChar() : '\u25A1';
    }

    @Override
    public char toASCIIChar() {
        return getEntity() != null ? getEntity().toASCIIChar() : '#';
    }
}
