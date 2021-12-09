package monitoring.tool;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

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

    ArrayList<Node> nodeList= new ArrayList();

    private ArrayList<Attack> firewallLog = new ArrayList<>();      //list of previous attacks if 'firewall' is set to true
    private Color color;
    private int numberOfAlerts;
    public int numAttacks;

    //constructor
    public Node (String name, double lat, double lon, boolean firewallStatus)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.firewallStatus = firewallStatus;
        this.onlineStatus = true;
        this.outbreakStatus = false;
        this.color = Color.green;
        this.numAttacks = 0;
    }

    //getters
    public boolean getOnlineStatus()
    {
        if (this.onlineStatus == false)//implement min of 2 virus types
        {
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
    public boolean getOutbreakStatus() {return outbreakStatus;}
    public String getName() {return this.name;}
    public int getLon() {return (int)this.lon;}
    public int getLat() {return (int)this.lat;}
    public int getXpos() {return xpos;}
    public int getYpos() {return ypos;}
    public int getNumberOfAlerts() {return numberOfAlerts;}
    public boolean isOnlineStatus() {return onlineStatus;}

    public int getFirewallLogSize() {
        return firewallLog.size();
    }

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
    public ArrayList<Node> getConnectedNodes()
    {
        return connections;
    }
    public Color getColor() {return this.color;}

    public int getNumOfConnections() {return this.numOfConnections;}
    public int getNumOfAttacks() {return numOfAttacks;}
    //public ArrayList getBadConnections() {return badConnections;}
    //public ArrayList getFirewallLog() {return firewallLog;}

    //setters
    public void checkColor()
    {
        if(this.firewallStatus == true & this.onlineStatus == true)
        {
            this.color = Color.blue;
        }
        else if(this.onlineStatus == false)
        {
            this.color = Color.red;
        }
        else
        {
            this.color = Color.green;
        }
    }
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
            this.numAttacks++;
            this.attacks.add(attack);
            this.check2mins();
            this.check4mins();
            this.check6virus();
            if(!this.onlineStatus){
                this.goesInactive();
            }
        }
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
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
                        System.out.println("Alert!!! " + this.getName() + " was injected with at least 2 " + attacks.get(0).getColorType() + " viruses in the last 2 minutes");
                        numberOfAlerts++;
                        break;
                    }
                }
                else {
                    break;
                }

            }
        }
    }
    public void check4mins()
    {
        int counter = 0;
        if(attacks.size() > 4)
        {
            for (int i = attacks.size()-2; i >= 0; i--)
            {
                if (attacks.get(attacks.size()-1).compareDateTime(attacks.get(i)) <= 240)
                {
                    if (attacks.get(attacks.size()-1).getColorType().equals(attacks.get(i).getColorType()))
                    {
                        counter++;
                    }
                    if(counter >= 4){
                        System.out.println("WARNING - OUTBREAK: " + this.getName() + " was injected with at least 4 " + attacks.get(0).getColorType() + " viruses in the last 4 minutes");
                        System.out.println("This is the adjacency matrix before the outbreak: ");
                        adjMatrix();
                        outbreak(attacks.get(attacks.size()-1));
                        System.out.println("This is the adjacency matrix after the outbreak: ");
                        adjMatrix();
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
    public void outbreak(Attack spread){
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setAttacks(new Attack(connections.get(i).getName(), spread.getColorType(), spread.getDate(), spread.getTime()));
            //System.out.println(this.name+" spread something to "+connections.get(i).getName());
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
                    this.color = Color.red;
                    break;
                }
            }
        }
    }
    public ArrayList<Attack> sortAttacksByTime(ArrayList<Attack> outAttacks)
    {
        if (outAttacks.size()>1) {
            boolean finished = false;
            while (!finished) {
                finished = true;
                for (int i = 0; i < outAttacks.size() - 1; i++) {
                    Attack holder;
                    if ((outAttacks.get(i).getTotalDate().compareTo(outAttacks.get(i + 1).getTotalDate())) > 0) {
                        holder = outAttacks.get(i);
                        outAttacks.set(i, attacks.get(i + 1));
                        outAttacks.set(i + 1, holder);
                        finished = false;
                    }
                }
            }
        }
        return outAttacks;
    }

    public void sortAttacksByColour()
    {
        ArrayList<Attack> attackList;
        if(firewallStatus){
            attackList = this.firewallLog;
        }
        else{
            attackList = this.attacks;
        }
        ArrayList<Attack> redAttacks = new ArrayList<>();
        ArrayList<Attack> blueAttacks = new ArrayList<>();
        ArrayList<Attack> yellowAttacks = new ArrayList<>();
        ArrayList<Attack> blackAttacks = new ArrayList<>();
        for (int i = 0; i < attackList.size(); i++) {
            switch (attackList.get(i).getColorType()) {
                case "red" -> redAttacks.add(attackList.get(i));
                case "blue" -> blueAttacks.add(attackList.get(i));
                case "yellow" -> yellowAttacks.add(attackList.get(i));
                case "black" -> blackAttacks.add(attackList.get(i));
            }
        }
        redAttacks = sortAttacksByTime(redAttacks);
        blueAttacks = sortAttacksByTime(blueAttacks);
        yellowAttacks = sortAttacksByTime(yellowAttacks);
        blackAttacks = sortAttacksByTime(blackAttacks);

        ForSorting red = new ForSorting(redAttacks.size(), redAttacks);
        ForSorting blue = new ForSorting(blueAttacks.size(), blueAttacks);
        ForSorting yellow = new ForSorting(yellowAttacks.size(), yellowAttacks);
        ForSorting black = new ForSorting(blackAttacks.size(), blackAttacks);

        ArrayList<ForSorting> toSort = new ArrayList<>();
        toSort.add(red);
        toSort.add(blue);
        toSort.add(yellow);
        toSort.add(black);

        boolean finished = false;
        while (!finished) {
            finished = true;

            for (int i = 0; i < toSort.size() - 1; i++) {
                ForSorting holder;
                if (toSort.get(i).getCount() < toSort.get(i+1).getCount()) {
                    holder = toSort.get(i);
                    toSort.set(i, toSort.get(i + 1));
                    toSort.set(i + 1, holder);
                    finished = false;
                }
            }
        }

        for (int i = 0; i < toSort.size(); i++) {
            for (int j = 0; j < toSort.get(i).getColorAttacks().size(); j++) {

                Attack cAttack = toSort.get(i).getColorAttacks().get(j);
                System.out.println("Attack:: Name: " + cAttack.getName() + " Colour: " + cAttack.getColorType() + " Date: " + cAttack.getDate() + " Time: " + cAttack.getTime());

            }
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
            System.out.println
                    (
                            "CityName: "+firewall.getName()+" |"
                                    +" Colour: " + firewall.getColorType()+" |"
                                    + " Date: " + firewall.getDate()+" |"
                                    + " Time: " + firewall.getTime()
                    );
        }
    }
    public String returnInfo()
    {
        return "Name: "+getName()
                +" Connections:"+getConnections()
                +" Xpos:"+getXpos()
                +" Ypos:"+getYpos()
                +" Lat:"+getLat()
                +" Lon:"+getLon()
                +" Firewall:"+getFirewallStatus()
                +" Num Connections: "+getNumOfConnections();
    }

    //functions
    public void insertConnection(Node node)
    {
        this.connections.add(node);
    }

    public void allSafeRoutes(Node destNode){
        Stack<Node> visitedNodes = new Stack<Node>();
        this.allSafeRoutes(destNode, visitedNodes);

    }
    public void allSafeRoutes(Node destNode, Stack<Node> visitedNodes){

        visitedNodes.push(this);
        if (this == destNode){
            for (int i = 0; i < visitedNodes.size(); i++) {
                System.out.print(visitedNodes.get(i).getName());
                if (i != visitedNodes.size()-1){
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }
        for (int i = 0; i < this.connections.size(); i++) {
            if(!visitedNodes.contains(connections.get(i)) && (connections.get(i).numAttacks == 0)){
                connections.get(i).allSafeRoutes(destNode, visitedNodes);
            }

        }
        visitedNodes.pop();
    }

    public void goesInactive(){
        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = 0; j < nodeList.size(); j++) {
                ArrayList<Node> conn = nodeList.get(i).getConnectedNodes();
                if(nodeList.get(i).getConnectedNodes().contains(this)){
                    conn.remove(this);
                    nodeList.get(i).updateConnections(conn);
                }
            }
        }
    }

    public void updateConnections(ArrayList<Node> connections){
        this.connections = connections;
        this.numOfConnections--;
    }

    public void adjMatrix(){
        System.out.print("X ");
        for (int i = 0; i < nodeList.size(); i++) {
            System.out.print(i + " ");
        }
        System.out.println("");
        for (int i = 0; i < nodeList.size(); i++) {
            System.out.print(i + " ");
            for (int j = 0; j < nodeList.size(); j++) {
                if(nodeList.get(i).getConnectedNodes().contains(nodeList.get(j))){
                    System.out.print("1 ");
                }
                else{
                    System.out.print("0 ");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

}