package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * The main game board of the game.
 *
 * <p>
 * The top-left hand corner of the game board is the "origin" of the board (0, 0).
 * </p>
 */
public final class GameBoard {

    /**
     * Number of rows in the game board.
     */
    private final int numRows;
    /**
     * Number of columns in the game board.
     */
    private final int numCols;

    /**
     * 2D array representing each cell in the game board.
     */
    @NotNull
    private final Cell[][] board;

    /**
     * The instance of {@link Player} on this game board.
     */
    @NotNull
    private final Player player;

    /**
     * player's position on the given map
     */
    private Position playerPos;

    /**
     * Creates an instance using the provided creation parameters.
     *
     * @param numRows The number of rows in the game board.
     * @param numCols The number of columns in the game board.
     * @param cells   The initial values of cells.
     * @throws IllegalArgumentException if any of the following are true:
     *                                  <ul>
     *                                      <li>{@code numRows} is not equal to {@code cells.length}</li>
     *                                      <li>{@code numCols} is not equal to {@code cells[0].length}</li>
     *                                      <li>There is no player or more than one player in {@code cells}</li>
     *                                      <li>There are no gems in {@code cells}</li>
     *                                      <li>There are some gems which cannot be reached by the player</li>
     *                                  </ul>
     */
    public GameBoard(final int numRows, final int numCols, @NotNull final Cell[][] cells) {
        if (numRows != cells.length || numCols != cells[0].length) {
            throw new IllegalArgumentException("Row/Col numbers not match with given map shape.");
        }
        if (!checkPlayerGemNum(cells)) {
            throw new IllegalArgumentException("The number of players/gems not satisfied.");
        }
        if (!checkAllGemsReachable(cells)) {
            throw new IllegalArgumentException("There are some gems that cannot be reached by the player.");
        }
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = cells;
        // get the (player) entity on position: (playerPos)
        var playerCell = getEntityCell(playerPos);
        var playerOnPlayerPos = (playerCell).getEntity();
        if (playerOnPlayerPos == null) {
            throw new IllegalArgumentException("How come! Wrong player position found???");
        }
        this.player = (Player) playerOnPlayerPos;
        playerCell.setEntity(this.player);
    }

    /**
     * total gem numbers on the given map, useful when checking whether gems can all be reached.
     */
    private int numGems = 0;

    /**
     * check the player and gem numbers in the given map,
     * besides, update this.numGems
     * @param cells given map, with type Cell[][]
     * @return true if only one player and at least one gem, false otherwise.
     */
    private boolean checkPlayerGemNum(@NotNull final Cell[][] cells) {
        int numPlayer = 0;  // the # of players in given map
        int numGem = 0;     // the # of gems in given map
        for(var row : cells) {
            for(var cell : row) {
                // if it is an entityCell, cast it and get Entity
                if (cell instanceof EntityCell) {
                    Entity currEntity = ((EntityCell) cell).getEntity();
                    // if empty entity, move on to next cell
                    if (currEntity == null) {
                        continue;
                    }
                    if (currEntity instanceof Gem) {
                        numGem++;
                    } else if (currEntity instanceof Player) {
                        this.playerPos = cell.getPosition();
                        numPlayer++;
                    }
                }
            }
        }
        this.numGems = numGem;
//        System.out.println("numPlayer = " + numPlayer + ", numGen = " + numGem);
        return (numPlayer == 1) && (numGem > 0);
    }

    /**
     * check if all gems are reachable, by comparing the # of reachable gems
     * and # of total gems
     * @param cells given map
     * @return true if all gems are reachable, false otherwise
     */
    private boolean checkAllGemsReachable(@NotNull final Cell[][] cells) {
        getReachablePos(cells, playerPos);
        int reachableGems = 0;
        for (var pos : reachablePosIsGem.keySet()) {
            if (reachablePosIsGem.get(pos)) {
                reachableGems++;
            }
        }
        return reachableGems == numGems;
    }

    /**
     * A HashMap containing whether a reachable position is gem.
     * 1st indicating reachable position, 2nd is true if position contains a gem
     */
    private HashMap<Position, Boolean> reachablePosIsGem = new HashMap<>();

    /**
     * Use recursion (DFS) to get all reachable positions, and
     * insert into HashMap with whether the position contains a gem
     * @param cells given map
     * @param pos starting position
     */
    private void getReachablePos(@NotNull final Cell[][] cells, @NotNull Position pos) {
        // check if this cell is an entity cell, and even, if holds a gem
        var currCell = cells[pos.row()][pos.col()];
        if (currCell instanceof EntityCell) {
            var entity = ((EntityCell) currCell).getEntity();
            if (entity instanceof Gem) {
                // if it is an entity cell and contains a gem
                reachablePosIsGem.put(pos, true);
            } else {
                // if it is an entity cell but doesn't contain gem
                reachablePosIsGem.put(pos, false);
            }
        } else {
            // if it isn't an entity cell
            reachablePosIsGem.put(pos, false);
        }
        // iterate four directions
        Direction[] directions = Direction.values();
        for(var dire : directions) {
            Position newPos = pos.offsetByOrNull(dire.getOffset(), cells.length, cells[0].length);
            // if new position is valid and has not been visited and is not a wall, call recursion
            if (newPos != null && !reachablePosIsGem.containsKey(newPos) && !isWall(cells, newPos)) {
                getReachablePos(cells, newPos);
            }
        }
    }

    /**
     * given a map and a position, check whether the position is a wall,
     * Assume position is always valid.
     * @param cells given map
     * @param pos given position
     * @return true if the position is a wall, false otherwise
     */
    private boolean isWall(@NotNull final Cell[][] cells, @NotNull Position pos) {
        var cell = cells[pos.row()][pos.col()];
        return (cell instanceof Wall);
    }


    /**
     * Returns the {@link Cell}s of a single row of the game board.
     *
     * @param r Row index.
     * @return 1D array representing the row. The first element in the array corresponds to the leftmost element of the
     * row.
     */
    @NotNull
    public Cell[] getRow(final int r) {
        return board[r];
    }

    /**
     * Returns the {@link Cell}s of a single column of the game board.
     *
     * @param c Column index.
     * @return 1D array representing the column. The first element in the array corresponds to the topmost element of
     * the column.
     */
    @NotNull
    public Cell[] getCol(final int c) {
        Cell[] column = new Cell[numRows];
        for(int i = 0; i < board.length; ++i) {
            column[i] = board[i][c];
        }
        return column;
    }

    /**
     * Returns a single cell of the game board.
     *
     * @param r Row index.
     * @param c Column index.
     * @return The {@link Cell} instance at the specified location.
     */
    @NotNull
    public Cell getCell(final int r, final int c) {
        return board[r][c];
    }

    /**
     * Returns a single cell of the game board.
     *
     * @param position The position object representing the location of the cell.
     * @return The {@link Cell} instance at the specified location.
     */
    @NotNull
    public Cell getCell(@NotNull final Position position) {
        return board[position.row()][position.col()];
    }

    /**
     * Returns an {@link EntityCell} on the game board.
     *
     * <p>
     * This method is a convenience method for getting a cell which is unconditionally known to be an entity cell.
     * </p>
     *
     * @param r Row index.
     * @param c Column index.
     * @return The {@link EntityCell} instance at the specified location.
     * @throws IllegalArgumentException if the cell at the specified position is not an instance of {@link EntityCell}.
     */
    @NotNull
    public EntityCell getEntityCell(final int r, final int c) {
        Cell currCell = getCell(r, c);
        if (!(currCell instanceof EntityCell)) {
            throw new IllegalArgumentException("The specified cell is not an EntityCell.");
        }
        return (EntityCell) currCell;
    }

    /**
     * Returns an {@link EntityCell} on the game board.
     *
     * <p>
     * This method is a convenience method for getting a cell which is unconditionally known to be an entity cell.
     * </p>
     *
     * @param position The position object representing the location of the cell.
     * @return The {@link EntityCell} instance at the specified location.
     * @throws IllegalArgumentException if the cell at the specified position is not an instance of {@link EntityCell}.
     */
    @NotNull
    public EntityCell getEntityCell(@NotNull final Position position) {
        return getEntityCell(position.row(), position.col());
    }

    /**
     * @return The number of rows of this game board.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return The number of columns of this game board.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * @return The player instance.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    private Position getPlayerPos() {
        if (player.getOwner() == null) {
            throw new IllegalCallerException("How come? player has no owner??");
        }
        return player.getOwner().getPosition();
    }

    /**
     * Re-calculate the num of gems on the board.
     * @return The number of gems still present in the game board.
     */
    public int getNumGems() {
        int numGem = 0;     // the # of gems in given map
        for(var row : board) {
            for(var cell : row) {
                // if it is an entityCell, cast it and get Entity
                if (cell instanceof EntityCell) {
                    Entity currEntity = ((EntityCell) cell).getEntity();
                    // if this cell contains a gem
                    if (currEntity instanceof Gem) {
                        numGem++;
                    }
                }
            }
        }
        return numGem;
    }
}
