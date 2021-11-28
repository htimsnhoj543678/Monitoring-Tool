package monitoring.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.*;

public class Main extends JFrame
{
    //make sure there is no blank line at the end of Graph.txt or it will throw an exception
    public static final String graphPath = "src\\resources\\Graph.txt";
    public static final String attackPath = "src\\resources\\Attack.txt";
    public static ArrayList<Node> nodeList= new ArrayList();
    public static Queue<Attack> attackQueue = new LinkedList<>();

    //reads in from Graph.txt up until "---", makes a node out of each line with lat/lon firewall true/false
    //draws the node xy position on the map
    //adds that node to nodeList
    public static void readInNodes(Manager manager)
    {
        String cityname;
        double lat;
        double lon;
        String tempString;

        try {
            FileInputStream input = new FileInputStream(graphPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine())
            {
                String nextline = scanin.nextLine();
                if(nextline.equals("-------------------------------------"))
                {
                    break;
                }
                else
                {
                    //city name
                    String parts1[] = nextline.split(", ");
                    cityname = parts1[0];

                    //lat and lon
                    String parts2[] = nextline.split("[\\(\\)]");
                    tempString = parts2[1];
                    String parts3[] = tempString.split(", ");
                    lat = Double.parseDouble(parts3[0]);
                    lon = Double.parseDouble(parts3[1]);

                    //firewall true/false
                    Node tempNode = new Node(cityname,lat,lon,false);
                    if(nextline.contains("firewall"))
                    {
                        tempNode.setFirewallStatus(true);
                        tempNode.checkColor();
                    }

                    int x = manager.ui.lonToX(tempNode.getLon());
                    int y = manager.ui.latToY(tempNode.getLat());

                    //set nodes x and y pos
                    tempNode.setXY(x,y);

                    //insert node into list
                    nodeList.add(tempNode);
                }
            }
            scanin.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //reads in from Graph.txt after "---", reads origin city and its destination for each line
    //adds destination city to origincitys private "connections" list
    public static void readInConnections()
    {
        ArrayList<String> connectionsList = new ArrayList<>();

        try
        {
            FileInputStream input = new FileInputStream(graphPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine())
            {
                if(scanin.nextLine().equals("-------------------------------------"))
                {
                    while(scanin.hasNextLine())
                    {
                        connectionsList.add(scanin.nextLine());
                    }
                }
            }
            scanin.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for(int i = 0; i < connectionsList.size();i++)
        {
            String splitstring[] = connectionsList.get(i).split(", ");
            String originCity = splitstring[0];
            String destCity = splitstring[1];

            for(int j = 0; j < nodeList.size();j++)
            {
                if(nodeList.get(j).getName().equals(originCity))
                {
                    for(int k = 0; k < nodeList.size(); k++)
                    {
                        if(nodeList.get(k).getName().equals(destCity))
                        {
                            nodeList.get(j).insertConnection(nodeList.get(k));
                            nodeList.get(j).setNumConnections();

                            nodeList.get(k).insertConnection(nodeList.get(j));
                            nodeList.get(k).setNumConnections();
                        }
                    }
                }
            }
        }
    }
    public static void readInAttacks()
    {
        try
        {
            FileInputStream input = new FileInputStream(attackPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine())
            {
                String line = scanin.nextLine();
                String[] splitAttack = line.split(", ");
                attackQueue.add(new Attack(splitAttack[0], splitAttack[1], splitAttack[2], splitAttack[3]));

            }
            scanin.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        while (!attackQueue.isEmpty()){
            for (int i = 0; i <= nodeList.size(); i++)
            {
                if (nodeList.get(i).getName().equals(attackQueue.peek().getName()))
                {
                    nodeList.get(i).setAttacks(attackQueue.poll());
                    break;
                }
            }
        }
    }
    public static void drawConnections(Manager manager)
    {
        for(int i = 0;i<nodeList.size();i++)                                        //walks through list of nodes
        {
            Node tempNode = nodeList.get(i);                                        //grabs current node
            int x1 = manager.ui.lonToX(tempNode.getLon());                          //current node xpos
            int y1 = manager.ui.latToY(tempNode.getLat());                          //current node ypos

            for(int j = 0;j<nodeList.size();j++)                                    //walks through nodes again for name comparison
            {
                Node tempNode2 = nodeList.get(j);                                   //grabs current node
                int x2 = manager.ui.lonToX(tempNode2.getLon());                     //grabs x2pos
                int y2 = manager.ui.latToY(tempNode2.getLat());                     //grabs y2pos
                if(tempNode.getConnections().contains(tempNode2.getName()))
                {
                    if(tempNode2.getOnlineStatus())
                    {
                        manager.ui.createLine(x1,y1,x2,y2,Color.gray);
                    }
                }
            }
        }
    }
    public static void drawNodes(Manager manager)
    {
        for (int i = 0; i < nodeList.size(); i++)
        {
            nodeList.get(i).checkColor();
            manager.ui.createNode(nodeList.get(i));
        }
    }
    public static void drawLabels(Manager manager)
    {
        //display labels on map
        for (int i = 0; i < nodeList.size(); i++)
        {
            manager.ui.createLabels(nodeList.get(i));
        }

    }

    public static void main(String[] args)
    {
        //initial map will all given info (only karachi becomes offline)
        Manager manager = new Manager();
        readInNodes(manager);
        drawLabels(manager);
        readInConnections();
        readInAttacks();
        drawNodes(manager);
        drawConnections(manager);

        //next map of updated nodes after an outbreak has spread to other nodes
        //(the outbreak from karachi has spread to shanghai causing it to go offline now too)
//        readInAttacks();
//        manager.ui.removeNodes();
//       manager.ui.removeLines();
//        drawNodes(manager);
//        drawConnections(manager);


//        //readInNodes() / readInConnections() testing
//        for(int i = 0; i < nodeList.size();i++)
//        {
//            System.out.println(nodeList.get(i).returnInfo());
//            //nodeList.get(i).printAttacks();
//            //nodeList.get(i).printFirewall();
//        }

        manager.ui.cmdInput.addKeyListener(
                new KeyListener(){
                    @Override
                    public void keyPressed(KeyEvent e){
                        if(e.getKeyCode() == KeyEvent.VK_ENTER){
                            String outcome = manager.ui.cmdInput.getText();
                            if(outcome.equals("show names"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show connections"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getConnections());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show xypos"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getXpos()+" "+nodeList.get(i).getYpos());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show latlon"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getLat()+" "+nodeList.get(i).getLon());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show firewall"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getFirewallStatus());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show attacks"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    nodeList.get(i).printAttacks();
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show onlinestatus"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getOnlineStatus());
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("show firewalllog"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    nodeList.get(i).printFirewall();
                                }
                                System.out.println("\n");
                            }
                            else if(outcome.equals("update"))
                            {
                                readInAttacks();
                                manager.ui.removeNodes();
                                manager.ui.removeLines();
                                drawNodes(manager);
                                drawConnections(manager);
                            }
                            else
                            {
                                System.out.println("Commands: ");
                                System.out.println("show names");
                                System.out.println("show connections");
                                System.out.println("show xypos");
                                System.out.println("show latlon");
                                System.out.println("show firewall");
                                System.out.println("show attacks");
                                System.out.println("show onlinestatus");
                                System.out.println("show firewalllog");
                                System.out.println("\n");
                            }
                        }
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {}
                    @Override
                    public void keyReleased(KeyEvent e) {}
                }
        );
//        //how to add a node ------------------------------------------------------------------------------------------
//        //making a node for Calgary
//        Node myNode = new Node("Calgary",51.05,-114.07,false);
//
//        //drawing the new node to the map
//        int x = manager.ui.lonToX(myNode.getLon());
//        int y = manager.ui.latToY(myNode.getLat());
//
//        //setting the internal xy position for our node
//        myNode.setXY(x,y);
//
//        //drawing the label
//        manager.ui.createLabels(x,y,myNode.getName());
//
//        //drawing the node
//        manager.ui.createNode(myNode);
//
//        //adding it to the nodeList used in main
//        nodeList.add(myNode);
//
//        //taking a pre-existing node from the list (Ottawa)
//        Node Ottawa  = nodeList.get(2);
//
//        //giving our node a connection (Ottawa)
//        //nodeList.get(nodeList.size()-1).insertConnection(myNode); //connect from ottawa to calgary
//        myNode.insertConnection(Ottawa);                            //connect from calgary to ottawa
//
//        //grabbing its xy position
//        int x2 = manager.ui.lonToX(Ottawa.getLon());
//        int y2 = manager.ui.latToY(Ottawa.getLat());
//
//        //drawing a line between our node and ottawa
//        manager.ui.createLine(x,y,x2,y2,Color.green);
    }
}
