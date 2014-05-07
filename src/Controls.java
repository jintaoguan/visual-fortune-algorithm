
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

class Controls extends Panel
	implements ActionListener
{

	public Controls(DrawingPaper mycanvas)
	{
		running = true;
		canvas = mycanvas;
		String as[] = {
			"Go", "Stop", "Clear"
		};
		buttons = new Button[as.length];
		for(int i = 0; i < as.length; i++)
		{
			buttons[i] = new Button(as[i]);
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}

	}

	public void actionPerformed(ActionEvent actionevent)
	{
		String s = actionevent.getActionCommand();
		if(s == "Go")
		{
            threadRunning(true);
            if(thread == null){
                Thread t = new Thread(new Runnable(){
                    public void run(){
                        do
                        {
                            try
                            {
                                Thread.sleep(25L);
                            }
                            catch(InterruptedException _ex) {break;}
                        } while(canvas.singlestep() && !Thread.currentThread().isInterrupted());
                        threadRunning(false);
                    }
                });
                thread = t;
                t.start();
            }
            else{
                threadRunning(true);
                thread.resume();
            }
			return;
		}
		if(s == "Clear")
		{
			canvas.clear();
            if(thread != null)
                thread.interrupt();
            thread = null;
			return;
		}
		if(s == "Stop")
		{
			threadRunning(false);
            thread.suspend();
            return;
		}
	}

	public void threadRunning(boolean flag)
	{
        if(flag)
        {
            buttons[0].setEnabled(false);
            buttons[1].setEnabled(true);
            return;
        }
        else{
            buttons[0].setEnabled(true);
            buttons[1].setEnabled(false);
        }
	}

	DrawingPaper canvas;
	Thread thread;
	boolean running;
	Button buttons[];
}
