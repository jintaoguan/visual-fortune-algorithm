
import java.awt.Graphics;

class Line
	implements Paintable
{

	Line(Point mypoint, Point mypoint1)
	{
		P1 = mypoint;
		P2 = mypoint1;
	}

	public void paint(Graphics g)
	{
		g.drawLine((int)P1.x, (int)P1.y, (int)P2.x, (int)P2.y);
	}

	Point P1, P2;
}
