package diagram;

import java.awt.Graphics;


public class Point
        implements Paintable {

    public volatile double x, y;
    public static int GLOBLE_INDEX = 0;
    public int index;

    public Point(double d, double d1) {
        x = d;
        y = d1;
    }

    public Point(Point mypoint) {
        x = mypoint.x;
        y = mypoint.y;
        this.index = mypoint.index;
    }

    public void paint(Graphics g) {
/*		int x = (int)this.x, y = (int)this.y;
        g.drawLine(x  , y-2, x  , y-2);
		g.drawLine(x-1, y-1, x+1, y-1);
		g.drawLine(x-2, y  , x+2, y  );
		g.drawLine(x-1, y+1, x+1, y+1);
		g.drawLine(x  , y+2, x  , y+2);
*/
        g.fillOval((int) (x - 3.0), (int) (y - 3.0), 7, 7);
        g.drawString(Integer.toString(index), (int) x - 5, (int) y - 5);
//		g.drawOval((int)(x - 4.0), (int)(y - 4.0), 9, 9);
    }

    public void paintUnimportant(Graphics g) {
/*		int x = (int)this.x, y = (int)this.y;
        g.drawLine(x  , y-2, x  , y-2);
		g.drawLine(x-1, y-1, x+1, y-1);
		g.drawLine(x-2, y  , x+2, y  );
		g.drawLine(x-1, y+1, x+1, y+1);
		g.drawLine(x  , y+2, x  , y+2);
*/
        g.fillOval((int) (x - 2.0), (int) (y - 2.0), 4, 4);
        g.drawString(Integer.toString(index), (int) x - 2, (int) y - 2);
//		g.drawOval((int)(x - 4.0), (int)(y - 4.0), 9, 9);
    }

    public double distance(Point mypoint) {
        double d = mypoint.x - x;
        double d1 = mypoint.y - y;
        return Math.sqrt(d * d + d1 * d1);
    }
}
