package sg.data;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brettweinger
 */
public class ScheduleItem<E extends Comparable<E>> implements Comparable<E> {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty type;
    private final StringProperty date;
    private final StringProperty title;
    private final StringProperty topic;
    private String link;

    /**
     * Constructor initializes both the TA name and email.
     */
    public ScheduleItem(String initType, String initDate, String initTitle, String initTopic, String initLink) {
        type = new SimpleStringProperty(initType);
        date = new SimpleStringProperty(initDate);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = initLink;
    }

    public String getType() {
        return type.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }
    
    public String getLink() {
        return link;
    }
    
    public void setType(String initType) {
        type.setValue(initType);
    }
    
    public void setDate(String initDate) {
        date.setValue(initDate);
    }
    
    public void setTitle(String initTitle) {
        title.setValue(initTitle);
    }
    
    public void setTopic(String initTopic) {
        topic.setValue(initTopic);
    }
    
    public void setLink(String initLink) {
        link = initLink;
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public StringProperty getDateProperty() {
        return date;
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public StringProperty getTopicProperty() {
        return topic;
    }
    
    @Override
    public int compareTo(E otherItem) {
        //int i = getType().compareToIgnoreCase(((ScheduleItem)otherItem).getType());
        String x1 = getType();
        String x2 = ((ScheduleItem)otherItem).getType();
            int compared = x1.compareTo(x2);
            if (compared != 0) {
               return compared;
            } 
            String itemDateString = getDate();
            String[] dateArray = itemDateString.split("/");
            LocalDate itemDate = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
            String otherItemDateString = ((ScheduleItem)otherItem).getDate();
            String[] otherDateArray = otherItemDateString.split("/");
            LocalDate otherItemDate = LocalDate.of(Integer.parseInt(otherDateArray[2]), Integer.parseInt(otherDateArray[0]), Integer.parseInt(otherDateArray[1]));
            boolean comp = itemDate.isBefore(otherItemDate);
            boolean eq = itemDate.equals(otherItemDate);
            if(eq) {
                return 0;
            } else if(comp) {
                return -1;
            } else {
                return 1;
            }
    }
}
