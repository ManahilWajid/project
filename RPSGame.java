import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RPSGame extends JFrame implements ActionListener {
   private JLabel movePromptLabel, statusLabel, statsLabel, messageLabel;
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


JLabel movePromptLabel = new JLabel("Make your move!", JLabel.CENTER);
movePromptLabel.setFont(new Font("Arial", Font.BOLD, 18));
movePromptLabel.setForeground(Color.WHITE);
movePromptLabel.setOpaque(true);
movePromptLabel.setBackground(Color.BLACK);

JPanel movePromptPanel = new JPanel(new BorderLayout());
movePromptPanel.setBackground(Color.BLACK); 
movePromptPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
movePromptPanel.add(movePromptLabel, BorderLayout.CENTER);
add(movePromptPanel, BorderLayout.NORTH);

        statusLabel = new JLabel("You: - | Computer: -", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setPreferredSize(new Dimension(0, 40));
        add(statusLabel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        rockBtn = new JButton("Rock");
        paperBtn = new JButton("Paper");
        scissorsBtn = new JButton("Scissors");

        styleButton(rockBtn, new Color(240, 128, 128));      
        styleButton(paperBtn, new Color(135,206,235));   
        styleButton(scissorsBtn, new Color(186,85,211)); 
        buttonPanel.add(rockBtn);
        buttonPanel.add(paperBtn);
        buttonPanel.add(scissorsBtn);
        southPanel.add(buttonPanel);

        statsLabel = new JLabel(getStatsText(), JLabel.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(statsLabel);

        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setPreferredSize(new Dimension(0, 30));
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(messageLabel);

        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(this);
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
         switch (result) {
            case "Win":
                messageLabel.setText("You are WINNER!");
                messageLabel.setForeground(new Color(34,139,34)); 
                break;
            case "Lose":
                messageLabel.setText("You are LOSER!");
                messageLabel.setForeground(new Color(178,34,34));
                break;
            default:
                messageLabel.setText("It's a DRAW!");
                messageLabel.setForeground (new Color(255,69,0)); 
                break;
        }
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
    JTextField nameField = new JTextField(15);

    JLabel nameLabel = new JLabel("Enter player name:");
    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    panel.add(nameLabel);
    panel.add(nameField);
    JButton enterButton = new JButton("Enter");
    JButton cancelButton = new JButton("Cancel");

    // Button colors
    enterButton.setBackground(Color.BLACK); 
    enterButton.setForeground(Color.WHITE);
    enterButton.setFocusPainted(false);

    cancelButton.setBackground(Color.BLACK); 
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setFocusPainted(false);

    JDialog dialog = new JDialog((Frame) null, "Welcome to Rock-Paper-Scissors", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(panel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(enterButton);
    buttonPanel.add(cancelButton);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(null);

    // Button actions
    enterButton.addActionListener(e -> {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Name cannot be empty!");
        } else {
            dialog.dispose();
            SwingUtilities.invokeLater(() -> new RPSGame(name));
        }
    });

    cancelButton.addActionListener(e -> {
        dialog.dispose();
        System.exit(0);
    });

    dialog.setVisible(true);
}

}







