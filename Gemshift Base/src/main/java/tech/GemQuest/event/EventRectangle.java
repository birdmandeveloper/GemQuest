package tech.GemQuest.event;

import java.awt.*;

// Object SPECIFICALLY linked to an Event
public class EventRectangle extends Rectangle {
    // VARIABLES
    private int eventRectDefaultX, eventRectDefaultY;
    private boolean eventDone = false;

    // CONSTRUCTOR
    public EventRectangle setEventDone(boolean eventDone) {
        this.eventDone = eventDone;
        return this;
    }

    // GETTERS AND SETTERS
    public int getEventRectDefaultX() {
        return eventRectDefaultX;
    }
    public EventRectangle setEventRectDefaultX(int eventRectDefaultX) {
        this.eventRectDefaultX = eventRectDefaultX;
        return this;
    }
    public int getEventRectDefaultY() {
        return eventRectDefaultY;
    }
    public EventRectangle setEventRectDefaultY(int eventRectDefaultY) {
        this.eventRectDefaultY = eventRectDefaultY;
        return this;
    }
    public boolean isEventDone() {
        return eventDone;
    }


}
