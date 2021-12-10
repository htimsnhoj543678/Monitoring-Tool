package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class CityLines extends JLabel {
    /**
     * variables of the CityLines class
     */
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color color;

    /**
     * constructor of a CityLines object
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 second x coordinate
     * @param y2 second y coordinate
     * @param color colour of the node
     */
    public CityLines(int x1, int y1, int x2, int y2, Color color)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;

    }

    /**
     *getters for the variables
     */
    public int getX1(){return x1;}
    public int getY1(){return y1;}
    public int getX2(){return x2;}
    public int getY2(){return y2;}
    public Color getColor() {return color;}

    /**
     * method for drawing the points representing the cities
     * @param g graphics element
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(this.color);
        g.drawLine(this.x1, this.y1, this.x2, this.y2);
    }
}

