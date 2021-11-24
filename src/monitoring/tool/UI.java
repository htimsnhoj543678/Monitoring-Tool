package monitoring.tool;

import javax.swing.*;
import java.awt.*;

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
    public JLabel nodeCityLabel;
    public JLabel lineLabel;

    //constructor
    public UI (Manager manager)
    {
        this.manager = manager;
        createMainField();                  //creates the main window and the text input panel for entering commands
        createBackground();                 //creates the background frame for the map image to sit on
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
        cmdInput.setBounds(10,920,500,50);
        cmdInput.setBackground(Color.black);
        cmdInput.setForeground(Color.white);
        cmdInput.setEditable(true);
        window.add(cmdInput);
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
        lineLabel.setBounds(7,7,backgroundMap.getWidth(),backgroundMap.getHeight());
        backgroundMap.add(lineLabel);
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    public void createNodes(int nodeX, int nodeY,String name)
    {
        //create a new label for the node
        nodeLabel = new JLabel();
        nodeLabel.setBounds(nodeX,nodeY,15,15);
        ImageIcon nodeImage = new ImageIcon(nodeImagePath);
        nodeLabel.setIcon(nodeImage);

        //create a label for the node name
        nodeCityLabel = new CityName(name,7,7);
        nodeCityLabel.setBounds(nodeX,nodeY,backgroundMap.getWidth(),backgroundMap.getHeight());
        backgroundMap.add(nodeCityLabel);

        //add it to the background map
        backgroundMap.add(nodeLabel);
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    //for turning lat/lon into xy coordinates
    public int latToY(double lat)
    {
        return (int)((manager.ui.backgroundMap.getHeight()/180.00) * (90.00 - lat));
    }
    public int lonToX(double lon)
    {
        return (int)((manager.ui.backgroundMap.getWidth()/360.00) * (180.00 + lon));
    }
}
