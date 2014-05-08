package event;

import java.awt.*;

//A priority queue to keep track of the site events and circle events
public class EventQueue {

    public PointEvent nextRightHandSideEvent;

    public void insert(PointEvent p) {
        if (nextRightHandSideEvent != null)
            nextRightHandSideEvent.insert(p);

        if (p.prev == null)
            nextRightHandSideEvent = p;
    }

    public void remove(PointEvent eventpoint) {
        if (eventpoint.next != null)
            eventpoint.next.prev = eventpoint.prev;

        if (eventpoint.prev != null)
            eventpoint.prev.next = eventpoint.next;
        else nextRightHandSideEvent = eventpoint.next;
    }

    public PointEvent pop() {
        PointEvent eventpoint = nextRightHandSideEvent;
        if (eventpoint != null) {
            nextRightHandSideEvent = nextRightHandSideEvent.next;
            if (nextRightHandSideEvent != null) {
                nextRightHandSideEvent.prev = null;
            }
        }
        return eventpoint;
    }

    public void paint(Graphics g, boolean flag) {
        for (PointEvent eventpoint = nextRightHandSideEvent; eventpoint != null; eventpoint = eventpoint.next) {
            if (flag || !(eventpoint instanceof CircleEvent)) {
                eventpoint.paint(g);
            }
        }

    }
}
