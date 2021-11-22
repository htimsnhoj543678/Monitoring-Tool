package monitoring.tool;

public class Attack
{
    private String name;
    private String colorType;
    private String date;
    private String time;

    public Attack(String name, String colorType, String date, String time)
    {
        this.name = name;
        this.colorType = colorType;
        this.date = date;
        this.time = time;
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
}
