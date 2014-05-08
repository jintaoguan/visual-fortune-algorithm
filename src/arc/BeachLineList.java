package arc;

import java.awt.*;

import diagram.Point;
import diagram.ParabolaPoint;
import event.EventQueue;
import main.*;

//A ranked list, with x-coordinate from max to min in converted space
public class BeachLineList {

    private BeachLine root;

    public void insert(Point mypoint, double d, EventQueue eventqueue) {
        //if null, set event as root
        if (root == null) {
            root = new BeachLine(mypoint);
            return;
        }
        try {
            ParabolaPoint parabolapoint = new ParabolaPoint(mypoint);
            parabolapoint.init(d);
            root.init(d);
            //insert this site event to beachLineList
            root.insert(parabolapoint, d, eventqueue);
            return;
        } catch (Throwable _ex) {
            System.out.println("*** error: No parabola intersection during arc.BeachLineList.insert()");
        }
    }

    public void checkBounds(DrawingPaper mycanvas, double d) {
        if (root != null) {
            root.init(d);
            root.checkBounds(mycanvas, d);
        }
    }

    public void paint(Graphics g, double d, boolean flag, boolean drawBeach) {
        if (root != null) {
            root.init(d);
            root.paint(g, d, 0.0D, flag, drawBeach);
        }
    }
}
