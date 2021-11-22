package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class Line extends JLabel {

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color color;

    public Line(int x1, int y1, int x2, int y2, Color color)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;

    }

    public int getX1(){return x1;}
    public int getY1(){return y1;}
    public int getX2(){return x2;}
    public int getY2(){return y2;}
    public Color getColor() {return color;}

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(this.color);
        g.drawLine(this.x1, this.y1, this.x2, this.y2);
    }
}

