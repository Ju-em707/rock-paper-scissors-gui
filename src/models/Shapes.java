package models;

import interfaces.Displayable;

import javax.swing.*;
import java.awt.*;

// Interface implementation, Encapsulation
public class Shapes implements Displayable {
    private String type;
    private int id;
    private boolean used;

    public Shapes(String type, int id) {
        this.type = type;
        this.id = id;
        this.used = false;
    }

    public String getType() { return type; }
    public int getId() { return id; }
    public boolean isUsed() { return used; }

    public void markAsUsed() { this.used = true; }
    public void markAsUnused() { this.used = false; }

    @Override
    public String getEmoji() {
        switch (type) {
            case "rock": return "\uD83D\uDC8E";
            case "paper": return "\uD83D\uDCC4";
            case "scissors": return "✂️";
            default: return "?";
        }
    }

    @Override
    public Color getColor() {
        switch (type) {
            case "rock": return new Color(100, 100, 100);
            case "paper": return new Color(52, 152, 219);
            case "scissors": return new Color(231, 76, 60);
            default: return Color.GRAY;
        }
    }

}
