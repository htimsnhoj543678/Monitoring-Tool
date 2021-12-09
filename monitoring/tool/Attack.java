package monitoring.tool;

import java.util.Calendar;

public class Attack
{
    private String name;
    private String colorType;
    private String date;
    private String time;
    private Calendar totalDate;

    public Attack(String name, String colorType, String date, String time)
    {
        this.name = name;
        this.colorType = colorType;
        this.date = date;
        this.time = time;
        setTotalDate(date, time);
    }

    public String getName() {
        return name;
    }

    public String getColorType() {
        return colorType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Calendar getTotalDate() {
        return totalDate;
    }

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

    public long compareDateTime(Attack that)
    {
        //returns the number of seconds between the current attack and the given attack
        //seconds
        return (this.totalDate.getTimeInMillis() - that.getTotalDate().getTimeInMillis())/1000;
    }
}
