package sg.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.*;
import sg.data.TimeSlot.DayOfWeek;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class SiteGeneratorData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    SiteGeneratorApp app;
    
    // THESE ARE ALL THE TEACHING ASSISTANTS
    HashMap<TAType, ArrayList<TeachingAssistantPrototype>> allTAs;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistantPrototype> teachingAssistants;
    ObservableList<TimeSlot> officeHours;    
    ObservableList<TimeSlot> allOfficeHours;
    
    ObservableList<ScheduleItem> allItems;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES BUT PROVIDES
    // MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    //BOUNDS FOR DATES IN SCHEDULE TAB
    //THEY ARE INTENTIONALLY LEFT OUT OF THE CONSTRUCTOR AND ARE INITIALLY NULL
    LocalDate startDate;
    LocalDate endDate;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 21;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public SiteGeneratorData(SiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        AppGUIModule gui = app.getGUIModule();

        // SETUP THE DATA STRUCTURES
        allTAs = new HashMap();
        allTAs.put(TAType.Graduate, new ArrayList());
        allTAs.put(TAType.Undergraduate, new ArrayList());
        
        // GET THE LIST OF TAs FOR THE LEFT TABLE
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        teachingAssistants = taTableView.getItems();
        allOfficeHours = FXCollections.observableArrayList();
        allItems = FXCollections.observableArrayList();
        
        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        resetOfficeHours();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    public void setStartHour(int start) {
        startHour = start;
    }
    
    public void setEndHour(int end) {
        endHour = end;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setStartDate(LocalDate date) {
        startDate = date;
    }
    
    public void setEndDate(LocalDate date) {
        endDate = date;
    }
    
    public ObservableList<ScheduleItem> getAllItems() {
        return allItems;
    }
    
    // PRIVATE HELPER METHODS
    
    private void sortTAs() {
        Collections.sort(teachingAssistants);
    }
    
    private void resetOfficeHours() {
        //THIS WILL STORE OUR OFFICE HOURS
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        officeHours.clear();
        allOfficeHours.clear();
        for (int i = startHour; i <= endHour; i++) {
            TimeSlot timeSlot = new TimeSlot(   this.getTimeString(i, true),
                                                this.getTimeString(i, false));
            officeHours.add(timeSlot);
            allOfficeHours.add(timeSlot);
            
            TimeSlot halfTimeSlot = new TimeSlot(   this.getTimeString(i, false),
                                                    this.getTimeString(i+1, true));
            officeHours.add(halfTimeSlot);
            allOfficeHours.add(halfTimeSlot);
        }
    }
    
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    // METHODS TO OVERRIDE
        
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void reset() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        ArrayList<TeachingAssistantPrototype> grads = allTAs.get(TAType.Graduate);
        ArrayList<TeachingAssistantPrototype> undergrads = allTAs.get(TAType.Undergraduate);
        grads.clear();
        undergrads.clear();
        
        for (TimeSlot timeSlot : allOfficeHours) {
            timeSlot.reset();
        }
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUIModule gui = app.getGUIModule();
        
        ComboBox startCombo = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        ComboBox endCombo = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        startCombo.setValue("9:00am");
        endCombo.setValue("10:00pm");
        ComboBox semesterCombo = (ComboBox) gui.getGUINode(SITE_SEMESTER_COMBO);
        semesterCombo.setValue("");
        ComboBox numberCombo = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBO);
        numberCombo.setValue("");
        ComboBox yearCombo = (ComboBox) gui.getGUINode(SITE_YEAR_COMBO);
        yearCombo.setValue("");
        ComboBox subjectCombo = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBO);
        subjectCombo.setValue("");
        TextField titleText = (TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        titleText.setText("");
    }
    
    // SERVICE METHODS
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if (initStartHour <= initEndHour) {
            // THESE ARE VALID HOURS SO KEEP THEM
            // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
            startHour = initStartHour;
            endHour = initEndHour;
        }
        resetOfficeHours();
    }
    
    public void changeHours(String start, String end) {
        //Start by converting from strings to integers...
        if(start.charAt(1) == ':') { //1-digit
            startHour = Integer.parseInt(start.substring(0, 1));
        } else {
            startHour = Integer.parseInt(start.substring(0, 2));
        }
        if(start.charAt(4) == 'p' || start.charAt(5) == 'p') {
            startHour += 12;
            if(startHour == 24) {
                startHour = 12;
            }
        }
        
        if(end.charAt(1) == ':') { //1-digit
            endHour = Integer.parseInt(end.substring(0, 1));
        } else {
            endHour = Integer.parseInt(end.substring(0, 2));
        }
        if(end.charAt(4) == 'p' || end.charAt(5) == 'p') {
            endHour += 12;
            if(endHour == 24) {
                endHour = 12;
            }
        }
        AppGUIModule gui = app.getGUIModule();
        officeHours.clear();
        for (int i = startHour; i < endHour; i++) {
            String currentHour = "";
            if(i < 12) {
                currentHour += i + "_00am";
            } else if(i == 12) {
                currentHour += i + "_00pm";
            } else {
                int newHour = i - 12;
                currentHour += newHour + "_00pm";
            }
            TimeSlot timeSlot = getTimeSlot(currentHour);
            officeHours.add(timeSlot);
            String replacedHour = currentHour.replaceFirst("_0", "_3");
            TimeSlot halfTimeSlot = getTimeSlot(replacedHour);
            officeHours.add(halfTimeSlot);
        }
    }
    
    public void addTA(TeachingAssistantPrototype ta) {
        if (!hasTA(ta)) {
            TAType taType = TAType.valueOf(ta.getType());
            ArrayList<TeachingAssistantPrototype> tas = allTAs.get(taType);
            tas.add(ta);
            this.updateTAs();
        }
    }

    public void addTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        addTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.addTA(dow, ta);
            }
        }
    }
    
    public void removeTA(TeachingAssistantPrototype ta) {
        // REMOVE THE TA FROM THE LIST OF TAs
        TAType taType = TAType.valueOf(ta.getType());
        allTAs.get(taType).remove(ta);
        
        // REMOVE THE TA FROM ALL OF THEIR OFFICE HOURS
        for (TimeSlot timeSlot : allOfficeHours) {
            timeSlot.removeTA(ta);
        }
        
        // AND REFRESH THE TABLES
        this.updateTAs();
    }

    public void removeTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        removeTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.removeTA(dow, ta);
            }
        }    
    }
    
    public DayOfWeek getColumnDayOfWeek(int columnNumber) {
        return TimeSlot.DayOfWeek.values()[columnNumber-2];
    }

    public TeachingAssistantPrototype getTAWithName(String name) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getName().equals(name))
                return ta;
        }
        return null;
    }

    public TeachingAssistantPrototype getTAWithEmail(String email) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getEmail().equals(email))
                return ta;
        }
        return null;
    }
    
    public TimeSlot getTimeSlot(String startTime) {
        Iterator<TimeSlot> timeSlotsIterator = allOfficeHours.iterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            String timeSlotStartTime = timeSlot.getStartTime().replace(":", "_");
            if (timeSlotStartTime.equals(startTime))
                return timeSlot;
        }
        return null;
    }

    public TAType getSelectedType() {
        RadioButton allRadio = (RadioButton)app.getGUIModule().getGUINode(OH_ALL_RADIO_BUTTON);
        if (allRadio.isSelected())
            return TAType.All;
        RadioButton gradRadio = (RadioButton)app.getGUIModule().getGUINode(OH_GRAD_RADIO_BUTTON);
        if (gradRadio.isSelected())
            return TAType.Graduate;
        else
            return TAType.Undergraduate;
    }

    public TeachingAssistantPrototype getSelectedTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TeachingAssistantPrototype> tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem();
    }
    
    public HashMap<TimeSlot, ArrayList<DayOfWeek>> getTATimeSlots(TeachingAssistantPrototype ta) {
        HashMap<TimeSlot, ArrayList<DayOfWeek>> timeSlots = new HashMap();
        for (TimeSlot timeSlot : allOfficeHours) {
            if (timeSlot.hasTA(ta)) {
                ArrayList<DayOfWeek> daysForTA = timeSlot.getDaysForTA(ta);
                timeSlots.put(timeSlot, daysForTA);
            }
        }
        return timeSlots;
    }
    
    private boolean hasTA(TeachingAssistantPrototype testTA) {
        return allTAs.get(TAType.Graduate).contains(testTA)
                ||
                allTAs.get(TAType.Undergraduate).contains(testTA);
    }
    
    public boolean isTASelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isLegalNewTA(String name, String email) {
        if ((name.trim().length() > 0)
                && (email.trim().length() > 0)) {
            // MAKE SURE NO TA ALREADY HAS THE SAME NAME
            TAType type = this.getSelectedType();
            TeachingAssistantPrototype testTA = new TeachingAssistantPrototype(name, email, type);
            if (allTAs.get(TAType.Graduate).contains(testTA) || allTAs.get(TAType.Undergraduate).contains(testTA))
                return false;
            if (this.isLegalNewEmail(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegalNewName(String testName) {
        if (testName.trim().length() > 0) {
            for (TeachingAssistantPrototype testTA : allTAs.get(TAType.Undergraduate)) {
                if (testTA.getName().equalsIgnoreCase(testName))
                    return false;
            }
            for (TeachingAssistantPrototype testTA : allTAs.get(TAType.Graduate)) {
                if (testTA.getName().equalsIgnoreCase(testName))
                    return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean isLegalNewEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
            for (TeachingAssistantPrototype ta : allTAs.get(TAType.Graduate)) {
                if (ta.getEmail().equalsIgnoreCase(email.trim()))
                    return false;
            }
            for (TeachingAssistantPrototype ta : allTAs.get(TAType.Undergraduate)) {
                if (ta.getEmail().equalsIgnoreCase(email.trim()))
                    return false;
            }
            return true;
        }
        else return false;
    }
    
    public boolean isDayOfWeekColumn(int columnNumber) {
        return columnNumber >= 2;
    }
    
    public boolean isTATypeSelected() {
        AppGUIModule gui = app.getGUIModule();
        RadioButton allRadioButton = (RadioButton)gui.getGUINode(OH_ALL_RADIO_BUTTON);
        return !allRadioButton.isSelected();
    }
    
    public boolean isValidTAEdit(TeachingAssistantPrototype taToEdit, String name, String email) {
        if (!taToEdit.getName().equalsIgnoreCase(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        if (!taToEdit.getEmail().equalsIgnoreCase(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }

    public boolean isValidNameEdit(TeachingAssistantPrototype taToEdit, String name) {
        if (!taToEdit.getName().equalsIgnoreCase(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        return true;
    }

    public boolean isValidEmailEdit(TeachingAssistantPrototype taToEdit, String email) {
        if (!taToEdit.getEmail().equalsIgnoreCase(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }    

    public void updateTAs() {
        TAType type = getSelectedType();
        selectTAs(type);
    }
    
    public void selectTAs(TAType type) {
        teachingAssistants.clear();
        Iterator<TeachingAssistantPrototype> tasIt = this.teachingAssistantsIterator();
        while (tasIt.hasNext()) {
            TeachingAssistantPrototype ta = tasIt.next();
            if (type.equals(TAType.All)) {
                teachingAssistants.add(ta);
            }
            else if (ta.getType().equals(type.toString())) {
                teachingAssistants.add(ta);
            }
        }
        
        // SORT THEM BY NAME
        sortTAs();

        // CLEAR ALL THE OFFICE HOURS
        Iterator<TimeSlot> officeHoursIt = allOfficeHours.iterator();
        while (officeHoursIt.hasNext()) {
            TimeSlot timeSlot = officeHoursIt.next();
            timeSlot.filter(type);
        }
        
        app.getFoolproofModule().updateAll();
    }
    
    public Iterator<TimeSlot> officeHoursIterator() {
        return allOfficeHours.iterator();
    }

    public Iterator<TeachingAssistantPrototype> teachingAssistantsIterator() {
        return new AllTAsIterator();
    }
    
    private class AllTAsIterator implements Iterator {
        Iterator gradIt = allTAs.get(TAType.Graduate).iterator();
        Iterator undergradIt = allTAs.get(TAType.Undergraduate).iterator();

        public AllTAsIterator() {}
        
        @Override
        public boolean hasNext() {
            if (gradIt.hasNext() || undergradIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (gradIt.hasNext())
                return gradIt.next();
            else if (undergradIt.hasNext())
                return undergradIt.next();
            else
                return null;
        }
    }
    
    public void updateItems() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItem> itemsTable = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ObservableList<ScheduleItem> items = itemsTable.getItems();
        DatePicker startPicker = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker endPicker = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        LocalDate startDate = startPicker.getValue();
        LocalDate endDate = endPicker.getValue();
        items.clear();
        for(int i = 0; i < allItems.size(); i++) {
            ScheduleItem item = allItems.get(i);
            String itemDateString = item.getDate();
            String[] dateArray = itemDateString.split("/");
            LocalDate itemDate = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
            boolean add = true;
            if(startDate != null) {
                if(itemDate.isBefore(startDate)) {
                    add = false;
                }
            }
            if(endDate != null) {
                if(itemDate.isAfter(endDate)) {
                    add = false;
                }
            }
            if(add) {
                items.add(item);
            }
        }
        Collections.sort(items);
    }
    
    public void processClearFields() {
        AppGUIModule gui = app.getGUIModule();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField titleText = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topicText = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField linkText = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        typeCombo.setValue("");
        datePicker.setValue(null);
        titleText.setText("");
        topicText.setText("");
        linkText.setText("");
        addButton.setText(props.getProperty(SCH_ADD_BUTTON_TEXT));
    }
    
    public void processUpdateTimes() {
        AppGUIModule gui = app.getGUIModule();
        ComboBox startBox = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        ComboBox endBox = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        String startValue = (String) startBox.getValue();
        String endValue = (String) endBox.getValue();
        changeHours(startValue, endValue);
    }

    public void processUpdateEndTimes() {
        AppGUIModule gui = app.getGUIModule();
        ComboBox endBox = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        String savedEndTime = (String) endBox.getValue();
        ObservableList<String> endTimes = endBox.getItems();
        int startTime = startHour;
        endTimes.clear();
        for(int i = startTime + 1; i < 22; i++) {
            String onHour = "";
            if(i < 12) {
                onHour = i + ":00am";
            } else if(i == 12) {
                onHour = i + ":00pm";
            } else {
                int newi = i - 12;
                onHour += newi + ":00pm";
            }
            endTimes.add(onHour);
        }
        if(!endTimes.contains("10:00pm")) {
            endTimes.add("10:00pm");
        }
        endBox.setItems(endTimes);
        app.getWorkspaceComponent().changeCombo(false);
        endBox.getSelectionModel().select(savedEndTime);
    }
    
    public void processUpdateStartTimes() {
        AppGUIModule gui = app.getGUIModule();
        ComboBox startBox = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        String savedStartTime = (String) startBox.getValue();
        ObservableList<String> startTimes = startBox.getItems();
        int endTime = endHour;
        startTimes.clear();
        for(int i = 9; i < endTime; i++) {
            String onHour = "";
            if(i < 12) {
                onHour = i + ":00am";
            } else if(i == 12) {
                onHour = i + ":00pm";
            } else {
                int newi = i - 12;
                onHour += newi + ":00pm";
            }
            startTimes.add(onHour);
        }
        if(!startTimes.contains("9:00am")) {
            startTimes.add("9:00am");
        }
        startBox.setItems(startTimes);
        app.getWorkspaceComponent().changeCombo(false);
        startBox.getSelectionModel().select(savedStartTime);
    }
}