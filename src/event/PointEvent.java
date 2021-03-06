package event;

import diagram.*;
import main.*;

public class PointEvent extends Point {

    public PointEvent prev, next;

    public PointEvent(Point mypoint) {
        super(mypoint);
        index = GLOBLE_INDEX++;
    }

    public PointEvent(double d, double d1) {
        super(d, d1);
        index = GLOBLE_INDEX++;
    }

    //insert event point to the queue, no mater it's point or circle
    //queue is a ranked double linked list
    public void insert(PointEvent eventpoint) {
        if (eventpoint.x > x || eventpoint.x == x && eventpoint.y > y) {
            if (next != null) {
                next.insert(eventpoint);
                return;
            } else {
                next = eventpoint;
                eventpoint.prev = this;
                return;
            }
        }
        if (eventpoint.x != x || eventpoint.y != y || (eventpoint instanceof CircleEvent)) {
            eventpoint.prev = prev;
            eventpoint.next = this;
            if (prev != null) {
                prev.next = eventpoint;
            }
            prev = eventpoint;
            return;
        } else {
            eventpoint.prev = eventpoint;
            System.out.println("Double point ignored: " + eventpoint.toString());
            return;
        }
    }

    public void action(DrawingPaper mycanvas) {
        mycanvas.beachLineList.insert(this, mycanvas.XPos, mycanvas.queue);
    }

}
