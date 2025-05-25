import java.util.Random;

/**
 * Represents the Minesweeper game board.
 * Contains a grid of Cells, handles mine placement,
 * and calculates neighboring mine counts.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;
    private boolean minesPlaced = false;

    /**
     * Constructor initializes the board grid with empty cells.
     * 
     * @param rows Number of rows in the board.
     * @param cols Number of columns in the board.
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Cell[rows][cols];

        // Initialize each cell in the grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell();
            }
        }
    }

    /**
     * Places mines randomly on the board, avoiding the first clicked safe cell.
     * Mines are placed only once.
     * 
     * @param safeRow The row of the safe cell (first click).
     * @param safeCol The column of the safe cell (first click).
     * @param totalMines Total number of mines to place.
     */
    public void placeMines(int safeRow, int safeCol, int totalMines) {
        if (minesPlaced) return;

        Random random = new Random();
        int placedMines = 0;

        while (placedMines < totalMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            // Skip if mine already present or if this is the safe cell
            if (!grid[r][c].isMine() && !(r == safeRow && c == safeCol)) {
                grid[r][c].setMine();
                placedMines++;
            }
        }

        calculateNeighborMines();
        minesPlaced = true;
    }

    /**
     * Calculates and sets the number of mines surrounding each non-mine cell.
     */
    private void calculateNeighborMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isMine()) continue;

                int mineCount = 0;

                // Check all adjacent cells (including diagonals)
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;

                        int nr = r + dr;
                        int nc = c + dc;

                        if (isInBounds(nr, nc) && grid[nr][nc].isMine()) {
                            mineCount++;
                        }
                    }
                }

                grid[r][c].setNeighborMines(mineCount);
            }
        }
    }

    /**
     * Returns true if the specified cell coordinates are within the board.
     * 
     * @param row Row index to check.
     * @param col Column index to check.
     * @return True if in bounds, false otherwise.
     */
    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Returns the cell at the specified coordinates.
     * 
     * @param row Row index.
     * @param col Column index.
     * @return The Cell object, or null if out of bounds.
     */
    public Cell getCell(int row, int col) {
        if (isInBounds(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    /**
     * Gets the number of rows of the board.
     * 
     * @return Number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns of the board.
     * 
     * @return Number of columns.
     */
    public int getCols() {
        return cols;
    }
}
