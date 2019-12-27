package sg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brettweinger
 */
public class Lecture<E extends Comparable<E>> implements Comparable<E> {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty time;
    private final StringProperty room;

    /**
     * Constructor initializes both the TA name and email.
     */
    public Lecture(String initSection, String initDays, String initTime, String initRoom) {
        section = new SimpleStringProperty(initSection);
        days = new SimpleStringProperty(initDays);
        time = new SimpleStringProperty(initTime);
        room = new SimpleStringProperty(initRoom);
    }

    public String getSection() {
        return section.get();
    }

    public String getDays() {
        return days.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    public void setSection(String initSection) {
        section.setValue(initSection);
    }
    
    public void setDays(String initDays) {
        days.setValue(initDays);
    }
    
    public void setTime(String initTime) {
        time.setValue(initTime);
    }
    
    public void setRoom(String initRoom) {
        room.setValue(initRoom);
    }

    public StringProperty getSectionProperty() {
        return section;
    }

    public StringProperty getDaysProperty() {
        return days;
    }

    public StringProperty getTimeProperty() {
        return time;
    }

    public StringProperty getRoomProperty() {
        return room;
    }
    
    @Override
    public int compareTo(E otherLecture) {
        return getSection().compareToIgnoreCase(((Lecture)otherLecture).getSection());
    }
}
