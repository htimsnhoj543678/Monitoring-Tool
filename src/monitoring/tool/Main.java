package monitoring.tool;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    //make sure there is no blank line at the end of Graph.txt or it will throw an exception
    public static final String graphPath = "src\\resources\\Graph.txt";

    public static ArrayList<Node> nodeList= new ArrayList();
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
                    }

                    int x = manager.ui.lonToX(tempNode.getLon());
                    int y = manager.ui.latToY(tempNode.getLat());
                    //display nodes on map
                    manager.ui.createNodes(manager.ui.lonToX(tempNode.getLon()),manager.ui.latToY(tempNode.getLat()));

                    //set nodes x and y pos
                    tempNode.setXY(manager.ui.lonToX(tempNode.getLon()),manager.ui.latToY(tempNode.getLat()));

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
                    nodeList.get(j).insertConnection(destCity);
                    nodeList.get(j).setNumConnections();
                }
            }
        }
    }
    public static void drawConnections(Manager manager)
    {
        for(int i = 0;i<nodeList.size();i++)                                    //walks through list of nodes
        {
            Node tempNode = nodeList.get(i);                                    //grabs current node
            ArrayList<String> connections = tempNode.getConnections();          //grabs list of connected cities
            int x1 = (manager.ui.lonToX(tempNode.getLon()) );                   //current node xpos
            int y1 = (manager.ui.latToY(tempNode.getLat()) );                   //current node ypos

            for(int j = 0;j<nodeList.size();j++)                                //walks through nodes again for name comparison
            {
                Node tempNode2 = nodeList.get(j);                               //grabs current node
                int x2 = (manager.ui.lonToX(tempNode2.getLon()) );              //grabs x2pos
                int y2 = (manager.ui.latToY(tempNode2.getLat()) );              //grabs y2pos
                if(tempNode.getConnections().contains(tempNode2.getName()))
                {
                    manager.ui.createLine(x1,y1,x2,y2,Color.red);               //draws the line
                }
            }
        }
    }
    public static void main(String[] args)
    {
        Manager manager = new Manager();
        readInNodes(manager);
        readInConnections();
        drawConnections(manager);
        for(int i = 0; i < nodeList.size();i++)
        {
            System.out.println("Name: "+nodeList.get(i).getName()+" Connections:"+nodeList.get(i).getConnections()+" Xpos:"+nodeList.get(i).getXpos()+" Ypos:"+nodeList.get(i).getYpos()+" Lat:"+nodeList.get(i).getLat()+" Lon:"+nodeList.get(i).getLon()+" Firewall:"+nodeList.get(i).getFirewallStatus()+" Num Connections: "+nodeList.get(i).getNumOfConnections());
        }
        System.out.println(manager.ui.cmdInput.getText());

    }

}
