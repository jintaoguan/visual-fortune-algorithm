package main;

import arc.*;
import diagram.Point;
import diagram.VoronoiDiagram;
import event.CircleEvent;
import event.EventQueue;
import event.PointEvent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawingPaper extends Canvas
        implements MouseListener {

    //the sweep line
    public int XPos;
    public VoronoiDiagram voronoi;
    public EventQueue queue;
    public BeachLineList beachLineList;

    private Graphics offScreenGraphics;
    private Image offScreenImage;
    private boolean drawCircles, drawBeach, drawVoronoiLines;

    public DrawingPaper(int i, int j) {
        setSize(i, j);
        drawCircles = true;
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

        Point mypoint = new Point(mouseevent.getPoint().getX(), mouseevent.getPoint().getY());
        if (mypoint.x > (double) XPos) {
            PointEvent pe = new PointEvent(mypoint);
            queue.insert(pe);
            mypoint.index = pe.index;
            voronoi.addElement(mypoint);
            voronoi.checkDegenerate();
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

        // paint the background
        g.setColor(Color.black);
        g.fillRect(0, 0, getBounds().width, getBounds().height);

        // paint completed lines and points
        g.setColor(Color.red);
        voronoi.paint(g, drawVoronoiLines);

        // paint the sweeping line
        g.setColor(Color.blue);
        g.drawLine(XPos, 0, XPos, getBounds().height);

        // paint the parabola
        if (queue != null && beachLineList != null) {
            g.setColor(Color.white);
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

        //status
        if(eventpoint != null){
            //this event status
            System.out.format("This is a %s, the index is %d\n",
                    eventpoint.getClass().toString().substring(12), eventpoint.index);
            if(eventpoint instanceof CircleEvent){
                System.out.format("The disappearing beach line for this circle event is %d\n",
                        ((CircleEvent) eventpoint).beachLine.beachIndex);
            }
            if(queue.nextRightHandSideEvent != null){
                //next event status
                System.out.format("Next is a %s, the index is %d\n",
                        queue.nextRightHandSideEvent.getClass().toString().substring(12), queue.nextRightHandSideEvent.index);
                if(queue.nextRightHandSideEvent instanceof CircleEvent){
                    System.out.format("The disappearing beach line for next circle event is %d\n",
                            ((CircleEvent) queue.nextRightHandSideEvent).beachLine.beachIndex);
                }
            }
            else{
                System.out.println("Next event is null\n");
            }
        }
        else{
            System.out.println("This event is null\n");
        }
        System.out.println();
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
