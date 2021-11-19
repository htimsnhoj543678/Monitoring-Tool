package monitoring.tool;
import java.util.ArrayList;
import java.util.Arrays;

public class Node
{
    private String name;                                            //name of the node eg: "Hong Kong"
    private double lat;                                             //type may be changed, will be latitude on map
    private double lon;                                             //type may be changed, will be longitude on map
    private boolean onlineStatus = true;                            //default true or false if node has 6 or more injections with a min of 2 types of viruses
    private boolean firewallStatus;                                 //true (stops attacks)
    private boolean outbreakStatus = false;                         //default false or true if node has more than 4 injections of the same virus in 4 mins
    private int numOfConnections;                                   //number of other nodes this node is connected to
    private Node[] connections;                                     //list of connected nodes
    private Attack[] attacks;                                       //list of attacks
    private ArrayList<String> attackList = new ArrayList<String>(); //type of attack: red, blue, yellow, or black, can then count # of attacks from this
    private int numOfAttacks;                                       //number of attacks on this node
    private ArrayList<Node> badConnections;                         //list of connected nodes which are infected
    //private ArrayList<Attack> firewallLog;                        //list of previous attacks if 'firewall' is set to true

    //constructor
    public Node (String name, double lat, double lon, boolean firewallStatus)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.firewallStatus = firewallStatus;
    }

    //getters
    public boolean getOnlineStatus()
    {
        if (this.onlineStatus == false || this.numOfAttacks >= 6)//implement min of 2 virus types
        {
            this.onlineStatus = false;
            this.connections = null;
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean getFirewallStatus()
    {
        return this.firewallStatus;
    }
    public boolean getOutbreakStatus()
    {
        if(numOfAttacks >= 4)
        {
            System.out.println("System is having an outbreak.");
            return true;
        }
        else
        {
            System.out.println("System is not having an outbreak.");
            return false;
        }
    }
    public String getName() {return this.name;}
    public int getLon() {return (int)this.lon;}
    public int getLat() {return (int)this.lat;}
    public Node[] getConnections() {return connections;}
    public void printConnections()
    {
        for(int i = 0; i <connections.length; i++)
        {
            if(connections[i] != null)
            System.out.println(connections[i].name);
        }
    }
    //public String getAttackType() {return attackType;}
    public int getNumOfConnections() {return numOfConnections;}
    public int getNumOfAttacks() {return numOfAttacks;}
    public ArrayList getBadConnections() {return badConnections;}
    //public ArrayList getFirewallLog() {return firewallLog;}

    //setters
    public void setOnlineStatus(boolean status) {this.onlineStatus = status;}
    public void setLonLat(double lat, double lon){this.lat=lat; this.lon=lon;}
    public void setFirewallStatus(boolean status) {this.firewallStatus = status;}
    public void setOutbreakStatus(boolean status) {this.outbreakStatus = status;}
    public void setNumConnections(int size)
    {
        this.connections = new Node[size];
        Arrays.fill(this.connections, null);
    }

    //functions
    public void insertConnection(Node node)
    {
        for(int i = 0; i<connections.length; i++)
        {
            if(this.connections[i] == null)
            {
                this.connections[i] = node;
                System.out.println("Inserted "+node.getName()+" at index: " +i);
                break;
            }
        }
    }

    public ArrayList<String> addInjection(String colorType)
    {
        this.numOfAttacks++;
        this.attackList.add(colorType);
        return attackList;
    }
}
