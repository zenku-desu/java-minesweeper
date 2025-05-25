import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Minesweeper GUI with custom dark theme and small icons.
 */
public class MinesweeperGUI {
    private final Difficulty difficulty;
    private GameController controller;

    private final int rows, cols;
    private final JButton[][] buttons;

    private JFrame frame;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedSeconds;

    // Custom colors matching the theme
    private final Color BG_DARK = Color.decode("#096B68");
    private final Color BG_TEAL = Color.decode("#129990");
    private final Color BG_MINT = Color.decode("#90D1CA");
    private final Color FG_LIGHT = Color.decode("#FFFBDE");

    private ImageIcon mineIcon;
    private ImageIcon flagIcon;

    /**
     * Constructor initializes variables, loads icons, sets up game and builds UI.
     */
    public MinesweeperGUI(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.rows = difficulty.getRows();
        this.cols = difficulty.getCols();
        this.buttons = new JButton[rows][cols];

        loadIcons();
        setupGame();
        buildUI();
    }

    /**
     * Loads and scales icons from files.
     */
    private void loadIcons() {
        try {
            Image mine = ImageIO.read(new File("img/mine.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            Image flag = ImageIO.read(new File("img/red-flag.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            mineIcon = new ImageIcon(mine);
            flagIcon = new ImageIcon(flag);
        } catch (IOException e) {
            System.err.println("Failed to load icons: " + e.getMessage());
        }
    }

    /**
     * Sets up the game controller and resets timer count.
     */
    private void setupGame() {
        controller = new GameController(difficulty);
        elapsedSeconds = 0;
    }

    /**
     * Builds the GUI components and lays them out.
     */
    private void buildUI() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(createTopPanel(), BorderLayout.NORTH);
        frame.add(createGridPanel(), BorderLayout.CENTER);

        setupTimer();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates the top panel containing the timer and restart button.
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_DARK);

        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(FG_LIGHT);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        topPanel.add(timerLabel, BorderLayout.WEST);

        JButton restartBtn = new JButton("Restart");
        restartBtn.setFocusPainted(false);
        restartBtn.setBackground(BG_MINT);
        restartBtn.setForeground(Color.BLACK);
        restartBtn.addActionListener(e -> restartGame());
        topPanel.add(restartBtn, BorderLayout.EAST);

        return topPanel;
    }

    /**
     * Creates the grid panel containing buttons representing cells.
     */
    private JPanel createGridPanel() {
        JPanel grid = new JPanel(new GridLayout(rows, cols));
        grid.setBackground(BG_DARK);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setBackground(BG_TEAL);
                btn.setForeground(FG_LIGHT);
                btn.setFocusPainted(false);
                btn.setFont(new Font("Monospaced", Font.BOLD, 14));
                btn.setMargin(new Insets(1, 1, 1, 1));

                final int row = r;
                final int col = c;

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (controller.isGameOver()) return;

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            controller.revealCell(row, col);
                            updateBoard();
                            if (controller.isGameOver()) showResult();
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            controller.toggleFlag(row, col);
                            updateBoard();
                        }
                    }
                });

                buttons[r][c] = btn;
                grid.add(btn);
            }
        }
        return grid;
    }

    /**
     * Sets up the timer that updates the elapsed time every second.
     */
    private void setupTimer() {
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            timerLabel.setText("Time: " + elapsedSeconds);
        });
        timer.start();
    }

    /**
     * Restarts the game: resets game logic, timer, and UI buttons.
     */
    private void restartGame() {
        timer.stop();
        setupGame();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = buttons[r][c];
                btn.setText("");
                btn.setIcon(null);
                btn.setBackground(BG_TEAL);
                btn.setEnabled(true);
            }
        }

        elapsedSeconds = 0;
        timerLabel.setText("Time: 0");
        timer.start();
    }

    /**
     * Updates buttons to reflect the current board state.
     */
    private void updateBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = controller.getBoard().getCell(r, c);
                JButton btn = buttons[r][c];

                if (cell.isRevealed()) {
                    btn.setEnabled(false);
                    btn.setBackground(BG_DARK);
                    if (cell.isMine()) {
                        btn.setIcon(mineIcon);
                        btn.setText("");
                    } else {
                        int count = cell.getNeighborMines();
                        btn.setText(count > 0 ? String.valueOf(count) : "");
                        btn.setIcon(null);
                    }
                } else if (cell.isFlagged()) {
                    btn.setIcon(flagIcon);
                    btn.setText("");
                } else {
                    btn.setIcon(null);
                    btn.setText("");
                    btn.setEnabled(true);
                    btn.setBackground(BG_TEAL);
                }
            }
        }
    }

    /**
     * Shows a message dialog indicating whether the player won or lost.
     */
    private void showResult() {
        timer.stop();
        String message = controller.isGameWon() ? "You won!" : "You hit a mine!";
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * Shows the difficulty selector dialog and starts the game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Difficulty difficulty = showDifficultyDialog();
            if (difficulty != null) {
                new MinesweeperGUI(difficulty);
            }
        });
    }

    /**
     * Displays a dialog for the player to select difficulty.
     * 
     * @return The selected difficulty or null if canceled.
     */
    private static Difficulty showDifficultyDialog() {
        Difficulty[] options = Difficulty.values();
        String[] names = new String[options.length];

        for (int i = 0; i < options.length; i++) {
            names[i] = options[i].name();
        }

        String choice = (String) JOptionPane.showInputDialog(null,
                "Select Difficulty:", "Minesweeper",
                JOptionPane.PLAIN_MESSAGE, null, names, names[0]);

        return choice != null ? Difficulty.valueOf(choice) : null;
    }
}
