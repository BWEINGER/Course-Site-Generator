package sg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brettweinger
 */
public class Recitation<E extends Comparable<E>> implements Comparable<E> {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty section;
    private final StringProperty daysTime;
    private final StringProperty room;
    private final StringProperty ta1;
    private final StringProperty ta2;

    /**
     * Constructor initializes both the TA name and email.
     */
    public Recitation(String initSection, String initDaysTime, String initRoom, String initTA1, String initTA2) {
        section = new SimpleStringProperty(initSection);
        daysTime = new SimpleStringProperty(initDaysTime);
        room = new SimpleStringProperty(initRoom);
        ta1 = new SimpleStringProperty(initTA1);
        ta2 = new SimpleStringProperty(initTA2);
    }

    public String getSection() {
        return section.get();
    }

    public String getDaysTime() {
        return daysTime.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    public String getTa1() {
        return ta1.get();
    }
    
    public String getTa2() {
        return ta2.get();
    }
    
    public void setSection(String initSection) {
        section.setValue(initSection);
    }
    
    public void setDaysTime(String initDaysTime) {
        daysTime.setValue(initDaysTime);
    }
    
    public void setRoom(String initRoom) {
        room.setValue(initRoom);
    }
    
    public void setTa1(String initTA1) {
        ta1.setValue(initTA1);
    }
    
    public void setTa2(String initTA2) {
        ta2.setValue(initTA2);
    }

    public StringProperty getSectionProperty() {
        return section;
    }

    public StringProperty getDaysTimeProperty() {
        return daysTime;
    }

    public StringProperty getRoomProperty() {
        return room;
    }
    
    public StringProperty getTa1Property() {
        return ta1;
    }
    
    public StringProperty getTa2Property() {
        return ta2;
    }
    
    @Override
    public int compareTo(E otherRecitation) {
        return getSection().compareToIgnoreCase(((Recitation)otherRecitation).getSection());
    }
}
