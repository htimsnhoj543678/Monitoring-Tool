package monitoring.tool;
import java.util.ArrayList;

public class ForSorting {
    private int count;
    private ArrayList<Attack> colorAttacks;

    public ForSorting(int size, ArrayList<Attack> colorAttacks) {
        this.count = size;
        this.colorAttacks = colorAttacks;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Attack> getColorAttacks() {
        return colorAttacks;
    }
}
