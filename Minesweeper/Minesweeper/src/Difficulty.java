/**
 * Enum representing different difficulty levels for the Minesweeper game.
 * Each difficulty defines the board size (rows and columns)
 * and the number of mines placed on the board.
 */
public enum Difficulty {

    // Easy difficulty: 8x8 grid with 10 mines
    EASY(8, 8, 10),
    // Medium difficulty: 16x16 grid with 40 mines
    MEDIUM(16, 16, 40),
    // Hard difficulty: 24x24 grid with 99 mines
    HARD(24, 24, 99);

    // Number of rows for the difficulty level
    private final int rows;
    // Number of columns for the difficulty level
    private final int cols;
    // Number of mines for the difficulty level
    private final int mines;

    /**
     * Constructor to create a difficulty level with specified board dimensions and mine count.
     * @param rows Number of rows in the grid.
     * @param cols Number of columns in the grid.
     * @param mines Number of mines to place.
     */
    Difficulty(int rows, int cols, int mines){
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
    }

    /**
     * Gets the number of rows for this difficulty level.
     * @return Number of rows.
     */
    public int getRows(){
        return rows;
    }

    /**
     * Gets the number of columns for this difficulty level.
     * @return Number of columns.
     */
    public int getCols(){
        return cols;
    }

    /**
     * Gets the number of mines for this difficulty level.
     * @return Number of mines.
     */
    public int getMines(){
        return mines;
    }
}
