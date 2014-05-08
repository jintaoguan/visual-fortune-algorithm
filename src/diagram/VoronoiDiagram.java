package diagram;

import java.awt.*;
import java.util.Vector;

public class VoronoiDiagram extends Vector {

    public VoronoiDiagram(int width, int height) {
        checkDegenerate();
    }

    //make sure the most left two points have different x-coordinate
    public void checkDegenerate() {
        if (size() > 1) {
            Point min = (Point) elementAt(0), next = min;
            for (int i = 1; i < size(); i++) {
                Object element = elementAt(i);
                if (element instanceof Point) {
                    if (((Point) element).x <= min.x) {
                        next = min;
                        min = (Point) element;
                    } else if (((Point) element).x <= min.x) {
                        next = (Point) element;
                    }
                }
            }

            if (min.x == next.x && min != next) {
                min.x--;
                System.out.println("Moved point: " + next.x + " -> " + min.x);
            }
        }
    }

    public void paint(Graphics g, boolean flag) {
        for (int i = 0; i < size(); i++) {
            if (flag || !(elementAt(i) instanceof Line))
                ((Paintable) elementAt(i)).paint(g);
        }
    }

    public void clear() {
        for (int i = 0; i < size(); i++) {
            if (elementAt(i) instanceof Line)
                removeElementAt(i--);
        }
    }
}
