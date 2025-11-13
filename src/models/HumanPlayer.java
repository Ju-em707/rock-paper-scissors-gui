package models;

import java.util.ArrayList;
import java.util.List;

// Inheritance
public class HumanPlayer extends Player {
    private List<Integer> selectedIndices;

    public HumanPlayer() {
        super("You");
        this.selectedIndices = new ArrayList<>();
    }

    public List<Integer> getSelectedIndices() { return selectedIndices; }

    public void selectIndex(int index) {
        if (selectedIndices.size() < 5 && !selectedIndices.contains(index)) {
            selectedIndices.add(index);
        }
    }

    public void deselectIndex(int index) {
        selectedIndices.remove(Integer.valueOf(index));
    }

    public void clearSelections() {
        selectedIndices.clear();
    }

    // Polymorphism
    @Override
    public Shapes selectShape() {
        // Handled in GUI
        return null;
    }
}
