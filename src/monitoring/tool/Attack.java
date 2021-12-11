package monitoring.tool;
import java.util.Calendar;

/**
 * this is the attack class, it takes in a string and then creates an attack object
 */

public class Attack
{
    /**
     * variables of an Attack object
     */
    private String name;
    private String colorType;
    private String date;
    private String time;
    private Calendar totalDate;
    private boolean usedForOutbreak;

    /**
     * attack constructor
     * @param name node that is being attacked
     * @param colorType colour of attack
     * @param date the date of the attack
     * @param time the time of the attack
     */
    public Attack(String name, String colorType, String date, String time)
    {
        this.name = name;
        this.colorType = colorType;
        this.date = date;
        this.time = time;
        setTotalDate(date, time);
        usedForOutbreak = false;
    }

    /**
     * getters for the variables
     */
    public String getName() {return name;}
    public String getColorType() {return colorType;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public Calendar getTotalDate() {return totalDate;}

    public boolean isUsedForOutbreak() {
        return usedForOutbreak;
    }

    public void setUsedForOutbreak(boolean usedForOutbreak) {
        this.usedForOutbreak = usedForOutbreak;
    }

    /**
     * this function converts the strings of date and time into a Calendar object and saves it for future use.
     * @param date date of the attack
     * @param time time of the attack
     */
    public void setTotalDate(String date, String time)
    {
        String thisTime[] = time.split(":");
        String thisDate[] = date.split("-");
        int sec = Integer.parseInt(thisTime[2]);
        int min = Integer.parseInt(thisTime[1]);
        int hrs = Integer.parseInt(thisTime[0]);
        int day = Integer.parseInt(thisDate[2]);
        int month = Integer.parseInt(thisDate[1]);
        int year = Integer.parseInt(thisDate[0]);//(year, month, day, hrs, min, sec)
        this.totalDate = Calendar.getInstance();
        this.totalDate.set(year-1900, month-1, day, hrs, min, sec);
        //System.out.println(totalDate.getTime());
    }

    /**
     * this function compares the current attack's date and time against a given attack's date and time.
     * @param that the attack who's date we want to compare to
     * @return returns the difference in the date and time in seconds.
     */
    public long compareDateTime(Attack that)
    {
        //returns the number of seconds between the current attack and the given attack
        //seconds
        return (this.totalDate.getTimeInMillis() - that.getTotalDate().getTimeInMillis())/1000;
    }
}
