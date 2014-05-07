// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

class CirclePoint extends EventPoint
{

	CirclePoint(double d, double d1, ArcNode arcnode)
	{
		super(d, d1);
		arc = arcnode;
		radius = distance(arcnode);
		x += radius;
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		double d = radius;
		g.drawOval((int)(x - 2D * d), (int)(y - d), (int)(2D * d), (int)(2D * d));
	}

	public void action(MyCanvas mycanvas)
	{
		ArcNode arcnode = arc.prev;
		ArcNode arcnode1 = arc.next;
        //convert to real space
		MyPoint mypoint = new MyPoint(x - radius, y);

        //Update breakpoints involving the arc that is disappearing in this event.  The edges that these breakpoints point to will be finishing.
		arc.completeTrace(mycanvas, mypoint);
		arcnode.completeTrace(mycanvas, mypoint);
		arcnode.startOfTrace = mypoint;

        //Remove the corresponding arc leaf from the arc tree.
        // Delete internal parent node of this arc leaf, and promote sibling leaf or subtree to the parent’s position.
        // Update breakpoints in binaryTree to reflect the new breakpoint that has been created.
        // Note that there will be two breakpoints that are disappearing in this event, and one new breakpoint that is being created.
        // This newly created breakpoint needs to point to one side of a new edge created in the edge vertex list.
        // The other side of the edge should be set to the vertex created by this event.
		arcnode.next = arcnode1;
		arcnode1.prev = arcnode;

        //Check for circle events involving this arc in the immediate left and right arcs of the beach line.  If circle events exist at these nodes, delete them.
		if(arcnode.circlePoint != null)
		{
			mycanvas.queue.remove(arcnode.circlePoint);
			arcnode.circlePoint = null;
		}
		if(arcnode1.circlePoint != null)
		{
			mycanvas.queue.remove(arcnode1.circlePoint);
			arcnode1.circlePoint = null;
		}

        //Check new triples of arcs created by this rearranging of the binaryTree for circle events.
        //If a circle event is detected put it in the priority queue, and put pointers in the leaf nodes that will disappear in that event.
		arcnode.checkCircle(mycanvas.queue);
		arcnode1.checkCircle(mycanvas.queue);
	}

	double radius;

    //the arc (leaf) in the binary tree that will disappear when this circle event occurs
	ArcNode arc;
}
