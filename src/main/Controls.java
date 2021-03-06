package main;

import diagram.*;
import diagram.Point;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controls extends Panel
        implements ActionListener {

    private DrawingPaper canvas;
    private Thread thread;
    private boolean running;
    private Button buttons[];

    public Controls(DrawingPaper mycanvas) {
        running = true;
        canvas = mycanvas;
        String as[] = {
                "Go", "Stop", "Clear", "Next"
        };
        buttons = new Button[as.length];
        for (int i = 0; i < as.length; i++) {
            buttons[i] = new Button(as[i]);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

    }

    public void actionPerformed(ActionEvent actionevent) {
        String s = actionevent.getActionCommand();
        if (s == "Go") {
            threadRunning(true);
            if (thread == null) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        do {
                            try {
                                Thread.sleep(15L);
                            } catch (InterruptedException _ex) {
                                break;
                            }
                        } while (canvas.singlestep() && !Thread.currentThread().isInterrupted());
                        threadRunning(false);
                    }
                });
                thread = t;
                t.start();
            } else {
                threadRunning(true);
                thread.resume();
            }
            return;
        }
        if (s == "Clear") {
            canvas.clear();
            if (thread != null)
                thread.interrupt();
            thread = null;
            Point.GLOBLE_INDEX = 0;
            return;
        }
        if (s == "Stop") {
            threadRunning(false);
            thread.suspend();
            return;
        }
        if(s == "Next")
        {
            canvas.step();
            return;
        }
    }

    public void threadRunning(boolean flag) {
        if (flag) {
            buttons[0].setEnabled(false);
            buttons[1].setEnabled(true);
            return;
        } else {
            buttons[0].setEnabled(true);
            buttons[1].setEnabled(false);
        }
    }

}
