package DataType;

import java.util.Calendar;

public class Train {
    private long uid;
    private String name;
    private int from;
    private int to;
    private Calendar launchTime;
    private int minuteCost;


    private double moneyCost;

    public Train(long uid, String name, int from, int to, Calendar launchTime, int minuteCost, double moneyCost) {
        this.uid = uid;
        this.name = name;
        this.from = from;
        this.to = to;
        this.launchTime = launchTime;
        this.minuteCost = minuteCost;
        this.moneyCost = moneyCost;
    }

    public Train(long uid, String name, int from, int to, Calendar launchTime, int minuteCost) {
        this(uid, name, from, to, launchTime, minuteCost, 20);
    }

    public Train(String name, int from, int to, Calendar launchTime, int minuteCost) {
        this(0, name, from, to, launchTime, minuteCost, 20);
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

    public double getMoneyCost() {
        return moneyCost;
    }

    public void setMoneyCost(double moneyCost) {
        this.moneyCost = moneyCost;
    }
}
