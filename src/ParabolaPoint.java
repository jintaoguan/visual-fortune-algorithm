
class ParabolaPoint extends Point
{

	public ParabolaPoint(Point mypoint)
	{
		super(mypoint);
	}

    //real x and y are the points in a downwards sweeping space with sweeping line's x coordinate as origin
    //which is equal to the old space
	public double realX()
	{
		return y;
	}

	public double realY(double d)
	{
		return d - x;
	}

	public CirclePoint calculateCenter(Point mypoint, ArcNode arcnode, Point mypoint1)
	{
		CirclePoint circlepoint = null;
		Point mypoint2 = new Point(arcnode.x - mypoint.x, arcnode.y - mypoint.y);
		Point mypoint3 = new Point(mypoint1.x - arcnode.x, mypoint1.y - arcnode.y);
		if(mypoint3.y * mypoint2.x > mypoint3.x * mypoint2.y)
		{
			double d = -mypoint2.x / mypoint2.y;
			double d1 = (mypoint.y + mypoint2.y / 2D) - d * (mypoint.x + mypoint2.x / 2D);
			double d2 = -mypoint3.x / mypoint3.y;
			double d3 = (arcnode.y + mypoint3.y / 2D) - d2 * (arcnode.x + mypoint3.x / 2D);
			double d4;
			double d5;
			if(mypoint2.y == 0.0D)
			{
				d4 = mypoint.x + mypoint2.x / 2D;
				d5 = d2 * d4 + d3;
			} else
			if(mypoint3.y == 0.0D)
			{
				d4 = arcnode.x + mypoint3.x / 2D;
				d5 = d * d4 + d1;
			} else
			{
				d4 = (d3 - d1) / (d - d2);
				d5 = d * d4 + d1;
			}
            //the circle events are detected as the beach line changes its shape, and need to be entered into the queue at that time
			circlepoint = new CirclePoint(d4, d5, arcnode);
		}
		return circlepoint;
	}

	public void init(double d)
	{
		double d1 = realX();
		double d2 = realY(d);
		a = 1.0D / (2D * d2);
		b = -d1 / d2;
		c = (d1 * d1) / (2D * d2) + d2 / 2D;
	}

	public double getYCoordinateOfParabolaByX(double d)
	{
		return (a * d + b) * d + c;
	}

    //calculate the x value of a quadratic formulate: ax^2 + bx + c = 0
	public double[] solveQuadratic(double d, double d1, double d2)
		throws Throwable
	{
		double ad[] = new double[2];
		double d3 = d1 * d1 - 4D * d * d2;
		if(d3 < 0.0D)
		{
			throw new Throwable();
		}
		if(d == 0.0D)
		{
			if(d1 != 0.0D)
			{
				ad[0] = -d2 / d1;
			} else
			{
				throw new Throwable();
			}
		} else
		{
			double d4 = Math.sqrt(d3);
			double d5 = -d1;
			double d6 = 2D * d;
			ad[0] = (d5 + d4) / d6;
			ad[1] = (d5 - d4) / d6;
		}
		return ad;
	}

    //parameters of parabola function: ax^2 + bx + c = y
	double a, b, c;
}
