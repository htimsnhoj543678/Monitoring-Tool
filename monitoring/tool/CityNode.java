package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class CityNode extends JLabel
{
    private Color color;

    public CityNode(Color color)
    {
        this.color = color;
    }

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
