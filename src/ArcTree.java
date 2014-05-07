import java.awt.Graphics;

//A ranked list, with x-coordinate from max to min in converted space
class ArcTree
{
	public void insert (Point mypoint, double d, EventQueue eventqueue)
	{
        //if null, set event as root
		if(root == null)
		{
			root = new ArcNode(mypoint);
			return;
		}
		try
		{
			ParabolaPoint parabolapoint = new ParabolaPoint(mypoint);
			parabolapoint.init(d);
			root.init(d);
            //insert this site event to arcTree
			root.insert(parabolapoint, d, eventqueue);
			return;
		}
		catch(Throwable _ex)
		{
			System.out.println("*** error: No parabola intersection during ArcTree.insert()");
		}
	}

	public void checkBounds (DrawingPaper mycanvas, double d)
	{
		if(root != null)
		{
			root.init(d);
			root.checkBounds(mycanvas, d);
		}
	}

	public void paint (Graphics g, double d, boolean flag, boolean drawBeach)
	{
		if(root != null)
		{
			root.init(d);
			root.paint(g, d, 0.0D, flag, drawBeach);
		}
	}

	ArcNode root;
}
