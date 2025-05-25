    /**
     * Represents a single cell on the Minesweeper board.
     * Each cell can be a mine or empty, can be revealed or hidden,
     * and can be flagged by the player.
     */

public class Cell {
    private int neighborMines;
    private boolean isRevealed;
    private boolean isFlagged;
    private boolean isMine;

    /**
     * Constructor initializes the cell as empty, unrevealed, and unflagged.
     */

    public Cell() {
        this.neighborMines = 0;
        this.isRevealed = false;
        this.isFlagged = false;
        this.isMine = false;
    }

    /**
     * Checks if this cell contains a mine.
     * @return true if cell is a mine, false otherwise.
     */

    public boolean isMine() {
        return isMine;
    }

    /**
     * Checks if this cell is flagged by the player.
     * @return true if flagged, false otherwise.
     */

    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Checks if this cell has been revealed.
     * @return true if revealed, false otherwise.
     */

    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Reveals the cell, showing its contents (mine or number).
     */

    public void reveal() {
        this.isRevealed = true;
    }

    /**
     * Sets this cell to contain a mine.
     */

    public void setMine() {
        this.isMine = true;
    }

    /**
     * Toggles the flagged status of this cell.
     * Flags are used to mark suspected mines.
     */

    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    /**
     * Sets the count of mines in neighboring cells.
     * @param count Number of neighboring mines.
     */

    public void setNeighborMines(int count) {
        this.neighborMines = count;
    }

    /**
     * Gets the number of neighboring mines.
     * @return Count of neighboring mines.
     */

    public int getNeighborMines() {
        return neighborMines;
    }
}
