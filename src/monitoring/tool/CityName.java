package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class CityName extends JLabel
{
    private String name;
    private int xPos;
    private int yPos;

    public CityName(String name, int xPos, int yPos) {this.name = name; this.xPos = xPos; this.yPos = yPos;}

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.setFont(new Font("TimeRoman",Font.PLAIN,10));
        g.drawString(this.name,this.xPos,this.yPos);

    }
}
