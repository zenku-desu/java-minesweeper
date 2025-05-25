/**
 * Manages the Minesweeper game logic, including player moves,
 * mine placement, and win/loss detection.
 */
public class GameController {
    private final Board board;
    private final int totalMines;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstMoveMade;

    /**
     * Initializes the game with a given difficulty.
     * Mines are placed after the first move to avoid unlucky starts.
     * 
     * @param difficulty The chosen difficulty level
     */
    public GameController(Difficulty difficulty) {
        this.board = new Board(difficulty.getRows(), difficulty.getCols());
        this.totalMines = difficulty.getMines();
        this.gameOver = false;
        this.gameWon = false;
        this.firstMoveMade = false;
    }

    /**
     * Handles revealing a cell at the specified position.
     * On the first move, mines are placed avoiding the chosen cell.
     * If an empty cell is revealed, adjacent empty cells are revealed recursively.
     * If a mine is revealed, the game ends with a loss.
     * 
     * @param row The row index of the cell to reveal
     * @param col The column index of the cell to reveal
     */
    public void revealCell(int row, int col) {
        if (gameOver || !board.isInBounds(row, col)) return;

        if (!firstMoveMade) {
            board.placeMines(row, col, totalMines);
            firstMoveMade = true;
        }

        Cell cell = board.getCell(row, col);

        if (cell.isRevealed() || cell.isFlagged()) return;

        cell.reveal();

        if (cell.isMine()) {
            gameOver = true;
            gameWon = false;
            revealAllMines();
            return;
        }

        if (cell.getNeighborMines() == 0) {
            revealAdjacentCells(row, col);
        }

        checkWinCondition();
    }

    /**
     * Toggles a flag on a cell, marking it as suspected to contain a mine.
     * Flags can only be placed on unrevealed cells.
     * 
     * @param row The row index of the cell to flag/unflag
     * @param col The column index of the cell to flag/unflag
     */
    public void toggleFlag(int row, int col) {
        if (gameOver || !board.isInBounds(row, col)) return;

        Cell cell = board.getCell(row, col);

        if (!cell.isRevealed()) {
            cell.toggleFlag();
        }
    }

    /**
     * Reveals all mines on the board.
     * Called when the player hits a mine to show all mine locations.
     */
    private void revealAllMines() {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isMine()) {
                    cell.reveal();
                }
            }
        }
    }

    /**
     * Recursively reveals adjacent cells with zero neighboring mines.
     * Stops when cells with neighboring mines are reached.
     * 
     * @param row The row of the current cell
     * @param col The column of the current cell
     */
    private void revealAdjacentCells(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int newRow = row + dr;
                int newCol = col + dc;

                if (board.isInBounds(newRow, newCol)) {
                    Cell neighbor = board.getCell(newRow, newCol);
                    if (!neighbor.isRevealed() && !neighbor.isMine()) {
                        neighbor.reveal();
                        if (neighbor.getNeighborMines() == 0) {
                            revealAdjacentCells(newRow, newCol);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the player has won by revealing all non-mine cells.
     * If so, marks the game as won and over.
     */
    private void checkWinCondition() {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isMine() && !cell.isRevealed()) {
                    return; // Still cells to reveal, game continues
                }
            }
        }
        gameWon = true;
        gameOver = true;
    }

    // Getters for external classes (like the GUI) to query game state

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public Board getBoard() {
        return board;
    }
}
