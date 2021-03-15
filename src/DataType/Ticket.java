package DataType;

import java.util.Calendar;

public class Ticket {

    private String id;
    private String from;
    private String to;
    private Calendar start;
    private int length;

    public Ticket(String id, String from, String to, Calendar start, int length) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.start = start;
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.MINUTE, length);
        return end;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

