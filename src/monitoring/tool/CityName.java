package monitoring.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CityName extends JLabel
{
    /**
     * varriables of CityName class
     */
    private String name;
    private int xPos;
    private int yPos;

    /**
     * costructor of a CityName object
     * @param name name of the node
     * @param xPos x position on the window
     * @param yPos y position on the window
     */
    public CityName(String name, int xPos, int yPos) {this.name = name; this.xPos = xPos; this.yPos = yPos;}

    /**
     * method for drawing the text on the graphics object at the location of the node
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(name, g);


        g.fillRect(xPos, yPos - fm.getAscent(), (int) rect.getWidth(), (int) rect.getHeight());
        g.setColor(Color.black);
        g.setFont(new Font("TimeRoman",Font.PLAIN,10));
        g.setColor(Color.white);
        g.drawString(this.name,this.xPos,this.yPos);
    }
}
