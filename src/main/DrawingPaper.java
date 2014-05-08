package main;

import arc.*;
import diagram.Point;
import diagram.VoronoiDiagram;
import event.EventQueue;
import event.PointEvent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawingPaper extends Canvas
        implements MouseListener {

    public int XPos;
    public VoronoiDiagram voronoi;
    public EventQueue queue;
    public BeachLineList beachLineList;

    private Graphics offScreenGraphics;
    private Image offScreenImage;
    private boolean drawCircles, drawBeach, drawVoronoiLines;

    public DrawingPaper(int i, int j) {
        setSize(i, j);
        drawCircles = false;
        drawBeach = true;
        drawVoronoiLines = true;
        addMouseListener(this);

        //the points vector
        voronoi = new VoronoiDiagram(i, j);
    }

    //Initialized data structures. Insert site events into the priority queue based on their x-coordinate value.
    public synchronized void init() {
        offScreenImage = createImage(getBounds().width, getBounds().height);
        offScreenGraphics = offScreenImage.getGraphics();
        XPos = 0;
        beachLineList = new BeachLineList();
        queue = new EventQueue();
        voronoi.clear();
        //site events are known beforehand and can be entered into the priority queue during initialization
        for (int i = 0; i < voronoi.size(); i++) {
            queue.insert(new PointEvent((Point) voronoi.elementAt(i)));
        }

    }

    public void mouseClicked(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public synchronized void mousePressed(MouseEvent mouseevent) {

        Point mypoint = new Point(mouseevent.getPoint());
        if (mypoint.x > (double) XPos) {
            voronoi.addElement(mypoint);
            voronoi.checkDegenerate();
            queue.insert(new PointEvent(mypoint));
            repaint();
        }

	    /*
	    Point mypoint1 = new Point(100,300);
	    Point mypoint2 = new Point(200,500);
	    voronoi.addElement(mypoint1);
        voronoi.checkDegenerate();
        queue.insert(new event.PointEvent(mypoint1));
        voronoi.addElement(mypoint2);
        voronoi.checkDegenerate();
        queue.insert(new event.PointEvent(mypoint2));
        repaint();
        */
    }

    public synchronized void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getBounds().width, getBounds().height);
        g.setColor(Color.red);
        voronoi.paint(g, drawVoronoiLines);
        g.setColor(Color.green);
        g.drawLine(XPos, 0, XPos, getBounds().height);
        if (queue != null && beachLineList != null) {
            g.setColor(Color.black);
            queue.paint(g, drawCircles);
            beachLineList.paint(g, XPos, drawVoronoiLines, drawBeach);
        }
    }

    public void update(Graphics g) {
        offScreenGraphics.setClip(g.getClipBounds());
        paint(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, this);
//		paint(g);
    }

    //sweep by pix
    public synchronized boolean singlestep() {
        //if we have not reached first event
        //or current pix does not have event
        if (queue.nextRightHandSideEvent == null || (double) XPos < queue.nextRightHandSideEvent.x)
            XPos++;

        //we encounter the next event
        while (queue.nextRightHandSideEvent != null && (double) XPos >= queue.nextRightHandSideEvent.x) {
            PointEvent eventpoint = queue.pop();
            XPos = Math.max(XPos, (int) eventpoint.x);
            eventpoint.action(this);
            beachLineList.checkBounds(this, XPos);
        }

        if (XPos > getBounds().width && queue.nextRightHandSideEvent == null)
            beachLineList.checkBounds(this, XPos);

        repaint();
        return queue.nextRightHandSideEvent != null || XPos < 1000 + getBounds().width;
    }

    public synchronized void step() {
        PointEvent eventpoint = queue.pop();
        if (eventpoint != null) {
            XPos = Math.max(XPos, (int) eventpoint.x);
            eventpoint.action(this);
        } else if (XPos < getBounds().width) {
            XPos = getBounds().width;
        } else {
            init();
        }
        beachLineList.checkBounds(this, XPos);
        repaint();
    }

    public synchronized void clear() {
        voronoi = new VoronoiDiagram(getBounds().width, getBounds().height);
        restart();
    }

    public synchronized void restart() {
        init();
        repaint();
    }

}
