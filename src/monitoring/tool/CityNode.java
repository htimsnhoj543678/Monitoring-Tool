package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class CityNode extends JLabel
{
    /**
     * variables for CityNode
     */
    private Color color;

    /**
     * CityNode constructor
     * @param color the colour that the node is going to be (green - online, blue - firewall, red - offline)
     */
    public CityNode(Color color)
    {
        this.color = color;
    }

    /**
     * method for drawing the node on the window
     * @param g graphics object
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(this.color);
        g.drawOval(0,0,15,15);
        g.fillOval(0, 0,15,15);
        g.dispose();
    }

}
