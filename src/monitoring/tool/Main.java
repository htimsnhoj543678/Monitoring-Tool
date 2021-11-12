package monitoring.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        //testing of node logic
        Node node1 = new Node("Hong Kong",1,1,true, false, false,2);
        Node node2 = new Node("New York",1,2,true, false, false,0);
        Node node3 = new Node("Amsterdam",1,3,true, false, false,0);

        node1.setNumConnections(5);
        node1.insertConnection(node2);
        node1.insertConnection(node3);
        node1.setFirewallStatus(true);
        System.out.println("\n");

        System.out.println(node1.getName()+" is online: "+node1.getOnlineStatus());
        node1.getFirewallStatus();
        node1.getOutbreakStatus();
        System.out.println("Current connections: ");
        node1.printConnections();
        System.out.println("\n");

        System.out.println(node1.addInjection("red"));
        System.out.println(node1.getNumOfAttacks());
    }
}
