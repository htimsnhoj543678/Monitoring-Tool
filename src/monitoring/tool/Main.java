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

public class Main extends JFrame {
    //make sure there is no blank line at the end of Graph.txt or it will throw an exception
    public static final String graphPath = "src\\resources\\Graph.txt";
    public static final String attackPath = "src\\resources\\Attack.txt";
    public static ArrayList<Node> nodeList= new ArrayList();
    public static ArrayList<Attack> attackList = new ArrayList();

    //reads in from Graph.txt up until "---", makes a node out of each line with lat/lon firewall true/false
    //draws the node xy position on the map
    //adds that node to nodeList
    public static void readInNodes(Manager manager) {
        String cityname;
        double lat;
        double lon;
        String tempString;

        try {
            FileInputStream input = new FileInputStream(graphPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine()) {
                String nextline = scanin.nextLine();
                if(nextline.equals("-------------------------------------")) {
                    break;
                }

                else {
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
                    if(nextline.contains("firewall")) {
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

        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //reads in from Graph.txt after "---", reads origin city and its destination for each line
    //adds destination city to origincitys private "connections" list
    public static void readInConnections() {
        ArrayList<String> connectionsList = new ArrayList<>();
        try {
            FileInputStream input = new FileInputStream(graphPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine()) {
                if(scanin.nextLine().equals("-------------------------------------")) {
                    while(scanin.hasNextLine()) {
                        connectionsList.add(scanin.nextLine());
                    }
                }
            }
            scanin.close();
        }

        catch (IOException e) {
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

    public static void readInAttacks() {
        try {
            FileInputStream input = new FileInputStream(attackPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine()) {
                String line = scanin.nextLine();
                String[] splitAttack = line.split(", ");
                attackList.add(new Attack(splitAttack[0], splitAttack[1], splitAttack[2], splitAttack[3]));

            }
            scanin.close();
        }

        catch (IOException e) {
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

    public static void readInAttacks(String location) {
        String newAttackPath = location;
        ArrayList<Attack> newAttackList = new ArrayList();

        try {
            FileInputStream input = new FileInputStream(newAttackPath);
            Scanner scanin = new Scanner(input);
            while(scanin.hasNextLine()) {
                String line = scanin.nextLine();
                String[] splitAttack = line.split(", ");
                newAttackList.add(new Attack(splitAttack[0], splitAttack[1], splitAttack[2], splitAttack[3]));

            }
            scanin.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        boolean finished = false;
        while (!finished) {
            finished = true;
            for (int i = 0; i < newAttackList.size() - 1; i++) {
                Attack holder;
                if ((newAttackList.get(i).getTotalDate().compareTo(newAttackList.get(i + 1).getTotalDate())) > 0) {
                    holder = newAttackList.get(i);
                    newAttackList.set(i, newAttackList.get(i + 1));
                    newAttackList.set(i + 1, holder);
                    finished = false;
                }
            }
        }

        Queue<Attack> newAttackQueue = new LinkedList<>();
        for (int i = 0; i < newAttackList.size(); i++) {
            newAttackQueue.add(newAttackList.get(i));
        }
        while (!newAttackQueue.isEmpty()){
            for (int i = 0; i <= nodeList.size(); i++)
            {
                if (nodeList.get(i).getName().equals(newAttackQueue.peek().getName()))
                {
                    nodeList.get(i).setAttacks(newAttackQueue.poll());
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
        System.out.println("Menu: \n");
        System.out.println("Enter Help to show commands ");
        System.out.println("load 'filename.txt'    Loads a new attack file ");
        System.out.println("show names             Shows all nodes ");
        System.out.println("show connections       Shows current connections of active nodes");
        System.out.println("show xypos             Shows the coordinates of the nodes ");
        System.out.println("show latlon            Shows real latitudes and longitudes of the nodes");
        System.out.println("show attacks           Shows current city, virus type, date and time");
        System.out.println("show onlinestatus      Shows current online and offline nodes");
        System.out.println("show firewall          Shows current nodes with active firewall");
        System.out.println("show firewalllog       Shows attack attempts on nodes ");
        System.out.println("show firewallattacked  Shows all firewalls with attacks on them");
        System.out.println("show infected          Shows current infection status of nodes");
        System.out.println("show inactive          Shows all inactive nodes");
        System.out.println("show outbreaks         Shows all nodes with outbreaks");
        System.out.println("show adjmatrix         Generates adjacency matrix(ces) that correspond with active connections");

        System.out.println("\nNode Commands: ");
        System.out.println("show status:'node_name_here'                                            Shows current status of selected node");
        System.out.println("show alerts:'node_name_here'                                            Shows alerts of selected node");
        System.out.println("show firewalllog:'node_name_here'                                       Shows attempts at breaching node firewall(s)");
        System.out.println("show viruses:'node_name_here'                                           Shows current virus locations");
        System.out.println("show saferoutes:'origin_node_name_here'>'destination_node_name_here'    Shows all possible safe routs between 2 nodes");
        System.out.println("show shortestpath:'origin_node_name_here'>'destination_node_name_here'  Shows the shortest safest path between 2 nodes ");
        System.out.println("");
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
//"src\\resources\\Attack.txt"
                            else if(outcome.contains("load "))
                            {
                                String loc[] = outcome.split(" ");
                                System.out.println("Reading in new attack file...");
                                readInAttacks("src\\resources\\"+loc[1]);
                                manager.ui.removeNodes();
                                manager.ui.removeLines();
                                drawNodes(manager);
                                drawConnections(manager);
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
                                    if(nodeList.get(i).getFirewallStatus()){
                                        nodeList.get(i).sortAttacksByColour();;
                                    }
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
                                else if(nodeCommand[0].equals("show shortestpath")){
                                    System.out.println("Shortest path between " + node.getName() + " and " + destNode.getName() + " is:");
                                    node.shortestPath(destNode);


                                }
                                else if(nodeCommand[0].equals("show firewalllog")){
                                    if (node.getFirewallStatus()){
                                        System.out.println( node.getName()+ " has a firewall");
                                        node.sortAttacksByColour();
                                    }
                                    else{
                                        System.out.println("There is no firewall on " + node.getName());
                                    }
                                }
                                else if(nodeCommand[0].equals("show viruses")){
                                    if (node.numAttacks==0){
                                        System.out.println("There are no viruses on " + node.getName());
                                    }
                                    else{
                                        System.out.println(node.getName() + " has the following viruses:");
                                        node.sortAttacksByColour();
                                    }
                                }
                                else {
                                    System.out.println("Menu: \n");
                                    System.out.println("Enter Help to show commands ");
                                    System.out.println("load 'filename.txt'    Loads a new attack file ");
                                    System.out.println("show names             Shows all nodes ");
                                    System.out.println("show connections       Shows current connections of active nodes");
                                    System.out.println("show xypos             Shows the coordinates of the nodes ");
                                    System.out.println("show latlon            Shows real latitudes and longitudes of the nodes");
                                    System.out.println("show attacks           Shows current city, virus type, date and time");
                                    System.out.println("show onlinestatus      Shows current online and offline nodes");
                                    System.out.println("show firewall          Shows current nodes with active firewall");
                                    System.out.println("show firewalllog       Shows attack attempts on nodes ");
                                    System.out.println("show firewallattacked  Shows all firewalls with attacks on them");
                                    System.out.println("show infected          Shows current infection status of nodes");
                                    System.out.println("show inactive          Shows all inactive nodes");
                                    System.out.println("show outbreaks         Shows all nodes with outbreaks");
                                    System.out.println("show adjmatrix         Generates adjacency matrix(ces) that correspond with active connections");

                                    System.out.println("\nNode Commands: ");
                                    System.out.println("show status:'node_name_here'                                            Shows current status of selected node");
                                    System.out.println("show alerts:'node_name_here'                                            Shows alerts of selected node");
                                    System.out.println("show firewalllog:'node_name_here'                                       Shows attempts at breaching node firewall(s)");
                                    System.out.println("show viruses:'node_name_here'                                           Shows current virus locations");
                                    System.out.println("show saferoutes:'origin_node_name_here'>'destination_node_name_here'    Shows all possible safe routs between 2 nodes");
                                    System.out.println("show shortestpath:'origin_node_name_here'>'destination_node_name_here'  Shows the shortest safest path between 2 nodes ");
                                    System.out.println("");
                                }
                            }
                            else {
                                System.out.println("Menu: \n");
                                System.out.println("Enter Help to show commands ");
                                System.out.println("load 'filename.txt'    Loads a new attack file ");
                                System.out.println("show names             Shows all nodes ");
                                System.out.println("show connections       Shows current connections of active nodes");
                                System.out.println("show xypos             Shows the coordinates of the nodes ");
                                System.out.println("show latlon            Shows real latitudes and longitudes of the nodes");
                                System.out.println("show attacks           Shows current city, virus type, date and time");
                                System.out.println("show onlinestatus      Shows current online and offline nodes");
                                System.out.println("show firewall          Shows current nodes with active firewall");
                                System.out.println("show firewalllog       Shows attack attempts on nodes ");
                                System.out.println("show firewallattacked  Shows all firewalls with attacks on them");
                                System.out.println("show infected          Shows current infection status of nodes");
                                System.out.println("show inactive          Shows all inactive nodes");
                                System.out.println("show outbreaks         Shows all nodes with outbreaks");
                                System.out.println("show adjmatrix         Generates adjacency matrix(ces) that correspond with active connections");

                                System.out.println("\nNode Commands: ");
                                System.out.println("show status:'node_name_here'                                            Shows current status of selected node");
                                System.out.println("show alerts:'node_name_here'                                            Shows alerts of selected node");
                                System.out.println("show firewalllog:'node_name_here'                                       Shows attempts at breaching node firewall(s)");
                                System.out.println("show viruses:'node_name_here'                                           Shows current virus locations");
                                System.out.println("show saferoutes:'origin_node_name_here'>'destination_node_name_here'    Shows all possible safe routs between 2 nodes");
                                System.out.println("show shortestpath:'origin_node_name_here'>'destination_node_name_here'  Shows the shortest safest path between 2 nodes ");
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
    }
}
