import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Minesweeper GUI using a custom dark theme and smaller icons.
 */
public class MinesweeperGUI {
    private final Difficulty difficulty;
    private GameController controller;
    private final JButton[][] buttons;
    private final int rows, cols;
    private JFrame frame;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedSeconds;

    private final Color BG_DARK = Color.decode("#096B68");
    private final Color BG_TEAL = Color.decode("#129990");
    private final Color BG_MINT = Color.decode("#90D1CA");
    private final Color FG_LIGHT = Color.decode("#FFFBDE");

    private ImageIcon mineIcon, flagIcon;

    public MinesweeperGUI(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.rows = difficulty.getRows();
        this.cols = difficulty.getCols();
        this.buttons = new JButton[rows][cols];

        loadIcons();
        setupGame();
        buildUI();
    }

    // Load and scale icons from relative paths
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

    // Start or restart the game logic
    private void setupGame() {
        controller = new GameController(difficulty);
        elapsedSeconds = 0;
    }

    // Build the GUI components
    private void buildUI() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Timer and restart button panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_DARK);

        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(FG_LIGHT);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        topPanel.add(timerLabel, BorderLayout.WEST);

        JButton restart = new JButton("Restart");
        restart.setFocusPainted(false);
        restart.setBackground(BG_MINT);
        restart.setForeground(Color.BLACK);
        restart.addActionListener(e -> restartGame());
        topPanel.add(restart, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        // Grid panel
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

                final int row = r, col = c;
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

        frame.add(grid, BorderLayout.CENTER);

        // Timer logic
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            timerLabel.setText("Time: " + elapsedSeconds);
        });
        timer.start();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Restart the game
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
        timerLabel.setText("Time: 0");
        timer.start();
    }

    // Update UI based on board state
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
                    } else {
                        int count = cell.getNeighborMines();
                        btn.setText(count > 0 ? String.valueOf(count) : "");
                    }
                } else if (cell.isFlagged()) {
                    btn.setIcon(flagIcon);
                } else {
                    btn.setIcon(null);
                    btn.setText("");
                }
            }
        }
    }

    // Show result dialog
    private void showResult() {
        timer.stop();
        String msg = controller.isGameWon() ? "You won!" : "You hit a mine!";
        JOptionPane.showMessageDialog(frame, msg);
    }

    // Show difficulty selector on launch
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Difficulty diff = showDifficultyDialog();
            if (diff != null) new MinesweeperGUI(diff);
        });
    }

    // Dialog to choose difficulty
    private static Difficulty showDifficultyDialog() {
        Difficulty[] options = Difficulty.values();
        String[] names = new String[options.length];
        for (int i = 0; i < options.length; i++) names[i] = options[i].name();
        String choice = (String) JOptionPane.showInputDialog(null,
                "Select Difficulty:", "Minesweeper",
                JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        return choice != null ? Difficulty.valueOf(choice) : null;
    }
}
