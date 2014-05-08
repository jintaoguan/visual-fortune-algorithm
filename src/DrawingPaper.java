
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class DrawingPaper extends Canvas
	implements MouseListener
{

	public DrawingPaper(int i, int j)
	{
        setSize(i, j);
		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		addMouseListener(this);

        //the points vector
		Voronoi = new VoronoiPoints(i, j);
	}

    //Initialized data structures. Insert site events into the priority queue based on their x-coordinate value.
	public synchronized void init()
	{
		offScreenImage = createImage(getBounds().width, getBounds().height);
		offScreenGraphics = offScreenImage.getGraphics();
		XPos = 0;
		arcTree = new ArcTree();
		queue = new EventQueue();
		Voronoi.clear();
        //site events are known beforehand and can be entered into the priority queue during initialization
		for(int i = 0; i < Voronoi.size(); i++)
		{
			queue.insert(new EventPoint((Point) Voronoi.elementAt(i)));
		}

	}

	public void mouseClicked(MouseEvent mouseevent)
	{
	}

	public void mouseReleased(MouseEvent mouseevent)
	{
	}

	public void mouseEntered(MouseEvent mouseevent)
	{
	}

	public void mouseExited(MouseEvent mouseevent)
	{
	}

	public synchronized void mousePressed(MouseEvent mouseevent)
	{
	    
		Point mypoint = new Point(mouseevent.getPoint());
		if(mypoint.x > (double)XPos)
		{
			Voronoi.addElement(mypoint);
			Voronoi.checkDegenerate();
			queue.insert(new EventPoint(mypoint));
			repaint();
		}
		
	    /*
	    Point mypoint1 = new Point(100,300);
	    Point mypoint2 = new Point(200,500);
	    Voronoi.addElement(mypoint1);
        Voronoi.checkDegenerate();
        queue.insert(new EventPoint(mypoint1));
        Voronoi.addElement(mypoint2);
        Voronoi.checkDegenerate();
        queue.insert(new EventPoint(mypoint2));
        repaint();
        */
	}

	public synchronized void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		g.setColor(Color.blue);
		Voronoi.paint(g, drawVoronoiLines);
		g.setColor(Color.red);
		g.drawLine(XPos, 0, XPos, getBounds().height);
		if(queue != null && arcTree != null)
		{
			g.setColor(Color.black);
			queue.paint(g, drawCircles);
			arcTree.paint(g, XPos, drawVoronoiLines, drawBeach);
		}
	}

	public void update(Graphics g)
	{
		offScreenGraphics.setClip(g.getClipBounds());
		paint(offScreenGraphics);
		g.drawImage(offScreenImage, 0, 0, this);
//		paint(g);
	}

    //sweep by pix
	public synchronized boolean singlestep ()
	{
        //if we have not reached first event
        //or current pix does not have event
		if(queue.nextRightHandSideEvent == null || (double)XPos < queue.nextRightHandSideEvent.x)
			XPos++;

        //we encounter the next event
		while(queue.nextRightHandSideEvent != null && (double)XPos >= queue.nextRightHandSideEvent.x)
		{
			EventPoint eventpoint = queue.pop();
			XPos = Math.max(XPos, (int)eventpoint.x);
			eventpoint.action(this);
			arcTree.checkBounds(this, XPos);
		}

		if(XPos > getBounds().width && queue.nextRightHandSideEvent == null)
			arcTree.checkBounds(this, XPos);

		repaint();
		return queue.nextRightHandSideEvent != null || XPos < 1000 + getBounds().width;
	}

	public synchronized void step()
	{
		EventPoint eventpoint = queue.pop();
		if(eventpoint != null)
		{
			XPos = Math.max(XPos, (int)eventpoint.x);
			eventpoint.action(this);
		} else
		if(XPos < getBounds().width)
		{
			XPos = getBounds().width;
		} else
		{
			init();
		}
		arcTree.checkBounds(this, XPos);
		repaint();
	}

	public synchronized void clear()
	{
		Voronoi = new VoronoiPoints(getBounds().width, getBounds().height);
		restart();
	}

	public synchronized void restart()
	{
		init();
		repaint();
	}

	Graphics offScreenGraphics;
	Image offScreenImage;
	int XPos;
	VoronoiPoints Voronoi;
	boolean drawCircles, drawBeach, drawVoronoiLines;
	EventQueue queue;
	ArcTree arcTree;
}
