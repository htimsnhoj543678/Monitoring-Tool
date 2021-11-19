package monitoring.tool;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Math.*;
import java.util.Scanner;

public class Main
{
    public static void readInNodes (Manager manager)
    {
        String cityname;
        double lat;
        double lon;
        String tempString;

        try {
            FileInputStream input = new FileInputStream("C:\\Users\\braes\\Desktop\\school\\Yr3\\Term Project\\src\\resources\\Graph.txt");
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
                    String parts[] = nextline.split(", ");
                    cityname = parts[0];

                    //lat and lon
                    String parts2[] = nextline.split("[\\(\\)]");
                    tempString = parts2[1];
                    String parts3[] = tempString.split(", ");
                    lat = Double.parseDouble(parts3[0]);
                    lon = Double.parseDouble(parts3[1]);

                    Node tempNode = new Node(cityname,lat,lon,false);
                    if(nextline.contains("firewall"))
                    {
                        tempNode.setFirewallStatus(true);
                    }
                    manager.ui.createNodes(manager.ui.lonToX(tempNode.getLon()),manager.ui.latToY(tempNode.getLat()));
                    System.out.println(tempNode.getName()+" "+tempNode.getFirewallStatus());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Manager manager = new Manager();
        readInNodes(manager);

    }
}
