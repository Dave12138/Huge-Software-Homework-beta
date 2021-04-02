package DataType;

import java.util.Calendar;

public class Train {
    private long uid;
    private String name;
    private int from;
    private int to;
    private Calendar launchTime;
    private int minuteCost;

    public Train(long uid, String name, int from, int to, Calendar launchTime, int minuteCost) {
        this.uid = uid;
        this.name = name;
        this.from = from;
        this.to = to;
        this.launchTime = launchTime;
        this.minuteCost = minuteCost;
    }

    public Train(String name, int from, int to, Calendar launchTime, int minuteCost) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.launchTime = launchTime;
        this.minuteCost = minuteCost;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Calendar getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Calendar launchTime) {
        this.launchTime = launchTime;
    }

    public int getMinuteCost() {
        return minuteCost;
    }

    public void setMinuteCost(int minuteCost) {
        this.minuteCost = minuteCost;
    }
}
