package models;

import java.util.List;
import java.util.Random;

// Inheritance
public class AIPlayer extends Player {
    private Random random;

    public AIPlayer() {
        super("ai");
        this.random = new Random();
    }

    // Polymorphism
    @Override
    public Shapes selectShape() {
        List<Shapes> available = getAvailableShapes();
        if (available.isEmpty()) return null;
        return available.get(random.nextInt(available.size()));
    }
}
