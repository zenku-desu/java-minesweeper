import java.util.Random;

/**
 * Represents the Minesweeper game board.
 * It contains a grid of Cell objects, manages mine placement,
 * and calculates the number of neighboring mines for each cell.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;
    private boolean minesPlaced = false;

    /**
     * Constructor to create the board with specified dimensions.
     * Mines are NOT placed here.
     *
     * @param rows Number of rows.
     * @param cols Number of columns.
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];

        // Initialize the grid with new empty cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell();
            }
        }
    }

    /**
     * Places mines randomly across the grid, avoiding the safe cell (safeRow, safeCol).
     * Mines will not be placed on the safe cell itself.
     *
     * @param safeRow The row of the first clicked cell (safe cell).
     * @param safeCol The column of the first clicked cell (safe cell).
     * @param totalMines Total mines to randomly place on the board.
     */
    public void placeMines(int safeRow, int safeCol, int totalMines) {
        if (minesPlaced) return; // Only place mines once

        Random random = new Random();
        int minesPlacedCount = 0;

        while (minesPlacedCount < totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            // Avoid placing mine on the safe cell or on an already mined cell
            if (!grid[row][col].isMine() && !(row == safeRow && col == safeCol)) {
                grid[row][col].setMine();
                minesPlacedCount++;
            }
        }

        calculateNeighborMines();
        minesPlaced = true;
    }

    /**
     * Calculates the number of mines adjacent to each cell.
     * This includes checking all 8 neighbors (up, down, left, right, and diagonals).
     * Sets the neighbor mine count in each cell.
     */
    private void calculateNeighborMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isMine()) {
                    continue;
                }

                int mineCount = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;

                        int newRow = r + dr;
                        int newCol = c + dc;

                        if (isInBounds(newRow, newCol) && grid[newRow][newCol].isMine()) {
                            mineCount++;
                        }
                    }
                }
                grid[r][c].setNeighborMines(mineCount);
            }
        }
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell getCell(int row, int col) {
        if (isInBounds(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
