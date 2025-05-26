import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RPSGame extends JFrame implements ActionListener {
    private JLabel statusLabel, statsLabel;
    private JButton rockBtn, paperBtn, scissorsBtn;
    private String[] choices = {"Rock", "Paper", "Scissors"};
    private Random random = new Random();
    private String playerName;

    public RPSGame(String playerName) {
        this.playerName = playerName;

        setTitle("Rock-Paper-Scissors Game - Player: " + playerName);
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        rockBtn = new JButton("Rock");
        paperBtn = new JButton("Paper");
        scissorsBtn = new JButton("Scissors");

        rockBtn.addActionListener(this);
        paperBtn.addActionListener(this);
        scissorsBtn.addActionListener(this);

        buttonPanel.add(rockBtn);
        buttonPanel.add(paperBtn);
        buttonPanel.add(scissorsBtn);

        // Status label
        statusLabel = new JLabel("Make your move!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Stats label
        statsLabel = new JLabel(getStatsText(), JLabel.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        add(statusLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(statsLabel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String playerMove = e.getActionCommand();
        String computerMove = choices[random.nextInt(choices.length)];

        String result;
        if (playerMove.equals(computerMove)) {
            result = "Draw";
        } else if (
            (playerMove.equals("Rock") && computerMove.equals("Scissors")) ||
            (playerMove.equals("Paper") && computerMove.equals("Rock")) ||
            (playerMove.equals("Scissors") && computerMove.equals("Paper"))
        ) {
            result = "Win";
        } else {
            result = "Lose";
        }

        // Save result for this player
        DBHelper.saveResult(playerName, result);

        statusLabel.setText("You: " + playerMove + " | Computer: " + computerMove + " → " + result);
        statsLabel.setText(getStatsText());
    }

    private String getStatsText() {
        int wins = DBHelper.getCountByResult(playerName, "Win");
        int losses = DBHelper.getCountByResult(playerName, "Lose");
        int draws = DBHelper.getCountByResult(playerName, "Draw");
        int totalScore = DBHelper.getTotalScore(playerName);
        return String.format("Wins: %d | Losses: %d | Draws: %d | Total Score: %d", wins, losses, draws, totalScore);
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog(null, "Enter your player name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Player name cannot be empty. Exiting.");
            System.exit(0);
        }
        SwingUtilities.invokeLater(() -> new RPSGame(name.trim()));
    }
}
