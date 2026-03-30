import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class BattleshipGame extends JFrame implements ActionListener {
    private Board board;
    private JButton[][] cells;
    private JButton playAgainButton;
    private JButton quitButton;
    private JLabel missCounterLabel;
    private JLabel strikeCounterLabel;
    private JLabel totalMissLabel;
    private JLabel totalHitLabel;
    
    private int missCounter;
    private int strikeCounter;
    private int totalMiss;
    private int totalHit;
    
    private static final ImageIcon WAVE_ICON;
    private static final ImageIcon MISS_ICON;
    private static final ImageIcon HIT_ICON;
    
    static {
        WAVE_ICON = createIcon(Color.CYAN, "~");
        MISS_ICON = createIcon(Color.YELLOW, "M");
        HIT_ICON = createIcon(Color.RED, "X");
    }
    
    private static ImageIcon createIcon(Color color, String text) {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, 50, 50);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(color);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (50 - fm.stringWidth(text)) / 2;
        int y = (50 + fm.getAscent()) / 2;
        g2d.drawString(text, x, y);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    public BattleshipGame() {
        setTitle("BattleShip Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        board = new Board();
        cells = new JButton[Board.SIZE][Board.SIZE];
        
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("BATTLESHIP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel boardPanel = new JPanel(new GridLayout(Board.SIZE, Board.SIZE));
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                cells[i][j] = new JButton();
                cells[i][j].setIcon(WAVE_ICON);
                cells[i][j].setBackground(new Color(173, 216, 230));
                cells[i][j].addActionListener(this);
                cells[i][j].setActionCommand(i + "," + j);
                boardPanel.add(cells[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        missCounterLabel = new JLabel("MISS [0-5]: 0");
        strikeCounterLabel = new JLabel("STRIKE [0-3]: 0");
        totalMissLabel = new JLabel("TOTAL MISS: 0");
        totalHitLabel = new JLabel("TOTAL HIT: 0");
        
        missCounterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        strikeCounterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalMissLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalHitLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        statsPanel.add(missCounterLabel);
        statsPanel.add(strikeCounterLabel);
        statsPanel.add(totalMissLabel);
        statsPanel.add(totalHitLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        playAgainButton = new JButton("Play Again");
        quitButton = new JButton("Quit");
        
        playAgainButton.addActionListener(this);
        quitButton.addActionListener(this);
        
        buttonPanel.add(playAgainButton);
        buttonPanel.add(quitButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statsPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        
        startNewGame();
    }
    
    private void startNewGame() {
        board.reset();
        board.placeShips();
        
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                cells[i][j].setIcon(WAVE_ICON);
                cells[i][j].setEnabled(true);
                cells[i][j].setBackground(new Color(173, 216, 230));
            }
        }
        
        missCounter = 0;
        strikeCounter = 0;
        totalMiss = 0;
        totalHit = 0;
        
        updateLabels();
    }
    
    private void updateLabels() {
        missCounterLabel.setText("MISS [0-5]: " + missCounter);
        strikeCounterLabel.setText("STRIKE [0-3]: " + strikeCounter);
        totalMissLabel.setText("TOTAL MISS: " + totalMiss);
        totalHitLabel.setText("TOTAL HIT: " + totalHit);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Play Again")) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to start a new game?", 
                "Play Again", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                startNewGame();
            }
        } else if (command.equals("Quit")) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to quit?", 
                "Quit", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            String[] parts = command.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            if (board.getCellState(row, col) == CellState.BLANK) {
                CellState result = board.fireAt(row, col);
                cells[row][col].setEnabled(false);
                
                if (result == CellState.HIT) {
                    totalHit++;
                    missCounter = 0;
                    cells[row][col].setIcon(HIT_ICON);
                    cells[row][col].setBackground(Color.RED);
                    
                    Ship ship = board.getShip(row, col);
                    if (ship != null && ship.isSunk()) {
                        JOptionPane.showMessageDialog(this, 
                            ship.getName() + " is sunk!", 
                            "Ship Sunk", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        if (board.allShipsSunk()) {
                            JOptionPane.showMessageDialog(this, 
                                "Congratulations! You won!", 
                                "Victory", 
                                JOptionPane.INFORMATION_MESSAGE);
                            promptPlayAgain();
                        }
                    }
                } else if (result == CellState.MISS) {
                    totalMiss++;
                    missCounter++;
                    cells[row][col].setIcon(MISS_ICON);
                    cells[row][col].setBackground(Color.YELLOW);
                    
                    if (missCounter >= 5) {
                        strikeCounter++;
                        missCounter = 0;
                        
                        if (strikeCounter >= 3) {
                            JOptionPane.showMessageDialog(this, 
                                "Game Over! You lost!", 
                                "Defeat", 
                                JOptionPane.INFORMATION_MESSAGE);
                            promptPlayAgain();
                        }
                    }
                }
                
                updateLabels();
            }
        }
    }
    
    private void promptPlayAgain() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Would you like to play again?", 
            "Play Again", 
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BattleshipGame game = new BattleshipGame();
            game.setVisible(true);
        });
    }
}
