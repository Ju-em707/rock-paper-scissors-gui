package models;

import interfaces.Playable;
import interfaces.Scorable;

import java.util.ArrayList;
import java.util.List;

// Inheritance, Implementation, Abstraction
public abstract class Player implements Playable, Scorable {
    protected String name;
    protected List<Shapes> shapes;
    protected int score;

    public Player(String name) {
        this.name = name;
        this.shapes = new ArrayList<>();
        this.score = 0;
    }

    public String getName() { return name; }
    public List<Shapes> getShapes() { return shapes; }
    @Override public int getScore() { return score; }

    @Override
    public void addShape(Shapes shape) {
        shapes.add(shape);
    }

    @Override public void incrementScore() { score++; }
    @Override public void resetScore() { score = 0; }
    public void clearShapes() { shapes.clear(); }

    public List<Shapes> getAvailableShapes() {
        List<Shapes> available = new ArrayList<>();
        for (Shapes s : shapes) {
            if (!s.isUsed()) available.add(s);
        }
        return available;
    }

    // Abstraction
    public abstract Shapes selectShape();
}
