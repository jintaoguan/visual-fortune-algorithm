
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame
{

    public static void main(String[] args){
        JFrame f = new JFrame();
        f.setSize(1000, 1000);
        DrawingPaper canvas = new DrawingPaper(800, 800);
        Controls controls = new Controls(canvas);
        f.setLayout(new BorderLayout());
        f.add("Center", canvas);
        f.add("South", controls);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        canvas.init();
    }
}
