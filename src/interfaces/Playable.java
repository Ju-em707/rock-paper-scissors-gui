package interfaces;

import models.Shapes;
import java.util.List;

public interface Playable {
    Shapes selectShape();
    void addShape(Shapes shape);
    List<Shapes> getAvailableShapes();
}
