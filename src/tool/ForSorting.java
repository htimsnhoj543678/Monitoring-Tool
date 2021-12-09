package monitoring.tool;
import java.util.ArrayList;
/**this class is used when sorting attacks by colour then time, it acts similar to how a tuple would in python by
 * joining 2 datatype, so they can be referenced with each other. this class is use primarily in the class Node**/
public class ForSorting {
    private int count;
    private ArrayList<Attack> colorAttacks;

    /**
     * constructor
     * @param size the size of colorAttacks
     * @param colorAttacks arraylist of attacks that have been presorted by colour
     */
    public ForSorting(int size, ArrayList<Attack> colorAttacks) {
        this.count = size;
        this.colorAttacks = colorAttacks;
    }

    /**
     * getters for the variables
     */
    public int getCount() {
        return count;
    }
    public ArrayList<Attack> getColorAttacks() {
        return colorAttacks;
    }
/**------------------------------------------------------------------------------------------------------------------**/
}
