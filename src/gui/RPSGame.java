package gui;

import models.HumanPlayer;
import models.Shapes;
import system.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RPSGame extends JFrame {
    private GameLogic game;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel selectionPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;

    private JButton playAgainButton;
    private JLabel scoreLabel;
    private JLabel roundLabel;
    private List<JButton> shapeButtons;
    private List<JButton> playerCardButtons;
    private JButton confirmButton;

    public RPSGame() {
        game = new GameLogic();
        shapeButtons = new ArrayList<>();
        playerCardButtons = new ArrayList<>();

        setTitle("Rock Paper Scissors - Ultimate Edition");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setIconImage(new ImageIcon("images/RPS-logo.png").getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        startGame();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(20, 20, 30));

        createSelectionPanel();
        createGamePanel();
        createResultPanel();

        mainPanel.add(selectionPanel, "selection");
        mainPanel.add(gamePanel, "game");
        mainPanel.add(resultPanel, "result");

        add(mainPanel);
    }

    private void createSelectionPanel() {
        selectionPanel = new JPanel(new BorderLayout(20, 20));
        selectionPanel.setBackground(new Color(20, 20, 30));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("✨ ROCK PAPER SCISSORS ✨");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel instructionLabel = new JLabel("<html><center>Select 5 cards from the 20 shapes below<br>You won't see what you're choosing!</center></html>");
        instructionLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        selectionPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(4, 5, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        for (int i = 0; i < 20; i++) {
            JButton btn = createStyledButton("?", new Color(50, 50, 70));
            final int index = i;
            btn.addActionListener(e -> handleShapeSelection(index, btn));
            shapeButtons.add(btn);
            gridPanel.add(btn);
        }

        selectionPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);

        confirmButton = createStyledButton("CONFIRM SELECTION (0/5)", new Color(46, 204, 113));
        confirmButton.setFont(new Font("Monospaced", Font.BOLD, 15));
        confirmButton.setPreferredSize(new Dimension(300, 60));
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmSelection());
        bottomPanel.add(confirmButton);

        selectionPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout(20, 20));
        gamePanel.setBackground(new Color(20, 20, 30));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        scoreLabel = new JLabel("YOU: 0  |  AI: 0");
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        scoreLabel.setForeground(new Color(255, 215, 0));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(scoreLabel, BorderLayout.NORTH);

        roundLabel = new JLabel("Round 1/5");
        roundLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roundLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(roundLabel, BorderLayout.CENTER);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        JLabel selectLabel = new JLabel("Select Your Card:");
        selectLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        selectLabel.setForeground(Color.WHITE);
        selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(selectLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        for (int i = 0; i < 5; i++) {
            JButton btn = createLargeCardButton("", Color.GRAY);
            final int index = i;
            btn.addActionListener(e -> {
                btn.setBackground(btn.getBackground().darker().darker());
                playCard(index);
            });
            playerCardButtons.add(btn);
            cardsPanel.add(btn);
        }

        centerPanel.add(cardsPanel, BorderLayout.CENTER);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void createResultPanel() {
        resultPanel = new JPanel(new BorderLayout(20, 20));
        resultPanel.setBackground(new Color(20, 20, 30));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.brighter(), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JButton createLargeCardButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 48));
        btn.setPreferredSize(new Dimension(120, 160));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.brighter(), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void startGame() {
        game.reset();
        game.generateInitialShapes(20);
        game.setGamePhase("selection");

        for (JButton btn : shapeButtons) {
            btn.setText("?");
            btn.setBackground(new Color(50, 50, 70));
            btn.setEnabled(true);
        }

        confirmButton.setText("CONFIRM SELECTION (0/5)");
        confirmButton.setEnabled(false);

        cardLayout.show(mainPanel, "selection");
    }

    private void handleShapeSelection(int index, JButton btn) {
        HumanPlayer human = game.getHumanPlayer();

        if (human.getSelectedIndices().contains(index)) {
            human.deselectIndex(index);
            btn.setBackground(new Color(50, 50, 70));
        } else {
            if (human.getSelectedIndices().size() < 5) {
                human.selectIndex(index);
                btn.setBackground(Color.ORANGE);
            }
        }

        int selected = human.getSelectedIndices().size();
        confirmButton.setText("CONFIRM SELECTION (" + selected + "/5)");
        confirmButton.setEnabled(selected == 5);
    }

    private void confirmSelection() {
        game.confirmHumanSelection();

        List<Shapes> humanShapes = game.getHumanPlayer().getShapes();
        for (int i = 0; i < 5; i++) {
            Shapes shape = humanShapes.get(i);
            JButton btn = playerCardButtons.get(i);
            btn.setText(shape.getEmoji());
            btn.setBackground(Color.ORANGE);
            btn.setEnabled(true);
        }

        updateGameUI();
        cardLayout.show(mainPanel, "game");
    }

    private void playCard(int index) {
        Shapes humanShape = game.getHumanPlayer().getShapes().get(index);
        if (humanShape.isUsed()) return;

        Shapes aiShape = game.getAIPlayer().selectShape();
        if (aiShape == null) return;

        for (JButton btn : playerCardButtons) {
            btn.setEnabled(false);
        }

        showBattleResult(humanShape, aiShape, index);
    }

    private void showBattleResult(Shapes humanShape, Shapes aiShape, int cardIndex) {
        JDialog dialog = new JDialog(this, "Round Result", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(20, 20));
        dialog.getContentPane().setBackground(new Color(20, 20, 30));

        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerPanel.setOpaque(false);
        JLabel playerLabel = new JLabel("YOU: " + humanShape.getEmoji());
        playerLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        playerLabel.setForeground(Color.WHITE);
        playerPanel.add(playerLabel);
        contentPanel.add(playerPanel);

        JPanel vsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        vsPanel.setOpaque(false);
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        vsLabel.setForeground(new Color(255, 215, 0));
        vsPanel.add(vsLabel);
        contentPanel.add(vsPanel);

        JPanel aiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aiPanel.setOpaque(false);
        JLabel aiLabel = new JLabel("AI: " + aiShape.getEmoji());
        aiLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        aiLabel.setForeground(Color.WHITE);
        aiPanel.add(aiLabel);
        contentPanel.add(aiPanel);

        dialog.add(contentPanel, BorderLayout.CENTER);

        GameLogic.GameResult result = game.playRound(humanShape, aiShape);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

        String resultText = "";
        Color resultColor = Color.WHITE;
        if (result.result().equals("human")) {
            resultText = "🎉 YOU WIN THIS ROUND! 🎉";
            resultColor = new Color(46, 204, 113);
        } else if (result.result().equals("ai")) {
            resultText = "💀 AI WINS THIS ROUND 💀";
            resultColor = new Color(231, 76, 60);
        } else {
            resultText = "🤝 IT'S A TIE! 🤝";
            resultColor = new Color(241, 196, 15);
        }

        JLabel resultLabel = new JLabel(resultText);
        resultLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        resultLabel.setForeground(resultColor);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(resultLabel, BorderLayout.NORTH);

        JButton continueBtn = createStyledButton("CONTINUE", new Color(52, 152, 219));
        continueBtn.setPreferredSize(new Dimension(200, 50));
        continueBtn.addActionListener(e -> {
            dialog.dispose();
            afterRound(cardIndex);
            continueBtn.setBackground(continueBtn.getBackground().darker().darker());
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(continueBtn);
        bottomPanel.add(btnPanel, BorderLayout.CENTER);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        Timer timer = new Timer(2000, e -> {
            if (dialog.isVisible()) {
                continueBtn.doClick();
            }
        });
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    private void afterRound(int cardIndex) {
        playerCardButtons.get(cardIndex).setEnabled(false);
        playerCardButtons.get(cardIndex).setBackground(Color.DARK_GRAY);

        updateGameUI();

        if (game.getGamePhase().equals("gameOver")) {
            showGameOver();
        } else if (game.getGamePhase().equals("suddenDeath")) {
            showSuddenDeath();
        } else {
            List<Shapes> humanShapes = game.getHumanPlayer().getShapes();
            for (int i = 0; i < 5; i++) {
                if (!humanShapes.get(i).isUsed()) {
                    playerCardButtons.get(i).setEnabled(true);
                }
            }
        }
    }

    private void updateGameUI() {
        int humanScore = game.getHumanPlayer().getScore();
        int aiScore = game.getAIPlayer().getScore();
        scoreLabel.setText("YOU: " + humanScore + "  |  AI: " + aiScore);

        int currentRound = game.getRound() + 1;
        if (game.getGamePhase().equals("suddenDeath")) {
            roundLabel.setText("⚡ SUDDEN DEATH ⚡");
        } else if (game.getRound() < 5) {
            roundLabel.setText("Round " + currentRound + "/5");
        } else {
            roundLabel.setText("Game Complete");
        }
    }

    private void showSuddenDeath() {
        game.setUpSuddenDeath();

        JDialog dialog = new JDialog(this, "Sudden Death!", true);
        dialog.setSize(500, 305);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(20, 20));
        dialog.getContentPane().setBackground(new Color(20, 20, 30));

        JLabel titleLabel = new JLabel("⚡ SUDDEN DEATH ⚡");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JLabel msgLabel = new JLabel("<html><center>Next point wins!" +
                                    "</center></html>");
        msgLabel.setFont(new Font("Monospaced", Font.PLAIN, 17));
        msgLabel.setForeground(Color.WHITE);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(msgLabel, BorderLayout.CENTER);

        JButton okBtn = createStyledButton("LET'S GO!", new Color(231, 76, 70));
        okBtn.setPreferredSize(new Dimension(200, 50));
        okBtn.addActionListener(_ -> {
            dialog.dispose();
            playSuddenDeathRound();
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void playSuddenDeathRound() {
        List<Shapes> remaining = new ArrayList<>();
        for (Shapes s : game.getInitialShapes()) {
            if (!game.getHumanPlayer().getShapes().contains(s) &&
                    !game.getAIPlayer().getShapes().contains(s)) {
                remaining.add(s);
            }
        }

        if (remaining.size() < 2) {
            game.generateAdditionalShapes(10);
            remaining = new ArrayList<>();
            for (Shapes s : game.getInitialShapes()) {
                if (!game.getHumanPlayer().getShapes().contains(s) &&
                        !game.getAIPlayer().getShapes().contains(s)) {
                    remaining.add(s);
                }
            }
        }

        // Random selection for sudden death
        Random random = new Random();
        Shapes humanShape = remaining.remove(random.nextInt(remaining.size()));
        Shapes aiShape = remaining.remove(random.nextInt(remaining.size()));

        game.getHumanPlayer().addShape(humanShape);
        game.getAIPlayer().addShape(aiShape);

        showBattleResult(humanShape, aiShape, 0);
    }

    private void showGameOver() {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout(20, 20));

        int humanScore = game.getHumanPlayer().getScore();
        boolean humanWon = humanScore == 3 || humanScore > game.getAIPlayer().getScore();

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 40, 50));

        // Trophy/Result
        JLabel trophyLabel = new JLabel(humanWon ? "🏆" : "💀");
        trophyLabel.setFont(new Font("Monospaced", Font.PLAIN, 120));
        trophyLabel.setForeground(humanWon ? Color.YELLOW : Color.BLACK);
        trophyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(trophyLabel);

        // Winner text
        JLabel winnerLabel = new JLabel(humanWon ? "YOU WIN!" : "AI WINS!");
        winnerLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        winnerLabel.setForeground(humanWon ? new Color(46, 204, 113) : new Color(231, 76, 60));
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(winnerLabel);

        // Final score
        JLabel finalScoreLabel = new JLabel("Final Score: " + game.getHumanPlayer().getScore() + " - " + game.getAIPlayer().getScore());
        finalScoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 28));
        finalScoreLabel.setForeground(Color.WHITE);
        finalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(finalScoreLabel);

        resultPanel.add(centerPanel, BorderLayout.CENTER);

        // Play again button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        playAgainButton = createStyledButton("PLAY AGAIN", new Color(52, 152, 219));
        playAgainButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        playAgainButton.setPreferredSize(new Dimension(250, 70));
        playAgainButton.addActionListener(e -> {
            startGame();
        });
        bottomPanel.add(playAgainButton);

        JButton exitButton = createStyledButton("EXIT", new Color(231, 76, 60));
        exitButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        exitButton.setPreferredSize(new Dimension(250, 70));
        exitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitButton);

        resultPanel.add(bottomPanel, BorderLayout.SOUTH);

        resultPanel.revalidate();
        resultPanel.repaint();

        cardLayout.show(mainPanel, "result");
    }

    private ImageIcon loadIcon(String resourcePath, int w, int h) {
        java.net.URL res = getClass().getResource(resourcePath);
        if (res == null) return null;
        ImageIcon icon = new ImageIcon(res);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RPSGame game = new RPSGame();
            game.setVisible(true);
        });
    }
}
