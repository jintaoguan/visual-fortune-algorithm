// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MyCanvas extends Canvas
	implements MouseListener
{

	public MyCanvas(int i, int j, int k)
	{
		drawCircles = false;
		drawBeach = true;
		drawVoronoiLines = true;
		drawDelaunay = false;
		addMouseListener(this);

        //the points vector
		Voronoi = new VoronoiClass(i, j, k);
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
		Delaunay = new DelaunayClass();
        //site events are known beforehand and can be entered into the priority queue during initialization
		for(int i = 0; i < Voronoi.size(); i++)
		{
			queue.insert(new EventPoint((MyPoint) Voronoi.elementAt(i)));
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
		MyPoint mypoint = new MyPoint(mouseevent.getPoint());
		if(mypoint.x > (double)XPos)
		{
			Voronoi.addElement(mypoint);
			Voronoi.checkDegenerate();
			queue.insert(new EventPoint(mypoint));
			repaint();
		}
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
		if(drawDelaunay)
		{
			g.setColor(Color.gray);
			Delaunay.paint(g);
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
		Voronoi = new VoronoiClass(getBounds().width, getBounds().height, 0);
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
	VoronoiClass Voronoi;
	DelaunayClass Delaunay;
	boolean drawCircles, drawBeach, drawVoronoiLines, drawDelaunay;
	EventQueue queue;
	ArcTree arcTree;
}
