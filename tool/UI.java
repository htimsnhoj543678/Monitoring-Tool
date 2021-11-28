package monitoring.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UI extends JFrame
{
    Manager manager;
    JFrame window;

    String backgroundImagePath = "src\\resources\\map_1810x908.PNG";

    public JTextField cmdInput;
    public JTextField infoPanel;
    public JPanel backgroundMap;
    public JLabel backgroundMapLabel;
    public ArrayList<JLabel> nodeLabelList = new ArrayList<>();
    public JLabel nodeNameLabel;
    public ArrayList<JLabel> lineLabel = new ArrayList<>();

    //constructor
    public UI (Manager manager)
    {
        this.manager = manager;
        createMainField();                  //creates the main window and the text input panel for entering commands
        createBackground();                 //creates the background frame for the map image to sit on
        createInfoPanel();
        window.setVisible(true);            //toggles visibility of the window
    }

    public void createMainField()
    {
        //creates a new JFrame for the main window that displays on the screen
        window = new JFrame();
        window.setSize(1835,1015);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.white);
        window.setLayout(null);

        //text input panel shown at the bottom left of the gui
        cmdInput = new JTextField("   input commands here");
        cmdInput.setBounds(10,920,500,50);
        cmdInput.setBackground(Color.black);
        cmdInput.setForeground(Color.white);
        cmdInput.setEditable(true);
        window.add(cmdInput);
    }

    public void createBackground()
    {
        //create new JPanel inside the main JFrame window
        backgroundMap = new JPanel();
        backgroundMap.setBounds(0,0,1810,908);
        backgroundMap.setBackground(Color.white);
        backgroundMap.setLayout(null);
        window.add(backgroundMap);

        //create new JLabel for the background map in the JPanel to place the background image
        backgroundMapLabel = new JLabel();
        backgroundMapLabel.setBounds(7,7,1810,908);
        ImageIcon backgroundImage = new ImageIcon(backgroundImagePath);
        backgroundMapLabel.setIcon(backgroundImage);
    }

    public void createLine(int x1, int y1, int x2, int y2, Color color)
    {
        //creates a JLabel for the line connections between cities, using the CityLines-paintComponent method to draw to the JLabel
        lineLabel.add(new CityLines(x1, y1, x2, y2, color));
        lineLabel.get(lineLabel.size()-1).setBounds(7,7,backgroundMap.getWidth(),backgroundMap.getHeight());

        backgroundMap.add(lineLabel.get(lineLabel.size()-1));
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    public void removeLines()
    {
        for(int i = 0; i <lineLabel.size();i++)
        {
            backgroundMap.remove(lineLabel.get(i));
            backgroundMap.repaint();
        }
    }

    public void createLabels(Node node)
    {
        //create a JLabel for the nodes name, using the class CityNames-paintComponent method to draw onto it with the name of the node
        nodeNameLabel = new CityName(node.getName(),15,7);
        nodeNameLabel.setBounds(node.getXpos(),node.getYpos(),backgroundMap.getWidth(),backgroundMap.getHeight());
        backgroundMap.add(nodeNameLabel);
    }

    public void createInfoPanel()
    {
        //creates the info panel shown at the bottom right of the gui, and adds it to the main JFrame "window"
        infoPanel = new JTextField("      Node info will show here");
        infoPanel.setBounds(530,920,800,50);
        infoPanel.setBackground(Color.black);
        infoPanel.setForeground(Color.white);
        infoPanel.setEditable(false);
        window.add(infoPanel);
    }

    public void createNode(Node node)
    {
        //create a new JLabel and call CityNodes-paintComponent method to paint onto the JLabel, then set its position and size on the map
        JLabel nodeLabel = new CityNode(node.getColor());
        nodeLabel.setBounds(node.getXpos(),node.getYpos(),16,16);

        nodeLabel.setOpaque(false);                                     //set the nodes JLabel to be invisible
        nodeLabel.addMouseListener(new MouseAdapter() {                 //listen for the mouse to hover over the nodes JLabel
            public void mouseEntered(MouseEvent evt) {
                nodeLabel.setOpaque(true);                              //set the nodes JLabel to be visible
                Color c = nodeLabel.getBackground();
                nodeLabel.setBackground(node.getColor());               //sets the background color to be the color of the node
                nodeLabel.setForeground(c);

                infoPanel.setText(node.returnInfo());                   //sets the text displayed in the bottom right textField to display the info for the node
            }
            public void mouseExited(MouseEvent evt) {                   //resets everything from above to what it was previously
                nodeLabel.setOpaque(false);
                Color c = nodeLabel.getBackground();
                nodeLabel.setBackground(nodeLabel.getForeground());
                nodeLabel.setForeground(c);

                infoPanel.setText("  Node info will show here");
            }
        });
        nodeLabelList.add(nodeLabel);

        //add to the background map
        backgroundMap.add(nodeLabelList.get(nodeLabelList.size()-1));
        backgroundMap.add(backgroundMapLabel);
        backgroundMap.repaint();
    }

    public void removeNodes()
    {
        for(int i = 0; i <nodeLabelList.size();i++)
        {
            backgroundMap.remove(nodeLabelList.get(i));
            backgroundMap.repaint();
        }
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