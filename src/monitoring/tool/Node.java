package monitoring.tool;
import java.util.ArrayList;
import java.util.Arrays;

public class Node
{
    private String name;                                            //name of the node eg: "Hong Kong"
    private double lat;                                             //type may be changed, will be latitude on map
    private double lon;                                             //type may be changed, will be longitude on map
    private int xpos;
    private int ypos;
    private boolean onlineStatus = true;                            //default true or false if node has 6 or more injections with a min of 2 types of viruses
    private boolean firewallStatus;                                 //true (stops attacks)
    private boolean outbreakStatus = false;                         //default false or true if node has more than 4 injections of the same virus in 4 mins
    private int numOfConnections;                                   //number of other nodes this node is connected to
    private ArrayList<String> connections = new ArrayList<>();                                     //list of connected nodes
    private ArrayList<Attack> attacks = new ArrayList<>();                                       //list of attacks
    //private ArrayList<String> attackList = new ArrayList<String>(); //type of attack: red, blue, yellow, or black, can then count # of attacks from this
    private int numOfAttacks;                                       //number of attacks on this node
    //private ArrayList<Node> badConnections;                         //list of connected nodes which are infected
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
    public int getXpos() {return xpos;}
    public int getYpos() {return ypos;}
    public ArrayList<String> getConnections() {return connections;}

    //public String getAttackType() {return attackType;}
    public int getNumOfConnections() {return numOfConnections;}
    public int getNumOfAttacks() {return numOfAttacks;}
    //public ArrayList getBadConnections() {return badConnections;}
    //public ArrayList getFirewallLog() {return firewallLog;}

    //setters
    public void setOnlineStatus(boolean status) {this.onlineStatus = status;}
    public void setFirewallStatus(boolean status) {this.firewallStatus = status;}
    public void setOutbreakStatus(boolean status) {this.outbreakStatus = status;}
    public void setXY(int x, int y){this.xpos=x; this.ypos=y;}
    public void setAttacks(Attack attack) {
        this.attacks.add(attack);
    }

    //print attack
    public void printAttacks(){
        for (int i = 0; i < attacks.size(); i++) {
            Attack cAttack = attacks.get(i);
            System.out.println("Attack:: Name: " + cAttack.getName() + " Colour: " + cAttack.getColorType() + " Date: " + cAttack.getDate() + " Time: " + cAttack.getTime());
        }
    }
    //functions
    public void insertConnection(String node)
    {
        this.connections.add(node);
    }

}
