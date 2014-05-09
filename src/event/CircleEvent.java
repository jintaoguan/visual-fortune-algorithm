package event;

import java.awt.Graphics;

import diagram.Point;
import arc.*;
import main.*;

public class CircleEvent extends PointEvent {

    private double radius;
    //the beachLine (leaf) in the binary tree that will disappear when this circle event occurs
    public BeachLine beachLine;

    public CircleEvent(double d, double d1, BeachLine arcnode) {
        super(d, d1);
        beachLine = arcnode;
        radius = distance(arcnode);
        x += radius;
    }

    public void paint(Graphics g) {
        super.paint(g);
        double d = radius;
        g.drawOval((int) (x - 2D * d), (int) (y - d), (int) (2D * d), (int) (2D * d));
    }

    public void action(DrawingPaper mycanvas) {
        BeachLine arcnode = beachLine.prev;
        BeachLine arcnode1 = beachLine.next;
        //convert to real space
        Point mypoint = new Point(x - radius, y);

        //Update breakpoints involving the beachLine that is disappearing in this event.  The edges that these breakpoints point to will be finishing.
        beachLine.completeTrace(mycanvas, mypoint);
        arcnode.completeTrace(mycanvas, mypoint);
        arcnode.startOfTrace = mypoint;

        //Remove the corresponding beachLine leaf from the beachLine tree.
        // Delete internal parent node of this beachLine leaf, and promote sibling leaf or subtree to the parent’s position.
        // Update breakpoints in binaryTree to reflect the new breakpoint that has been created.
        // Note that there will be two breakpoints that are disappearing in this event, and one new breakpoint that is being created.
        // This newly created breakpoint needs to point to one side of a new edge created in the edge vertex list.
        // The other side of the edge should be set to the vertex created by this event.
        arcnode.next = arcnode1;
        arcnode1.prev = arcnode;

        //Check for circle events involving this beachLine in the immediate left and right arcs of the beach line.  If circle events exist at these nodes, delete them.
        if (arcnode.circleEvent != null) {
            mycanvas.queue.remove(arcnode.circleEvent);
            arcnode.circleEvent = null;
        }
        if (arcnode1.circleEvent != null) {
            mycanvas.queue.remove(arcnode1.circleEvent);
            arcnode1.circleEvent = null;
        }

        //Check new triples of arcs created by this rearranging of the binaryTree for circle events.
        //If a circle event is detected put it in the priority queue, and put pointers in the leaf nodes that will disappear in that event.
        arcnode.checkCircle(mycanvas.queue);
        arcnode1.checkCircle(mycanvas.queue);
    }

}
