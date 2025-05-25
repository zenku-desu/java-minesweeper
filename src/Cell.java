/**
 * Represents a single cell on the Minesweeper board.
 * Holds state information like whether it's a mine,
 * if it’s revealed, flagged, and how many neighboring mines it has.
 */
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighborMines;

    /**
     * Creates an empty cell without a mine, hidden and unflagged.
     */
    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighborMines = 0;
    }

    /**
     * Marks this cell as a mine.
     */
    public void setMine() {
        this.isMine = true;
    }

    /**
     * Reveals this cell.
     */
    public void reveal() {
        this.isRevealed = true;
    }

    /**
     * Toggles the flagged status of this cell.
     * Flagging marks a cell as suspected to contain a mine.
     */
    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    // Getters

    /**
     * Checks if this cell contains a mine.
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Checks if this cell has been revealed.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Checks if this cell is flagged.
     */
    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Returns the count of neighboring mines.
     */
    public int getNeighborMines() {
        return neighborMines;
    }

    /**
     * Sets the count of neighboring mines for this cell.
     * 
     * @param count Number of neighboring mines.
     */
    public void setNeighborMines(int count) {
        this.neighborMines = count;
    }
}
