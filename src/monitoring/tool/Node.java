package monitoring.tool;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Node
{
    /**
     * variables for the Node class
     */
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
    ArrayList<Node> nodeList= new ArrayList();
    private ArrayList<Attack> firewallLog = new ArrayList<>();      //list of previous attacks if 'firewall' is set to true
    private Color color;
    private int numberOfAlerts;
    public int numAttacks;
/**------------------------------------------------------------------------------------------------------------------**/
    /**
     * constructor method for a Node object
     * @param name the name of the node
     * @param lat latitude of node
     * @param lon longitude of node
     * @param firewallStatus boolean on whether there is a firewall on the node
     */
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

    /**
     * the following are all getter methods for assorted private variables in the Node class.
     */
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
/**------------------------------------------------------------------------------------------------------------------**/
    /**
     * this method is used to check the colour of a node on the graph within the GUI.
     */
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

    /**
     * the following methods are all setters for assorted private variables in the Node class
     *
     */
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
    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
    /**----------------------------------------------------------------------------------------------**/

    /**
     * this function handles inserting attacks into the attack list and determines what happens when said attack is inserted.
     * this could be nothing, alarms, outbreaks, or even going offline.
     * @param attack that is to be inserted into
     */
    public void setAttacks(Attack attack)
    {
        if (firewallStatus)
        {
            this.firewallLog.add(attack);
            if (firewallLog.size()>1){
                if((firewallLog.get(firewallLog.size()-2).getColorType().equals(attack.getColorType()) && (firewallLog.get(firewallLog.size()-2).compareDateTime(attack) == 0))){
                    this.firewallLog.remove(attack);
                }
            }
        }
        else if(this.onlineStatus){
            this.numAttacks++;
            this.attacks.add(attack);
            if (numAttacks>1){
                if(!(attacks.get(numAttacks-2).getColorType().equals(attack.getColorType()) && (attacks.get(numAttacks-2).compareDateTime(attack) == 0))){
                    this.check2mins();
                    this.check4mins();
                    this.check6virus();
                    if(!this.onlineStatus){
                        this.goesInactive();
                    }
                }
                else{
                    this.numAttacks--;
                    this.attacks.remove(attack);
                }
            }
            else{
                this.check2mins();
                this.check4mins();
                this.check6virus();
                if(!this.onlineStatus){
                    this.goesInactive();
                }
            }
        }
    }
    /**
     * this function outputs an alert when 2 or more viruses have occurred on the same node in the past 2 minutes.
     */
    public void check2mins()
    {
        int counter = 1;

        if(attacks.size() >2)
        {
            for (int i = attacks.size()-2; i >= 0; i--)
            {
                if (attacks.get(attacks.size()-1).compareDateTime(attacks.get(i)) <= 120)
                {
                    if (attacks.get(attacks.size()-1).getColorType().equals(attacks.get(i).getColorType())) {
                        counter++;
                    }
                    if (counter > 2){
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

    /**
     * this function checks if there were 4 attacks of the same colour in the last 4 minutes. If this occurs then the
     * current adjacency matrix is shown, and outbreak occurs, and then the adjacency matrix is output for after the outbreak
     * to see how to connections have changed.
     */
    public void check4mins()
    {
        int counter = 1;
        if(attacks.size() > 3)
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
                        System.out.println("This is the adjacency matrix before the "+ attacks.get(attacks.size()-1).getColorType()+" outbreak from Node: " +this.getName() +" at: " + attacks.get(attacks.size()-1).getDate()+" "+attacks.get(attacks.size()-1).getTime());
                        adjMatrix();
                        outbreak(attacks.get(attacks.size()-1));
                        System.out.println("This is the adjacency matrix after the "+ attacks.get(attacks.size()-1).getColorType()+" outbreak from Node: " +this.getName() +" at: " + attacks.get(attacks.size()-1).getDate()+" "+attacks.get(attacks.size()-1).getTime());

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

    /**
     * this is the method to spread an outbreak when there are more than 4 viruses
     * @param spread the attack that is being spread to adjacent nodes.
     */
    public void outbreak(Attack spread){
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setAttacks(new Attack(connections.get(i).getName(), spread.getColorType(), spread.getDate(), spread.getTime()));
            //System.out.println(this.name+" spread something to "+connections.get(i).getName());
        }
    }

    /**
     * the function checks to see if there are least 6 virus of 2 different colours on a node. if this is true the node
     * is set to offline, and the connections are removed. it is called by setAttacks()
     */
    public void check6virus()
    {
        if(attacks.size() > 5)
        {
            for (int i = 0; i < attacks.size(); i++)
            {
                if (!(attacks.get(0).getColorType().equals(attacks.get(i).getColorType())))
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

    /**
     *this funtion sorts the provided arraylist of attacks by cronilogical order.
     * @param outAttacks the arraylist of attacks that need to be sorted
     * @return sorted arraylist of attacks
     */
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

    /**
     * this function sorts all the attacks on a node into separate arraylists by using a switch case.
     * the arraylists are then ordered from largest to smallest and then the attacks within the lists are sorted by time.
     * after this, all the attacks are output.
     */
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

    /**
     * outdated method for printing the attack list of a given node. used mostly for testing.
     */
    public void printAttacks()
    {
        for (int i = 0; i < attacks.size(); i++) {
            Attack cAttack = attacks.get(i);
            System.out.println("Attack:: Name: " + cAttack.getName() + " Colour: " + cAttack.getColorType() + " Date: " + cAttack.getDate() + " Time: " + cAttack.getTime());
        }
    }


    /**
     * outdated method for printing the firewall log of a given node. used mostly for testing.
     */
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

    /**
     * this method is called when a node is hovered over
     * @return string with the relevant information.
     */
    public String returnInfo()
    {
        return
                " Connections:"+getConnections()
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

    /**
     * this method finds all the safe routes from a given node to a destination node. this is done by calling a polymorphic
     * version of the allSafeRoutes funtion.
     * @param destNode destination node
     */
    public void allSafeRoutes(Node destNode){
        Stack<Node> visitedNodes = new Stack<Node>();
        int x = this.allSafeRoutes(destNode, visitedNodes);
        if (x<0){
            System.out.println("There is no safe path");
        }

    }

    /**
     * this is the alternate version of allSafeRoutes which is called on each of the relevant nodes. overall, this method
     * performs a depth first search.
     * @param destNode the node we are searching for
     * @param visitedNodes a stack of the nodes previously visited.
     * @return returns -1 if no route is found, returns 1 if a route is found
     */
    public int allSafeRoutes(Node destNode, Stack<Node> visitedNodes){
        int out = -1;
        visitedNodes.push(this);
        if (this == destNode){
            for (int i = 0; i < visitedNodes.size(); i++) {
                System.out.print(visitedNodes.get(i).getName());
                if (i != visitedNodes.size()-1){
                    System.out.print(", ");
                }
            }
            System.out.println("");
            out = 1;
        }
        for (int i = 0; i < this.connections.size(); i++) {
            if(!visitedNodes.contains(connections.get(i)) && (connections.get(i).numAttacks == 0)){
                if(out != 1){
                    out = connections.get(i).allSafeRoutes(destNode, visitedNodes);
                }
                else{
                    connections.get(i).allSafeRoutes(destNode, visitedNodes);
                }
            }

        }
        visitedNodes.pop();
        return out;
    }

    /**
     * shortestPath is a polymorphic function which finds the shortest path from one node to another by calling an
     * alternate version of shortestPath. This gives the node before the destination node and is done until the full path
     * is found.
     * @param destNode the destination node
     */
    public void shortestPath(Node destNode){
        //show saferoutes:Miami>Sao Paulo
        Stack<Node> visitedNodes = new Stack<>();
        ArrayList<Node> path = new ArrayList<>();
        path.add(destNode);
        Queue<Node> toVisit = new LinkedList<>();
        toVisit.add(this);
        if (checkForPath(destNode, visitedNodes)>0){
            visitedNodes.clear();

            while (!path.get(path.size()-1).getName().equals(this.getName())){
                destNode = shortestPath(destNode,visitedNodes , path, toVisit);
                path.add(destNode);
                visitedNodes.clear();
                toVisit.clear();

            }

            for (int i = path.size()-1; i >=0; i--) {
                System.out.print(path.get(i).getName());
                if (i != 0){
                    System.out.print(", ");
                }
            }
            System.out.println("");
            }

        else{
            System.out.println("There is no path available");


        }
    }

    /**
     * this is the seccond polymorphic version of shortestPath which is called on the required nodes inorder to find a
     * shortest path to the destination node
     * @param destNode Destination node
     * @param visitedNodes Stack of previously visited nodes
     * @param path The current path to the destination node
     * @param toVisit Queue of nodes to visit when the time comes
     * @return The node prior to the destination node in the shortest path which is then fed back in to find the path to
     * it until the full path is found
     */
    public Node shortestPath(Node destNode, Stack<Node> visitedNodes , ArrayList<Node>path, Queue<Node>toVisit){
        visitedNodes.push(this);
        if(this.connections.contains(destNode)){

            return this;
        }
        for (int i = 0; i < this.connections.size(); i++) {
            if (!visitedNodes.contains(connections.get(i))){
                toVisit.add(connections.get(i));
            }
        }
        Node temp = path.get(path.size()-1);
        while(!toVisit.isEmpty() && (path.get(path.size()-1) == temp)){
            Node x =toVisit.poll().shortestPath(destNode, visitedNodes , path, toVisit);
            if (x != path.get(0)){
                return x;
            }

        }
        return path.get(path.size()-1);
    }

    /**
     * Function checkForPath(Node destNode, Stack<Node> visitedNodes) is used by the shortest path method to determine
     * if there are any paths at all available. This is done by calling checkForPath(Node destNode, Stack<Node> visitedNodes)
     * on each of the relevant nodes.
     * @param destNode The destination node
     * @param visitedNodes a stack of nodes previously visited
     * @return -1 if no path was found, 1 if path was found
     */
    public int checkForPath(Node destNode, Stack<Node> visitedNodes) {

        int out = -1;
        visitedNodes.push(this);
        if (this == destNode) {
            out = 1;
        }
        for (int i = 0; i < this.connections.size(); i++) {
            if (!visitedNodes.contains(connections.get(i))) {
                if (out != 1) {
                    out = connections.get(i).checkForPath(destNode, visitedNodes);
                }
            }
        }
        visitedNodes.pop();
        return out;
    }
    /**
     * goesInactive() is called when a node goes offline. The function allows a node to remove itself from the
     * all other nodes connections list by calling updateConnections(ArrayList<Node> connections) on each of the
     * relevant nodes.
     * **/
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

    /**
     * This function is called by goesInactive() and changes a given node's connections to the provided connection
     * and then increments the numOfConnections variable accordingly
     * @param connections the connections list that you want to set the node to have.
     */
    public void updateConnections(ArrayList<Node> connections){

        this.connections = connections;
        this.numOfConnections--;
    }

    /**
     * This function generates the adjacency matrix from the graph. All nodes will output the same adjacency matrix
     * if they are using the same data.
     */
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