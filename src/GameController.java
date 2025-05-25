/**
 * The GameController class manages the state and logic of the Minesweeper game.
 * It handles player moves, game rules, and win/loss conditions.
 */
public class GameController {
    private final Board board;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstMoveMade = false;
    private final int totalMines;

    /**
     * Constructor to initialize the game controller with a difficulty setting.
     * It creates a new board based on the difficulty's size.
     * Mines will be placed after the first player move.
     *
     * @param difficulty The difficulty level chosen by the player
     */
    public GameController(Difficulty difficulty) {
        this.board = new Board(difficulty.getRows(), difficulty.getCols());
        this.gameOver = false;
        this.gameWon = false;
        this.totalMines = difficulty.getMines();
    }

    /**
     * Reveals the cell at the specified row and column.
     * If this is the first move, mines are placed avoiding this cell.
     * If the cell is empty (no neighboring mines), it recursively reveals adjacent cells.
     * If a mine is revealed, the game ends with a loss.
     *
     * @param row The row index of the cell to reveal
     * @param col The column index of the cell to reveal
     */
    public void revealCell(int row, int col) {
        if (gameOver || !board.isInBounds(row, col)) {
            return;
        }

        if (!firstMoveMade) {
            board.placeMines(row, col, totalMines);
            firstMoveMade = true;
        }

        Cell cell = board.getCell(row, col);

        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        cell.reveal();

        if (cell.getNeighborMines() == 0) {
            revealAdjacentCells(row, col);
        }

        if (cell.isMine()) {
            gameOver = true;
            gameWon = false;
            revealAllMines();
            return;
        }

        checkWinCondition();
    }

    /**
     * Toggles a flag on the specified cell.
     *
     * @param row The row index of the cell to toggle flag on
     * @param col The column index of the cell to toggle flag on
     */
    public void toggleFlag(int row, int col) {
        if (gameOver || !board.isInBounds(row, col)) {
            return;
        }

        Cell cell = board.getCell(row, col);

        if (cell.isRevealed()) {
            return;
        }

        cell.toggleFlag();
    }

    private void revealAdjacentCells(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;

                if (board.isInBounds(newRow, newCol) && !(dr == 0 && dc == 0)) {
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

    private void checkWinCondition() {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isMine() && !cell.isRevealed()) {
                    return;
                }
            }
        }
        gameWon = true;
        gameOver = true;
    }

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
