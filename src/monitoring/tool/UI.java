package monitoring.tool;

import javax.swing.*;
import java.awt.*;

public class UI
{
    Manager manager;
    JFrame window;

    String backgroundImagePath = "C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\monitoring\\tool\\map_1200x700.jpeg";
    String nodeImagePath = "C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\monitoring\\tool\\rednode_15x15.png";
    public JTextField cmdInput;
    public JPanel backgroundMap;
    public JLabel backgroundMapLabel;
    public JLabel nodeLabel;


    //constructor
    public UI (Manager manager)
    {
        this.manager = manager;
        createMainField();                  //creates the main window and the text input panel for entering commands
        createBackground();                 //creates the background frame for the map image to sit on
        createNodes(50,50);    //here nodes can be added by giving their coordinates as integers
        createNodes(50,70);

        window.setVisible(true);            //toggles visibility of the window
    }

    public void createMainField()
    {
        //main window
        window = new JFrame();
        window.setSize(1300,800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.white);
        window.setLayout(null);

        //text input panel
        cmdInput = new JTextField("input commands here");
        cmdInput.setBounds(50,700,1200,50);
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
        backgroundMap.setBounds(50,0,1200,700);
        backgroundMap.setBackground(Color.blue);
        backgroundMap.setLayout(null);
        window.add(backgroundMap);

        //create new label in the jpanel to place the background image
        backgroundMapLabel = new JLabel();
        backgroundMapLabel.setBounds(0,0,1200,700);
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
    }
}
