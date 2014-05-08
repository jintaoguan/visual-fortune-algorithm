
import java.awt.*;

class ArcNode extends ParabolaPoint
{
	public ArcNode (Point mypoint)
	{
		super(mypoint);
	}

    /**
     * A circle event is calculated by comparing the breakpoints on either side of the arc.
     * If there are one or no breakpoints, no circle event will occur.
     * If there are two breakpoints, then a test needs to be made to see if these breakpoints will converge.
     * If they will converge, this means that this is a potential circle event and needs to be entered into the priority queue.
     */
	public void checkCircle (EventQueue eventqueue)
	{
		if(prev != null && next != null)
		{
			circlePoint = calculateCenter(next, this, prev);
			if(circlePoint != null)
				eventqueue.insert(circlePoint);
		}
	}

	public void removeCircle (EventQueue eventqueue)
	{
		if(circlePoint != null)
		{
			eventqueue.remove(circlePoint);
			circlePoint = null;
		}
	}

	public void completeTrace (DrawingPaper mycanvas, Point mypoint)
	{
		if(startOfTrace != null)
		{
			mycanvas.Voronoi.addElement(new Line(startOfTrace, mypoint));
			startOfTrace = null;
		}
	}

	public void checkBounds (DrawingPaper mycanvas, double d)
	{
		if(next != null)
		{
			next.init(d);
			if(d > next.x && d > x && startOfTrace != null)
			{
				try
				{
					double ad[] = solveQuadratic(a - next.a, b - next.b, c - next.c);
					double d1 = ad[0];
					double d2 = d - getYCoordinateOfParabolaByX(d1);
					Rectangle rectangle = mycanvas.getBounds();
					if(d2 < startOfTrace.x && d2 < 0.0D || d1 < 0.0D || d2 >= (double)rectangle.width || d1 >= (double)rectangle.height)
						completeTrace(mycanvas, new Point(d2, d1));
				}
				catch(Throwable _ex)
				{
					System.out.println("*** exception");
				}
			}
			next.checkBounds(mycanvas, d);
		}
	}

    //When a new arc appears on the beach line it will divide an existing arc into two segments
	public void insert (ParabolaPoint parabolapoint, double sline, EventQueue eventqueue)
		throws Throwable
	{
		boolean split = true;
		if(next != null)
		{
			next.init(sline);
			if(sline > next.x && sline > x)
			{
                //get the two intersection x coordinates
				double xs[] = solveQuadratic(a - next.a, b - next.b, c - next.c);
				if(xs[0] <= parabolapoint.realX() && xs[0] != xs[1])
					split = false;
			}
			else
			{
				split = false;
			}
		}

		if(split)
		{
            //delete circle events involving this arc from the queue
			removeCircle(eventqueue);

            //add this arc to the arc tree
			ArcNode arcnode = new ArcNode(parabolapoint);
			arcnode.next = new ArcNode(this);
			arcnode.prev = this;
			arcnode.next.next = next;
			arcnode.next.prev = arcnode;

			if(next != null)
				next.prev = arcnode.next;

			next = arcnode;

            //Check for circle events caused by this new site
            //Note that we don’t have to check the triple of leaves where the new site is the middle leaf, because the breakpoints can’t converge.
            // Instead, check the triples where this new site is the far left and far right arc.
            // If the breakpoints are converging, calculate the circle event priority and place it in the queue.
            // Make a pointer from the middle leaf (the leaf that will disappear in the circle event) to the event in the queue.
			checkCircle(eventqueue);
			next.next.checkCircle(eventqueue);

			next.next.startOfTrace = startOfTrace;
			startOfTrace = new Point(sline - getYCoordinateOfParabolaByX(parabolapoint.y), parabolapoint.y);
			next.startOfTrace = new Point(sline - getYCoordinateOfParabolaByX(parabolapoint.y), parabolapoint.y);
		}
		else
		{
			next.insert(parabolapoint, sline, eventqueue);
		}
	}

	public void paint (Graphics g, double d, double d1, boolean flag, boolean drawBeach)
	{
		double d2 = g.getClipBounds().height;
		ArcNode arcnode = next;
		if(arcnode != null)
		{
			arcnode.init(d);
		}
		if(d == x)
		{
			double d3 = arcnode != null ? d - arcnode.getYCoordinateOfParabolaByX(y) : 0.0D;
			if(drawBeach)
				g.drawLine((int)d3, (int)y, (int)d, (int)y);
			d2 = y;
		}
		else
		{
			if(arcnode != null)  
			{
				if(d == arcnode.x)
				{
					d2 = arcnode.y;
				}
				else
				{
					try
					{
						double ad[] = solveQuadratic(a - arcnode.a, b - arcnode.b, c - arcnode.c);
						d2 = ad[0];
					}
					catch(Throwable _ex)
					{
						d2 = d1;
						System.out.println("*** error: No parabola intersection during ArcNode.paint() - SLine: " + d + ", " + toString() + " " + arcnode.toString());
					}
				}
			}

			if(drawBeach)
			{
				int i = 1;
				double d4 = 0.0D;
				// paint every points of the parabola in the canvas. 
				for(double d5 = d1; d5 < Math.min(Math.max(0.0D, d2), g.getClipBounds().height); d5 += i)
				{
				    // convert the coordinate of the parabola to the coordinate of the canvas
					double d6 = d - getYCoordinateOfParabolaByX(d5);
					
					// make sure the coordinate is inside this canvas.
					if(d5 > d1 && (d4 >= 0.0D || d6 >= 0.0D))
					{
						g.drawLine((int)d4, (int)(d5 - (double)i), (int)d6, (int)d5);
					}
					// iterate
					d4 = d6;
				}
			}

			if(flag && startOfTrace != null)
			{
				double d7 = d - getYCoordinateOfParabolaByX(d2);
				double d8 = d2;
				g.getClipBounds();
				g.getClipBounds();
				g.drawLine((int)startOfTrace.x, (int)startOfTrace.y, (int)d7, (int)d8);
			}
		}

		if(next != null)
			next.paint(g, d, Math.max(0.0D, d2), flag, drawBeach);
	}

	ArcNode next, prev;
    //Finally, each leaf also stores a pointer to a circle event in the priority queue where the arc defined by this site will disappear
	CirclePoint circlePoint;
	Point startOfTrace;
}
