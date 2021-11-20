package monitoring.tool;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UI
{
    Manager manager;
    JFrame window;

    String backgroundImagePath = "C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\resources\\map_1810x908.PNG";
    String nodeImagePath = "C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\resources\\greencircle15x15.png";
    public JTextField cmdInput;
    public JPanel backgroundMap;
    public JLabel backgroundMapLabel;
    public JLabel nodeLabel;
    public JLabel linelabel;


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
//        cmdInput.setLineWrap(true);
//        cmdInput.setWrapStyleWord(true);
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
    public void drawConnection(int x1, int y1, int x2, int y2)
    {
        linelabel = new JLabel();

        backgroundMap.add(linelabel);

    }
    public int latToY(double lat)
    {
        return (int)((manager.ui.backgroundMap.getHeight()/180.0) * (90 - lat));
    }
    public int lonToX(double lon)
    {
        return (int)((manager.ui.backgroundMap.getWidth()/360.0) * (180 + lon));
    }
}
