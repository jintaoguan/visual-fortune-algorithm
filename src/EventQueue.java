// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

//A priority queue to keep track of the site events and circle events
class EventQueue
{
	public void insert (EventPoint p)
	{
		if(nextRightHandSideEvent != null)
			nextRightHandSideEvent.insert(p);

		if(p.prev == null)
			nextRightHandSideEvent = p;
	}

	public void remove (EventPoint eventpoint)
	{
		if(eventpoint.next != null)
			eventpoint.next.prev = eventpoint.prev;

		if(eventpoint.prev != null)
				eventpoint.prev.next = eventpoint.next;
		else	nextRightHandSideEvent = eventpoint.next;
	}

	public EventPoint pop ()
	{
		EventPoint eventpoint = nextRightHandSideEvent;
		if(eventpoint != null)
		{
			nextRightHandSideEvent = nextRightHandSideEvent.next;
			if(nextRightHandSideEvent != null)
			{
				nextRightHandSideEvent.prev = null;
			}
		}
		return eventpoint;
	}

	public void paint(Graphics g, boolean flag)
	{
		for(EventPoint eventpoint = nextRightHandSideEvent; eventpoint != null; eventpoint = eventpoint.next)
		{
			if(flag || !(eventpoint instanceof CirclePoint))
			{
				eventpoint.paint(g);
			}
		}

	}


	EventPoint nextRightHandSideEvent;
}
