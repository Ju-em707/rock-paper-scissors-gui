package system;

import models.*;

import java.util.*;

public class GameLogic {
    private List<Shapes> initialShapes;
    private HumanPlayer humanPlayer;
    private AIPlayer aiPlayer;
    private int round;
    private String gamePhase;
    private GameResult lastResult;

    public GameLogic() {
        this.initialShapes = new ArrayList<>();
        this.humanPlayer = new HumanPlayer();
        this.aiPlayer = new AIPlayer();
        this.round = 0;
        this.gamePhase = "initial";
    }

    public List<Shapes> getInitialShapes() { return initialShapes; }
    public HumanPlayer getHumanPlayer() { return humanPlayer; }
    public AIPlayer getAIPlayer() { return aiPlayer; }
    public int getRound() { return round; }
    public String getGamePhase() { return gamePhase; }
    public GameResult getLastResult() { return lastResult; }

    public void setGamePhase(String phase) { this.gamePhase = phase; }

    public void generateInitialShapes(int count) {
        String[] types = {"rock", "paper", "scissors"};
        Random random = new Random();
        initialShapes.clear();

        for (int i = 0; i < count; i++) {
            String type = types[random.nextInt(types.length)];
            initialShapes.add(new Shapes(type, i));
        }
    }

    public void confirmHumanSelection() {
        List<Integer> indices = humanPlayer.getSelectedIndices();
        for (int idx : indices) {
            humanPlayer.addShape(initialShapes.get(idx));
        }

        List<Shapes> remainingShapes = new ArrayList<>();
        for (int i = 0; i < initialShapes.size(); i++) {
            if (!indices.contains(i)) {
                remainingShapes.add(initialShapes.get(i));
            }
        }

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int randIdx = random.nextInt(remainingShapes.size());
            aiPlayer.addShape(remainingShapes.remove(randIdx));
        }

        gamePhase = "playing";
    }

    public String determineWinner(Shapes humanShape, Shapes aiShape) {
        if (humanShape.getType().equals(aiShape.getType())) return "tie";

        Map<String, String> winConditions = new HashMap<>();
        winConditions.put("rock", "scissors");
        winConditions.put("scissors", "paper");
        winConditions.put("paper", "rock");

        return winConditions.get(humanShape.getType())
                            .equals(aiShape.getType())
                            ? "human" : "ai";
    }

    public GameResult playRound(Shapes humanShape, Shapes aiShape) {
        humanShape.markAsUsed();
        aiShape.markAsUsed();

        String result = determineWinner(humanShape, aiShape);

        if (result.equals("human")) humanPlayer.incrementScore();
        else if (result.equals("ai")) aiPlayer.incrementScore();

        round++;
        lastResult = new GameResult(humanShape, aiShape, result);

        if (round == 5 && humanPlayer.getScore() == aiPlayer.getScore()) {
            gamePhase = "suddenDeath";
        } else if (humanPlayer.getScore() == 3 || aiPlayer.getScore() == 3 || (round >= 5 && !result.equalsIgnoreCase("tie"))) {
            gamePhase = "gameOver";
        } else if ((humanPlayer.getScore() > aiPlayer.getScore() || humanPlayer.getScore() < aiPlayer.getScore()) && round >= 5) {
            gamePhase = "gameOver";
        }

        return lastResult;
    }

    public void setUpSuddenDeath() {
        List<Shapes> remaining = new ArrayList<>();
        for (Shapes s : initialShapes) {
            if (!humanPlayer.getShapes().contains(s) && !aiPlayer.getShapes().contains(s)) {
                remaining.add(s);
            }
        }

        if (remaining.size() < 2) {
            generateAdditionalShapes(10);
        }
    }

    public void generateAdditionalShapes(int count) {
        String[] types = {"rock", "paper", "scissors"};
        Random random = new Random();
        int startId = initialShapes.size();

        for (int i = 0; i < count; i++) {
            String type = types[random.nextInt(types.length)];
            initialShapes.add(new Shapes(type, startId + i));
        }
    }

    public void reset() {
        initialShapes.clear();
        humanPlayer.clearShapes();
        humanPlayer.clearSelections();
        humanPlayer.resetScore();
        aiPlayer.clearShapes();
        aiPlayer.resetScore();
        round = 0;
        gamePhase = "initial";
        lastResult = null;
    }

    public record GameResult(Shapes humanShape, Shapes aiShape, String result) {}
}
