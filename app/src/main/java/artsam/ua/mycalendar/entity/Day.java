package artsam.ua.mycalendar.entity;

import java.util.ArrayList;

public class Day {

    private String mDate;
    private ArrayList<Event> mEvents;

    public Day() {
    }

    public Day(String date, ArrayList<Event> events) {
        this.mDate = date;
        this.mEvents = events;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public ArrayList<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        this.mEvents = events;
    }

    @Override
    public String toString() {
        return "Day{" +
                "mDate='" + mDate + '\'' +
                ", mEvents=" + mEvents +
                '}';
    }
}

