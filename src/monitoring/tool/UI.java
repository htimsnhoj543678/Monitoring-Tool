package monitoring.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class UI extends JFrame
{
    Manager manager;
    JFrame window;

    String backgroundImagePath = "src\\resources\\map_1810x908.PNG";
    String nodeImagePath = "src\\resources\\greencircle15x15.png";
    public JTextField cmdInput;
    public JPanel backgroundMap;
    public JLabel backgroundMapLabel;
    public JLabel nodeLabel;
    public JLabel lineLabel;

    //constructor
    public UI (Manager manager)
    {
        this.manager = manager;
        createMainField();                  //creates the main window and the text input panel for entering commands
        createBackground();                 //creates the background frame for the map image to sit on
        //createNodes(50,50);    //here nodes can be added by giving their coordinates as integers

        window.setVisible(true);            //toggles visibility of the window
    }

    public void createMainField()
    {
        //main window
        window = new JFrame();
        window.setSize(1835,1015);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.white);
        window.setLayout(null);

        //text input panel
        cmdInput = new JTextField("input commands here");
        cmdInput.setBounds(10,920,1200,50);
        cmdInput.setBackground(Color.black);
        cmdInput.setForeground(Color.white);
        cmdInput.setEditable(true);
        window.add(cmdInput);

        //submit button
        JButton button =new JButton("Enter");
        button.setBounds(1220,930,95,30);
        window.add(button);


    }

    public void createBackground()
    {
        //create new Jpanel inside of the main window
        backgroundMap = new JPanel();
        backgroundMap.setBounds(0,0,1810,908);
        backgroundMap.setBackground(Color.white);
        backgroundMap.setLayout(null);
        window.add(backgroundMap);

        //create new label in the jpanel to place the background image
        backgroundMapLabel = new JLabel();
        backgroundMapLabel.setBounds(7,7,1810,908);
        ImageIcon backgroundImage = new ImageIcon(backgroundImagePath);
        backgroundMapLabel.setIcon(backgroundImage);


    }

    public void createLine(int x1, int y1, int x2, int y2, Color color)
    {
        lineLabel = new Line(x1, y1, x2, y2, color);
        int JPwidth = backgroundMap.getWidth();
        int JPheight = backgroundMap.getHeight();
        int startX = 7;
        int startY = 7;

        lineLabel.setBounds(startX,startY,JPwidth,JPheight);

        backgroundMap.add(lineLabel);
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    public void createNodes(int nodeX, int nodeY)
    {
        //create a new label for the node
        nodeLabel = new JLabel();
        nodeLabel.setBounds(nodeX,nodeY,15,15);
        ImageIcon nodeImage = new ImageIcon(nodeImagePath);
        nodeLabel.setIcon(nodeImage);

        //add it to the background map
        backgroundMap.add(nodeLabel);
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    public int latToY(double lat)
    {
        return (int)((manager.ui.backgroundMap.getHeight()/180.00) * (90.00 - lat));
    }
    public int lonToX(double lon)
    {
        return (int)((manager.ui.backgroundMap.getWidth()/360.00) * (180.00 + lon));
    }
}
