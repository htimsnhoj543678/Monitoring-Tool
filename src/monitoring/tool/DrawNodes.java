package monitoring.tool;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class DrawNodes extends JPanel
{
    private static final int circle_X = 200;
    private static final int circle_Y = 250;
    private static final int circle_RADIUS = 25;
    private static final int circle_RADIUS2 = circle_RADIUS;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try
        {
            final BufferedImage image = ImageIO.read(new File("C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\monitoring\\tool\\map_1200x700.jpeg"));
            g.drawImage(image, 0,0,null);
            g.drawOval(10,10,circle_RADIUS,circle_RADIUS);
            g.fillOval(10,10,circle_RADIUS,circle_RADIUS);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(1920 , 1080);
    }

    // create the GUI explicitly on the Swing event thread
    public static void createAndShowGui() {
        DrawNodes mainPanel = new DrawNodes();

        JFrame frame = new JFrame("title");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGui();
//            }
//        });
//    }
}
