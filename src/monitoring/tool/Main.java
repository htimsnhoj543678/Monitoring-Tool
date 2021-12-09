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
    public static ArrayList<Attack> attackList = new ArrayList();

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
                nodeList.get(j).setNodeList(nodeList);
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
        System.out.println("This is the initial adjacency matrix before any attacks are read in: ");
        nodeList.get(0).adjMatrix();
    }

    public static void readInAttacks()
    {
        //nodeList.get(0).ajdMatrix();//TODO: temporary delete later
        try
        {
            FileInputStream input = new FileInputStream(attackPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine())
            {
                String line = scanin.nextLine();
                String[] splitAttack = line.split(", ");
                attackList.add(new Attack(splitAttack[0], splitAttack[1], splitAttack[2], splitAttack[3]));

            }
            scanin.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        boolean finished = false;
        while (!finished) {
            finished = true;
            for (int i = 0; i < attackList.size() - 1; i++) {
                Attack holder;
                if ((attackList.get(i).getTotalDate().compareTo(attackList.get(i + 1).getTotalDate())) > 0) {
                    holder = attackList.get(i);
                    attackList.set(i, attackList.get(i + 1));
                    attackList.set(i + 1, holder);
                    finished = false;
                }
            }
        }
        Queue<Attack> attackQueue = new LinkedList<>();
        for (int i = 0; i < attackList.size(); i++) {
            attackQueue.add(attackList.get(i));

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


        System.out.println("This is the adjacency matrix after the attacks are read in: ");
        nodeList.get(0).adjMatrix();
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
                    else
                    {
                        nodeList.get(i).getConnectedNodes().remove(nodeList.get(j));
                        nodeList.get(i).setNumConnections();
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
                                int count = 0;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(nodeList.get(i).getFirewallStatus()){
                                        count++;
                                        System.out.println(nodeList.get(i).getName()+" "+nodeList.get(i).getFirewallStatus());
                                    }

                                }
                                System.out.println("There are a total of " + count +" nodes that have firewalls\n");
                            }
                            else if(outcome.equals("show firewallattacked"))
                            {
                                int count = 0;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(nodeList.get(i).getFirewallStatus() && (nodeList.get(i).getFirewallLogSize()>0)){
                                        count++;
                                        System.out.println(nodeList.get(i).getName()+"'s firewall was attacked ");
                                    }

                                }
                                System.out.println("There are a total of " + count +" nodes that have firewalls that have been attacked\n");
                            }
                            else if(outcome.equals("show attacks"))
                            {
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    nodeList.get(i).sortAttacksByColour();
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
                                    nodeList.get(i).sortAttacksByColour();;
                                }
                            }
                            else if(outcome.equals("show infected"))
                            {
                                int count = 0;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(nodeList.get(i).numAttacks>0){
                                        System.out.println(nodeList.get(i).getName()+" is infected");
                                        count++;
                                    }
                                }
                                System.out.println("There are a total of " + count +" nodes infected\n");
                            }
                            else if(outcome.equals("show outbreaks"))
                            {
                                int count = 0;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(nodeList.get(i).getOutbreakStatus()){
                                        System.out.println(nodeList.get(i).getName()+" has an outbreak");
                                        count++;
                                    }
                                }
                                System.out.println("There are a total of " + count +" nodes that have outbreaks\n");
                            }
                            else if(outcome.equals("show adjmatrix"))
                            {
                                System.out.println("This is the current adjacency matrix: ");
                                nodeList.get(0).adjMatrix();
                            }
                            else if(outcome.equals("show inactive"))
                            {
                                int count = 0;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(!nodeList.get(i).isOnlineStatus()){
                                        System.out.println(nodeList.get(i).getName()+" is inactive");
                                        count++;
                                    }
                                }
                                System.out.println("There are a total of " + count +" nodes that are inactive\n");
                            }

                            else if(outcome.equals("update"))
                            {
                                readInAttacks();
                                manager.ui.removeNodes();
                                manager.ui.removeLines();
                                drawNodes(manager);
                                drawConnections(manager);
                            }
                            else if(outcome.contains(":"))
                            {
                                String nodeCommand[] = outcome.split(":");
                                String nodeDest[] = nodeCommand[1].split(">");
                                Node node = null;
                                Node destNode = null;
                                for(int i = 0;i<nodeList.size();i++)
                                {
                                    if(nodeDest[0].equals(nodeList.get(i).getName())){
                                        node = nodeList.get(i);
                                    }
                                }
                                if(nodeDest.length>1){
                                    for(int i = 0;i<nodeList.size();i++)
                                    {
                                        if(nodeDest[1].equals(nodeList.get(i).getName())){
                                            destNode = nodeList.get(i);
                                        }
                                    }
                                }
                                if ((node == null) || ((destNode == null)&&(nodeDest.length>1))) {
                                }
                                else if(nodeCommand[0].equals("show status")){
                                    System.out.println(node.getName()+"'s online status is currently: "+ node.getOnlineStatus());
                                }
                                else if(nodeCommand[0].equals("show alerts")){
                                    System.out.println(node.getName()+" currently has "+ node.getNumberOfAlerts() + " alerts");
                                }
                                else if(nodeCommand[0].equals("show saferoutes")){
                                    if(node.numAttacks>0){
                                        System.out.println("Cannot display safe routes between " + node.getName() + " and " + destNode.getName() + ", "+ node.getName() + " has a virus");
                                    }
                                    else if(destNode.numAttacks>0){
                                        System.out.println("Cannot display safe routes between " + node.getName() + " and " + destNode.getName() + ", "+ destNode.getName() + " has a virus");
                                    }
                                    else{
                                        System.out.println("Safe routes between " + node.getName() + " and " + destNode.getName() + " are:");
                                        node.allSafeRoutes(destNode);
                                    }

                                }
                                else if(nodeCommand[0].equals("show firewalllog")){
                                    if (node.getFirewallStatus()){
                                        System.out.println( node.getName()+ " has a firewall:");
                                        node.sortAttacksByColour();
                                    }
                                    else{
                                        System.out.println("There is no firewall on this " + node.getName());
                                    }
                                }
                                else if(nodeCommand[0].equals("show viruses")){
                                    if (node.numAttacks==0){
                                        System.out.println("There are no viruses on " + node.getName());
                                    }
                                    System.out.println(node.getName() + " has the following viruses:");
                                    node.sortAttacksByColour();
                                }
                                else{
                                    System.out.println("General Commands: ");
                                    System.out.println("show names");
                                    System.out.println("show connections");
                                    System.out.println("show xypos");
                                    System.out.println("show latlon");
                                    System.out.println("show attacks");
                                    System.out.println("show onlinestatus");
                                    System.out.println("show firewall");
                                    System.out.println("show firewalllog");
                                    System.out.println("show firewallattacked");
                                    System.out.println("show infected");
                                    System.out.println("show inactive");
                                    System.out.println("show outbreaks");
                                    System.out.println("show adjmatrix");

                                    System.out.println("\nNode Commands: ");
                                    System.out.println("show status:[node_name_here]");
                                    System.out.println("show alerts:[node_name_here]");
                                    System.out.println("show show firewalllog:[node_name_here]");
                                    System.out.println("show viruses:[node_name_here]");
                                    System.out.println("show saferoutes:[origin_node_name_here]>[destination_node_name_here]");
                                    System.out.println("");
                                }

                            }
                            else
                            {
                                System.out.println("General Commands: ");
                                System.out.println("show names");
                                System.out.println("show connections");
                                System.out.println("show xypos");
                                System.out.println("show latlon");
                                System.out.println("show attacks");
                                System.out.println("show onlinestatus");
                                System.out.println("show firewall");
                                System.out.println("show firewalllog");
                                System.out.println("show firewallattacked");
                                System.out.println("show infected");
                                System.out.println("show inactive");
                                System.out.println("show outbreaks");
                                System.out.println("show adjmatrix");

                                System.out.println("\nNode Commands: ");
                                System.out.println("show status:[node_name_here]");
                                System.out.println("show alerts:[node_name_here]");
                                System.out.println("show show firewalllog:[node_name_here]");
                                System.out.println("show viruses:[node_name_here]");
                                System.out.println("show saferoutes:[origin_node_name_here]>[destination_node_name_here]");
                                System.out.println("");
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
