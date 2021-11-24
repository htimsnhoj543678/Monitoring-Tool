package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class CityNode extends JLabel
{
    private int xpos;
    private int ypos;
    private Color color;

    public CityNode(int xpos, int ypos, Color color)
    {
        this.xpos = xpos;
        this.ypos = ypos;
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(this.color);
        g.drawOval(this.xpos,this.ypos,15,15);
        g.fillOval(this.xpos,this.ypos,15,15);
    }

}
