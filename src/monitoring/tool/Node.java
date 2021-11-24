package monitoring.tool;
import java.util.ArrayList;

public class Node
{
    private String name;                                            //name of the node eg: "Hong Kong"
    private double lat;                                             //type may be changed, will be latitude on map
    private double lon;                                             //type may be changed, will be longitude on map
    private int xpos;
    private int ypos;
    private boolean onlineStatus;                                   //default true or false if node has 6 or more injections with a min of 2 types of viruses
    private boolean firewallStatus;                                 //true (stops attacks)
    private boolean outbreakStatus;                                 //default false or true if node has more than 4 injections of the same virus in 4 mins
    private int numOfConnections;                                   //number of other nodes this node is connected to
    private ArrayList<Node> connections = new ArrayList<>();        //list of connected nodes
    private ArrayList<Attack> attacks = new ArrayList<>();          //list of attacks
    private int numOfAttacks;                                       //number of attacks on this node
    //private ArrayList<Node> badConnections;                       //list of connected nodes which are infected
    private ArrayList<Attack> firewallLog = new ArrayList<>();      //list of previous attacks if 'firewall' is set to true

    //constructor
    public Node (String name, double lat, double lon, boolean firewallStatus)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.firewallStatus = firewallStatus;
        this.onlineStatus = true;
        this.outbreakStatus = false;
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
    public ArrayList<String> getConnections()
    {
        ArrayList<String> connectedNames = new ArrayList<>();
        if(connections == null)
        {
            connectedNames.add("");
            this.numOfConnections = 0;
        }
        else
        {
            for (int i = 0; i < connections.size(); i++)
            {
                connectedNames.add(connections.get(i).getName());
            }
        }
        return connectedNames;
    }

    public int getNumOfConnections() {return this.numOfConnections;}
    public int getNumOfAttacks() {return numOfAttacks;}
    //public ArrayList getBadConnections() {return badConnections;}
    //public ArrayList getFirewallLog() {return firewallLog;}

    //setters
    public void setOnlineStatus(boolean status) {this.onlineStatus = status;}
    public void setFirewallStatus(boolean status) {this.firewallStatus = status;}
    public void setOutbreakStatus(boolean status) {this.outbreakStatus = status;}
    public void setXY(int x, int y){this.xpos=x; this.ypos=y;}
    public void setNumConnections()
    {
        this.numOfConnections = 0;
        for(int i = 0; i < this.connections.size();i++)
            this.numOfConnections++;
    };
    public void setAttacks(Attack attack)
    {
        if (firewallStatus)
        {
            this.firewallLog.add(attack);
        }
        else if(this.onlineStatus){
            this.attacks.add(attack);
            this.check2mins();
            this.check4mins();
            this.check6virus();
        }
    }
    public void check2mins()
    {

        if(attacks.size() >1)
        {
            for (int i = attacks.size()-2; i >= 0; i--)
            {
                if (attacks.get(attacks.size()-1).compareDateTime(attacks.get(i)) <= 120)
                {

                    if (attacks.get(attacks.size()-1).getColorType().equals(attacks.get(i).getColorType()))
                    {
                        //System.out.println("WARNING: " + this.getName() + " was injected with at least 2 " + attacks.get(0).getColorType() + " viruses in the last 2 minutes");
                        break;
                    }
                } else {
                    break;
                }

            }
        }
    }
    public void check4mins()
    {
        if(attacks.size() > 4)
        {
            for (int i = attacks.size()-2; i >= attacks.size()-4; i--)
            {
                if (attacks.get(attacks.size()-1).compareDateTime(attacks.get(i)) <= 240)
                {

                    if (attacks.get(attacks.size()-1).getColorType().equals(attacks.get(i).getColorType()))
                    {
                        //System.out.println("WARNING: " + this.getName() + " was injected with at least 4 " + attacks.get(0).getColorType() + " viruses in the last 4 minutes");
                        //TODO: implement the outbreak stuff
                        this.outbreakStatus = true;
                        break;
                    }
                }
                else
                {
                    break;
                }

            }
        }
    }
    public void check6virus()
    {
        if(attacks.size() > 5)
        {
            for (int i = 0; i < attacks.size(); i++)
            {
                if ((attacks.get(0).getColorType().equals(attacks.get(i).getColorType())))
                {
                    this.onlineStatus = false;
                    this.numOfConnections = 0;
                    //System.out.println(this.getName() + " is now OFFLINE");
                    this.connections.removeAll(connections);
                    break;
                }
            }
        }
    }
    public void sortAttacks()
    {
        //TODO: Optimize this sort to reduce time or this sort might not be needed.
        if (this.attacks.size()>1)
        {
            int pos = 0;
            for (int j = 0; j < this.attacks.size(); j++)
            {
                Attack lowest = this.attacks.get(j);
                for (int i = j; i < this.attacks.size(); i++)
                {
                    if(0<(lowest.getTotalDate().compareTo(this.attacks.get(i).getTotalDate())));
                    lowest = this.attacks.get(i);
                    System.out.println(lowest.getTime());
                    pos = i;
                }
                this.attacks.set(pos,  attacks.get(j));
                this.attacks.set(j, lowest);
            }
            printAttacks();
        }

    }
    //print attack
    public void printAttacks()
    {

        for (int i = 0; i < attacks.size(); i++) {
            Attack cAttack = attacks.get(i);
            System.out.println("Attack:: Name: " + cAttack.getName() + " Colour: " + cAttack.getColorType() + " Date: " + cAttack.getDate() + " Time: " + cAttack.getTime());
        }
    }
    //print firewall
    public void printFirewall()
    {
        for (int i = 0; i < firewallLog.size(); i++)
        {
            Attack firewall = firewallLog.get(i);
            System.out.println("Firewall:: Name: " + firewall.getName() + " Colour: " + firewall.getColorType() + " Date: " + firewall.getDate() + " Time: " + firewall.getTime());
        }
    }
    //functions
    public void insertConnection(Node node)
    {
        this.connections.add(node);
    }

}
