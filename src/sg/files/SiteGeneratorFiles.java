package sg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.modules.AppGUIModule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.*;
import sg.data.Lab;
import sg.data.Lecture;
import sg.data.Recitation;
import sg.data.ScheduleItem;
import sg.data.SiteGeneratorData;
import sg.data.TAType;
import sg.data.TeachingAssistantPrototype;
import sg.data.TimeSlot;
import sg.data.TimeSlot.DayOfWeek;
import sg.transactions.Image_Transaction;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class SiteGeneratorFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    SiteGeneratorApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_SRC = "src";
    static final String JSON_HREF = "href";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_LINK = "link";
    static final String JSON_ROOM = "room";
    static final String JSON_PHOTO = "photo";
    static final String JSON_HOURS = "hours";
    static final String JSON_PAGES = "pages";
    static final String JSON_CSS = "stylesheet";
    
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_PREREQS = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_COMPONENTS = "gradedComponents";
    static final String JSON_NOTE = "gradingNote";
    static final String JSON_DISHONESTY = "academicDishonesty";
    static final String JSON_ASSISTANCE = "specialAssistance";
    
    static final String JSON_LECTURES = "meetingLectures";
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_TIME = "time";
    static final String JSON_RECITATIONS = "meetingRecitations";
    static final String JSON_LABS = "labs";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA_1 = "ta_1";
    static final String JSON_TA_2 = "ta_2";
    
    static final String JSON_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_MONDAY_DAY = "startingMondayDay";
    static final String JSON_FRIDAY_MONTH = "endingFridayMonth";
    static final String JSON_FRIDAY_DAY = "endingFridayDay";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_SCH_LECTURES = "lectures";
    static final String JSON_MONTH = "month";
    static final String JSON_DAY = "day";
    static final String JSON_TOPIC = "topic";
    static final String JSON_REFERENCES = "references";
    static final String JSON_SCH_RECITATIONS = "recitations";
    static final String JSON_HWS = "hws";
    static final String JSON_CRITERIA = "criteria";

    public SiteGeneratorFiles(SiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        AppGUIModule gui = app.getGUIModule();
        RadioButton all = (RadioButton)gui.getGUINode(OH_ALL_RADIO_BUTTON);
        all.setSelected(true); //bug fix
        
	// CLEAR THE OLD DATA OUT
	SiteGeneratorData dataManager = (SiteGeneratorData)data;
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        ComboBox startBox = (ComboBox)gui.getGUINode(OH_START_COMBO_BOX);
        ComboBox endBox = (ComboBox)gui.getGUINode(OH_END_COMBO_BOX);
        int start = Integer.parseInt(startHour);
        if(start > 12) {
            start -= 12;
            startHour = start + ":00pm";
        } else if(start == 12) {
            startHour = start + ":00pm";
        } else {
            startHour = start + ":00am";
        }
        int end = Integer.parseInt(endHour);
        if(end > 12) {
            end -= 12;
            endHour = end + ":00pm";
        } else if(end == 12) {
            endHour = end + ":00pm";
        } else {
            endHour = end + ":00am";
        }
        startBox.setValue(startHour);
        endBox.setValue(endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
        
        //LOAD SITE DATA
        String subject = json.getString(JSON_SUBJECT);
        String number = json.getString(JSON_NUMBER);
        String semester = json.getString(JSON_SEMESTER);
        String year = json.getString(JSON_YEAR);
        String title = json.getString(JSON_TITLE);
        ComboBox subj = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO);
        subj.setValue(subject);
        ComboBox num = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBO);
        num.setValue(number);
        ComboBox sem = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBO);
        sem.setValue(semester);
        ComboBox yr = (ComboBox)gui.getGUINode(SITE_YEAR_COMBO);
        yr.setValue(year);
        TextField titleTxt = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        titleTxt.setText(title);
        
        //LOAD LOGOS
        ImageView fiv = (ImageView)gui.getGUINode(SITE_FAVICON);
        ImageView niv = (ImageView)gui.getGUINode(SITE_NAVBAR);
        ImageView liv = (ImageView)gui.getGUINode(SITE_LEFT_FOOTER);
        ImageView riv = (ImageView)gui.getGUINode(SITE_RIGHT_FOOTER);
        
        JsonObject jsonLogosObject = json.getJsonObject(JSON_LOGOS);
        JsonObject jsonFavicon = jsonLogosObject.getJsonObject(JSON_FAVICON);
        String src = jsonFavicon.getString(JSON_SRC);
        Image img = new Image(src);
        Image initImg = new Image("https://pbs.twimg.com/profile_images/1054070756848607233/wHfu_1gp_400x400.jpg");
        Image_Transaction transaction = new Image_Transaction(fiv, initImg, img, app);
        app.processTransaction(transaction);
        //File f = new File(src);
        //fiv.setImage(new Image(f.toURI().toString()));
        JsonObject jsonNavbar = jsonLogosObject.getJsonObject(JSON_NAVBAR);
        src = jsonNavbar.getString(JSON_SRC);
        img = new Image(src);
        initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUDarkRedShieldLogo.png");
        transaction = new Image_Transaction(niv, initImg, img, app);
        app.processTransaction(transaction);
        //f = new File(src);
        //niv.setImage(new Image(f.toURI().toString()));
        JsonObject jsonLeftFooter = jsonLogosObject.getJsonObject(JSON_BOTTOM_LEFT);
        src = jsonLeftFooter.getString(JSON_SRC);
        img = new Image(src);
        initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUWhiteShieldLogo.jpg");
        transaction = new Image_Transaction(liv, initImg, img, app);
        app.processTransaction(transaction);
        //f = new File(src);
        //liv.setImage(new Image(f.toURI().toString()));
        JsonObject jsonRightFooter = jsonLogosObject.getJsonObject(JSON_BOTTOM_RIGHT);
        src = jsonRightFooter.getString(JSON_SRC);
        img = new Image(src);
        initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUCSLogo.png");
        transaction = new Image_Transaction(riv, initImg, img, app);
        app.processTransaction(transaction);
        //f = new File(src);
        //riv.setImage(new Image(f.toURI().toString()));
        
        ComboBox cssBox = (ComboBox)gui.getGUINode(SITE_CSS_COMBO);
        cssBox.setValue(json.getString(JSON_CSS));
        
        TextField instrName = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        TextField instrEmail = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        TextField instrRoom = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        TextField instrHome = (TextField)gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD);
        TextArea instrHours = (TextArea)gui.getGUINode(SITE_OH_TEXT_AREA);
        
        JsonObject jsonInstr = json.getJsonObject(JSON_INSTRUCTOR);
        String name = jsonInstr.getString(JSON_NAME);
        String email = jsonInstr.getString(JSON_EMAIL);
        String room = jsonInstr.getString(JSON_ROOM);
        String home = jsonInstr.getString(JSON_LINK);
        String instrHoursStr = jsonInstr.getString(JSON_HOURS); //remove this line on exporting
        instrName.setText(name);
        instrEmail.setText(email);
        instrRoom.setText(room);
        instrHome.setText(home);
        
        /*JsonArray jsonInstrHours = jsonInstr.getJsonArray(JSON_HOURS);
        String instrHoursStr = jsonInstrHours.toString();
        instrHoursStr = instrHoursStr.replaceAll("\\{", "\n {");
        instrHoursStr = instrHoursStr.replaceAll("]", "\n]");*/
        instrHours.setText(instrHoursStr);
        
        CheckBox homeCheck = (CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox syllabusCheck = (CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox scheduleCheck = (CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox hwsCheck = (CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX);
        homeCheck.setSelected(false);
        syllabusCheck.setSelected(false);
        scheduleCheck.setSelected(false);
        hwsCheck.setSelected(false);
        
        JsonArray jsonPages = json.getJsonArray(JSON_PAGES);
        for(int i = 0; i < jsonPages.size(); i++) {
            JsonObject jsonPage = jsonPages.getJsonObject(i);
            String pageName = jsonPage.getString(JSON_NAME);
            if(pageName.equals("Home")) {
                homeCheck.setSelected(true);
            } else if(pageName.equals("Syllabus")) {
                syllabusCheck.setSelected(true);
            } else if(pageName.equals("Schedule")) {
                scheduleCheck.setSelected(true);
            } else if(pageName.equals("HWs")) {
                hwsCheck.setSelected(true);
            }
        }
        
        //LOAD SYLLABUS DATA
        TextArea descT = (TextArea) gui.getGUINode(SYL_DESCRIPTION_TEXT_AREA);
        TextArea topicsT = (TextArea) gui.getGUINode(SYL_TOPICS_TEXT_AREA);
        TextArea prereqsT = (TextArea) gui.getGUINode(SYL_PREREQS_TEXT_AREA);
        TextArea outcomesT = (TextArea) gui.getGUINode(SYL_OUTCOMES_TEXT_AREA);
        TextArea textbooksT = (TextArea) gui.getGUINode(SYL_TEXTBOOKS_TEXT_AREA);
        TextArea componentsT = (TextArea) gui.getGUINode(SYL_COMPONENTS_TEXT_AREA);
        TextArea noteT = (TextArea) gui.getGUINode(SYL_NOTE_TEXT_AREA);
        TextArea dishonestyT = (TextArea) gui.getGUINode(SYL_DISHONESTY_TEXT_AREA);
        TextArea assistanceT = (TextArea) gui.getGUINode(SYL_ASSISTANCE_TEXT_AREA);
        
        
        String description = json.getString(JSON_DESCRIPTION);
        descT.setText(description);
        String topics = json.getString(JSON_TOPICS);
        //JsonArray topics = json.getJsonArray(JSON_TOPICS);
        topicsT.setText(topics.toString());
        String prereqs = json.getString(JSON_PREREQS);
        prereqsT.setText(prereqs);
        String jsonOutcomes = json.getString(JSON_OUTCOMES);
        //JsonArray jsonOutcomes = json.getJsonArray(JSON_OUTCOMES);
        outcomesT.setText(jsonOutcomes.toString());
        String jsonTextbooks = json.getString(JSON_TEXTBOOKS);
        //JsonArray jsonTextbooks = json.getJsonArray(JSON_TEXTBOOKS);
        textbooksT.setText(jsonTextbooks.toString());
        String jsonComponents = json.getString(JSON_COMPONENTS);
        //JsonArray jsonComponents = json.getJsonArray(JSON_COMPONENTS);
        componentsT.setText(jsonComponents.toString());
        String note = json.getString(JSON_NOTE);
        noteT.setText(note);
        String dishonesty = json.getString(JSON_DISHONESTY);
        dishonestyT.setText(dishonesty);
        String assistance = json.getString(JSON_ASSISTANCE);
        assistanceT.setText(assistance);
        
        //LOAD MEETING TIMES DATA
        ObservableList lectures = ((TableView)gui.getGUINode(MT_LECTURES_TABLE_VIEW)).getItems();
        ObservableList recitations = ((TableView)gui.getGUINode(MT_RECITATIONS_TABLE_VIEW)).getItems();
        ObservableList labs = ((TableView)gui.getGUINode(MT_LABS_TABLE_VIEW)).getItems();
        lectures.clear();
        recitations.clear();
        labs.clear();
        
        JsonArray jsonLectures = json.getJsonArray(JSON_LECTURES);
        for(int i = 0; i < jsonLectures.size(); i++) {
            JsonObject jsonLecture = jsonLectures.getJsonObject(i);
            String section = jsonLecture.getString(JSON_SECTION);
            String days = jsonLecture.getString(JSON_DAYS);
            String time = jsonLecture.getString(JSON_TIME);
            room = jsonLecture.getString(JSON_ROOM);
            lectures.add(new Lecture(section, days, time, room));
        }
        
        JsonArray jsonRecitations = json.getJsonArray(JSON_RECITATIONS);
        for(int i = 0; i < jsonRecitations.size(); i++) {
            JsonObject jsonRecitation = jsonRecitations.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            String dayTime = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String ta1 = jsonRecitation.getString(JSON_TA_1);
            String ta2 = jsonRecitation.getString(JSON_TA_2);
            recitations.add(new Recitation(section, dayTime, location, ta1, ta2));
        }
        
        JsonArray jsonLabs = json.getJsonArray(JSON_LABS);
        for(int i = 0; i < jsonLabs.size(); i++) {
            JsonObject jsonLab = jsonLabs.getJsonObject(i);
            String section = jsonLab.getString(JSON_SECTION);
            String dayTime = jsonLab.getString(JSON_DAY_TIME);
            String location = jsonLab.getString(JSON_LOCATION);
            String ta1 = jsonLab.getString(JSON_TA_1);
            String ta2 = jsonLab.getString(JSON_TA_2);
            labs.add(new Lab(section, dayTime, location, ta1, ta2));
        }
        
        //LOAD SCHEDULE DATA
        DatePicker mondayDate = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker fridayDate = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker date = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField schTitle = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField schTopic = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField schLink = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        ObservableList tableItems = ((TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW)).getItems();
        ObservableList<ScheduleItem> items = dataManager.getAllItems();
        tableItems.clear();
        items.clear();
        int startEndYear = LocalDate.now().getYear();
        String enteredYear = (String)yr.getValue();
        try {
            startEndYear = Integer.parseInt(enteredYear);
        } catch(NumberFormatException e) { }
        
        String startMonth = json.getString(JSON_MONDAY_MONTH);
        String startDay = json.getString(JSON_MONDAY_DAY);
        String endMonth = json.getString(JSON_FRIDAY_MONTH);
        String endDay = json.getString(JSON_FRIDAY_DAY);
        try {
            app.getWorkspaceComponent().changeDate(false);
            mondayDate.setValue(LocalDate.of(startEndYear, Integer.parseInt(startMonth), Integer.parseInt(startDay)));
            app.getWorkspaceComponent().changeDate(true);
            dataManager.setStartDate(LocalDate.of(startEndYear, Integer.parseInt(startMonth), Integer.parseInt(startDay)));
        } catch(NumberFormatException e) { }
        try {
            app.getWorkspaceComponent().changeDate(false);
            fridayDate.setValue(LocalDate.of(startEndYear, Integer.parseInt(endMonth), Integer.parseInt(endDay)));
            app.getWorkspaceComponent().changeDate(true);
            dataManager.setEndDate(LocalDate.of(startEndYear, Integer.parseInt(endMonth), Integer.parseInt(endDay)));
        } catch(NumberFormatException e) { }
        
        JsonArray jsonHolidays = json.getJsonArray(JSON_HOLIDAYS);
        for(int i = 0; i < jsonHolidays.size(); i++) {
            JsonObject holiday = jsonHolidays.getJsonObject(i);
            String month = holiday.getString(JSON_MONTH);
            String day = holiday.getString((JSON_DAY));
            title = holiday.getString(JSON_TITLE);
            String link = holiday.getString(JSON_LINK);
            LocalDate currentDate = LocalDate.of(startEndYear, Integer.parseInt(month), Integer.parseInt(day));
            items.add(new ScheduleItem("Holiday", currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth() + "/" + currentDate.getYear(), title, "", link));
        }
        
        jsonLectures = json.getJsonArray(JSON_SCH_LECTURES);
        for(int i = 0; i < jsonLectures.size(); i++) {
            JsonObject lecture = jsonLectures.getJsonObject(i);
            String month = lecture.getString(JSON_MONTH);
            String day = lecture.getString((JSON_DAY));
            title = lecture.getString(JSON_TITLE);
            String topic = lecture.getString(JSON_TOPIC);
            String link = lecture.getString(JSON_LINK);
            LocalDate currentDate = LocalDate.of(startEndYear, Integer.parseInt(month), Integer.parseInt(day));
            items.add(new ScheduleItem("Lecture", currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth() + "/" + currentDate.getYear(), title, topic, link));
        }
        
        JsonArray jsonReferences = json.getJsonArray(JSON_REFERENCES);
        for(int i = 0; i < jsonReferences.size(); i++) {
            JsonObject reference = jsonReferences.getJsonObject(i);
            String month = reference.getString(JSON_MONTH);
            String day =reference.getString((JSON_DAY));
            title = reference.getString(JSON_TITLE);
            String topic = reference.getString(JSON_TOPIC);
            String link = reference.getString(JSON_LINK);
            LocalDate currentDate = LocalDate.of(startEndYear, Integer.parseInt(month), Integer.parseInt(day));
            items.add(new ScheduleItem("Reference", currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth() + "/" + currentDate.getYear(), title, topic, link));
        }
        
        jsonRecitations = json.getJsonArray(JSON_SCH_RECITATIONS);
        for(int i = 0; i < jsonRecitations.size(); i++) {
            JsonObject recitation = jsonRecitations.getJsonObject(i);
            String month = recitation.getString(JSON_MONTH);
            String day = recitation.getString((JSON_DAY));
            title = recitation.getString(JSON_TITLE);
            String topic = recitation.getString(JSON_TOPIC);
            String link = recitation.getString(JSON_LINK);
            LocalDate currentDate = LocalDate.of(startEndYear, Integer.parseInt(month), Integer.parseInt(day));
            items.add(new ScheduleItem("Recitation", currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth() + "/" + currentDate.getYear(), title, topic, link));
        }
        
        JsonArray jsonHWs = json.getJsonArray(JSON_HWS);
        for(int i = 0; i < jsonHWs.size(); i++) {
            JsonObject hw = jsonHWs.getJsonObject(i);
            String month = hw.getString(JSON_MONTH);
            String day = hw.getString((JSON_DAY));
            title = hw.getString(JSON_TITLE);
            String topic = hw.getString(JSON_TOPIC);
            String link = hw.getString(JSON_LINK);
            LocalDate currentDate = LocalDate.of(startEndYear, Integer.parseInt(month), Integer.parseInt(day));
            items.add(new ScheduleItem("Homework", currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth() + "/" + currentDate.getYear(), title, topic, link));
        }
        dataManager.updateItems();
        //Update jTPS stack to not allow undos of loaded data
        app.getTPS().setPointer();
        app.getFoolproofModule().updateAll();
    }
        
    private void loadTAs(SiteGeneratorData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	SiteGeneratorData dataManager = (SiteGeneratorData)data;
        AppGUIModule gui = app.getGUIModule();

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        //SITE TAB
        ImageView fiv = (ImageView)gui.getGUINode(SITE_FAVICON);
        ImageView niv = (ImageView)gui.getGUINode(SITE_NAVBAR);
        ImageView liv = (ImageView)gui.getGUINode(SITE_LEFT_FOOTER);
        ImageView riv = (ImageView)gui.getGUINode(SITE_RIGHT_FOOTER);
        TextField instrName = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        TextField instrEmail = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        TextField instrRoom = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        TextField instrHome = (TextField)gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD);
        TextArea instrHours = (TextArea)gui.getGUINode(SITE_OH_TEXT_AREA);
        CheckBox homeCheck = (CheckBox)gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox syllabusCheck = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox scheduleCheck = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox hwsCheck = (CheckBox)gui.getGUINode(SITE_HWS_CHECKBOX);
        ComboBox cssBox = (ComboBox)gui.getGUINode(SITE_CSS_COMBO);
        
        JsonObject faviconJson = Json.createObjectBuilder()
                .add(JSON_SRC, fiv.getImage().impl_getUrl())
                .build();
        JsonObject navbarJson = Json.createObjectBuilder()
                .add(JSON_SRC, niv.getImage().impl_getUrl())
                .build();
        JsonObject leftJson = Json.createObjectBuilder()
                .add(JSON_SRC, liv.getImage().impl_getUrl())
                .build();
        JsonObject rightJson = Json.createObjectBuilder()
                .add(JSON_SRC, riv.getImage().impl_getUrl())
                .build();
        JsonObject logosJson = Json.createObjectBuilder()
                .add(JSON_FAVICON, faviconJson)
                .add(JSON_NAVBAR, navbarJson)
                .add(JSON_BOTTOM_LEFT, leftJson)
                .add(JSON_BOTTOM_RIGHT, rightJson)
                .build();
        
        JsonObject instructorJson = Json.createObjectBuilder()
                .add(JSON_NAME, instrName.getText())
                .add(JSON_LINK, instrHome.getText())
                .add(JSON_EMAIL, instrEmail.getText())
                .add(JSON_ROOM, instrRoom.getText())
                .add(JSON_HOURS, instrHours.getText())
                .build();
        
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        JsonObject homeJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Home")
                .build();
        JsonObject syllabusJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Syllabus")
                .build();
        JsonObject scheduleJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Schedule")
                .build();
        JsonObject hwsJson = Json.createObjectBuilder()
                .add(JSON_NAME, "HWs")
                .build();
        if(homeCheck.isSelected()) {
            pagesArrayBuilder.add(homeJson);
        }
        if(syllabusCheck.isSelected()) {
            pagesArrayBuilder.add(syllabusJson);
        }
        if(scheduleCheck.isSelected()) {
            pagesArrayBuilder.add(scheduleJson);
        }
        if(hwsCheck.isSelected()) {
            pagesArrayBuilder.add(hwsJson);
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        //SYLLABUS TAB
        TextArea descT = (TextArea) gui.getGUINode(SYL_DESCRIPTION_TEXT_AREA);
        TextArea topicsT = (TextArea) gui.getGUINode(SYL_TOPICS_TEXT_AREA);
        TextArea prereqsT = (TextArea) gui.getGUINode(SYL_PREREQS_TEXT_AREA);
        TextArea outcomesT = (TextArea) gui.getGUINode(SYL_OUTCOMES_TEXT_AREA);
        TextArea textbooksT = (TextArea) gui.getGUINode(SYL_TEXTBOOKS_TEXT_AREA);
        TextArea componentsT = (TextArea) gui.getGUINode(SYL_COMPONENTS_TEXT_AREA);
        TextArea noteT = (TextArea) gui.getGUINode(SYL_NOTE_TEXT_AREA);
        TextArea dishonestyT = (TextArea) gui.getGUINode(SYL_DISHONESTY_TEXT_AREA);
        TextArea assistanceT = (TextArea) gui.getGUINode(SYL_ASSISTANCE_TEXT_AREA);
        
        //MEETING TIMES TAB
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
        TableView<Lecture> lecturesTable = (TableView)gui.getGUINode(MT_LECTURES_TABLE_VIEW);
        ObservableList<Lecture> lectures = lecturesTable.getItems();
        for(int i = 0; i < lectures.size(); i++) {
            Lecture lecture = lectures.get(i);
            JsonObject lectureJson = Json.createObjectBuilder()
                .add(JSON_SECTION, lecture.getSection())
                .add(JSON_DAYS, lecture.getDays())
                .add(JSON_TIME, lecture.getTime())
                .add(JSON_ROOM, lecture.getRoom())
                .build();
                lecturesArrayBuilder.add(lectureJson);
        }  
	JsonArray lecturesArray = lecturesArrayBuilder.build();
        
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        TableView<Lab> labsTable = (TableView)gui.getGUINode(MT_LABS_TABLE_VIEW);
        ObservableList<Lab> labs = labsTable.getItems();
        for(int i = 0; i < labs.size(); i++) {
            Lab lab = labs.get(i);
            JsonObject labJson = Json.createObjectBuilder()
                .add(JSON_SECTION, lab.getSection())
                .add(JSON_DAY_TIME, lab.getDaysTime())
                .add(JSON_LOCATION, lab.getRoom())
                .add(JSON_TA_1, lab.getTa1())
                .add(JSON_TA_2, lab.getTa2())
                .build();
                labsArrayBuilder.add(labJson);
        }  
	JsonArray labsArray = labsArrayBuilder.build();
        
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        TableView<Recitation> recitationsTable = (TableView)gui.getGUINode(MT_RECITATIONS_TABLE_VIEW);
        ObservableList<Recitation> recitations = recitationsTable.getItems();
        for(int i = 0; i < recitations.size(); i++) {
            Recitation recitation = recitations.get(i);
            JsonObject recitationJson = Json.createObjectBuilder()
                .add(JSON_SECTION, recitation.getSection())
                .add(JSON_DAY_TIME, recitation.getDaysTime())
                .add(JSON_LOCATION, recitation.getRoom())
                .add(JSON_TA_1, recitation.getTa1())
                .add(JSON_TA_2, recitation.getTa2())
                .build();
                recitationsArrayBuilder.add(recitationJson);
        }  
	JsonArray recitationsArray = recitationsArrayBuilder.build();
        
        //SCHEDULE TAB
        DatePicker mondayDate = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker fridayDate = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker date = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField schTitle = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField schTopic = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField schLink = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        //ObservableList<ScheduleItem> items = ((TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW)).getItems();
        SiteGeneratorData siteData = (SiteGeneratorData) app.getDataComponent();
        ObservableList<ScheduleItem> items = siteData.getAllItems();
        
        String mondayMonth = "";
        String mondayDay = "";
        String fridayMonth = "";
        String fridayDay = "";
        if(mondayDate.getValue() != null) {
            mondayMonth = mondayDate.getValue().getMonthValue() + "";
            mondayDay = mondayDate.getValue().getDayOfMonth() + "";
        }
        if(fridayDate.getValue() != null) {
            fridayMonth = fridayDate.getValue().getMonthValue() + "";
            fridayDay = fridayDate.getValue().getDayOfMonth() + "";
        }
        
        JsonArrayBuilder holidaysArrayBuilder = Json.createArrayBuilder();
        lecturesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        recitationsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
        
        for(int i = 0; i < items.size(); i++) {
            ScheduleItem item = items.get(i);
            String[] splitDate = item.getDate().split("/");
            String month = splitDate[0];
            String day = splitDate[1];
            if(item.getType().equalsIgnoreCase("Holiday")) {
                JsonObject holidayJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_LINK, item.getLink())
                        .build();
                holidaysArrayBuilder.add(holidayJson);
            } else if(item.getType().equalsIgnoreCase("Lecture")) {
                JsonObject lectureJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                lecturesArrayBuilder.add(lectureJson);
            } else if(item.getType().equalsIgnoreCase("Reference")) {
                JsonObject referenceJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                referencesArrayBuilder.add(referenceJson);
            } else if(item.getType().equalsIgnoreCase("Recitation")) {
                JsonObject recitationJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                recitationsArrayBuilder.add(recitationJson);
            } else if(item.getType().equalsIgnoreCase("Homework")) {
                JsonObject hwJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                hwsArrayBuilder.add(hwJson);
            }
        }
        JsonArray holidaysArray = holidaysArrayBuilder.build();
        JsonArray schLecturesArray = lecturesArrayBuilder.build();
        JsonArray referencesArray = referencesArrayBuilder.build();
	JsonArray schRecitationsArray = recitationsArrayBuilder.build();
        JsonArray hwsArray = hwsArrayBuilder.build();
        
        String cssText = (String)cssBox.getValue();
        if(cssText == null) {
            cssText = "";
        }
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_SUBJECT, (String)((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO)).getValue())
                .add(JSON_NUMBER, (String)((ComboBox)gui.getGUINode(SITE_NUMBER_COMBO)).getValue())
                .add(JSON_SEMESTER, (String)((ComboBox)gui.getGUINode(SITE_SEMESTER_COMBO)).getValue())
                .add(JSON_YEAR, (String)((ComboBox)gui.getGUINode(SITE_YEAR_COMBO)).getValue())
                .add(JSON_TITLE, (String)((TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText())
		.add(JSON_LOGOS, logosJson)
                .add(JSON_CSS, cssText)
                .add(JSON_INSTRUCTOR, instructorJson)
                .add(JSON_PAGES, pagesArray)
                .add(JSON_DESCRIPTION, descT.getText())
                .add(JSON_TOPICS, topicsT.getText())
                .add(JSON_PREREQS, prereqsT.getText())
                .add(JSON_OUTCOMES, outcomesT.getText())
                .add(JSON_TEXTBOOKS, textbooksT.getText())
                .add(JSON_COMPONENTS, componentsT.getText())
                .add(JSON_NOTE, noteT.getText())
                .add(JSON_DISHONESTY, dishonestyT.getText())
                .add(JSON_ASSISTANCE, assistanceT.getText())
                .add(JSON_LECTURES, lecturesArray)
                .add(JSON_LABS, labsArray)
                .add(JSON_RECITATIONS, recitationsArray)
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .add(JSON_MONDAY_MONTH, mondayMonth)
                .add(JSON_MONDAY_DAY, mondayDay)
                .add(JSON_FRIDAY_MONTH, fridayMonth)
                .add(JSON_FRIDAY_DAY, fridayDay)
                .add(JSON_HOLIDAYS, holidaysArray)
                .add(JSON_SCH_LECTURES, schLecturesArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_SCH_RECITATIONS, schRecitationsArray)
                .add(JSON_HWS, hwsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        //CREATE AN EXPORT DIRECTORY WITH APPROPRIATE NAME AND COPY EVERYTHING OVER
        Label dir = (Label) app.getGUIModule().getGUINode(SITE_EXPORT_LABEL);
        String dirPath = dir.getText();
        Path path = Paths.get("export/" + dirPath);
        if(Files.isDirectory(path)) {
            FileUtils.deleteDirectory(new File("export/" + dirPath));
        }
        Files.createDirectory(path); //Creates a directory
        FileUtils.copyDirectory(new File("src/sg/files/sitedata"), new File("export/" + dirPath));
        
        //WE STILL NEED TO CREATE THE CSS AND JSON FILES. LET'S DEAL WITH THE STYLESHEET FIRST.
        ComboBox cssCombo = (ComboBox) app.getGUIModule().getGUINode(SITE_CSS_COMBO);
        String stylesheet = (String) cssCombo.getValue();
        if(stylesheet == null) {
        } else if(!stylesheet.equals("")) { //stylesheet isn't blank
            File cssFile = new File("work/css/" + stylesheet);
            File newCSSFile = new File("export/" + dirPath + "/css", "sea_wolf.css");
            FileUtils.copyFile(cssFile, newCSSFile);
        }
        //CSS FILE WILL BE LOADED IN (JAVAFX BUG - CHANGES ONLY UPDATE ON JVM RESTART)
        //NEXT DEAL WITH PARSING JSON FILES
        AppGUIModule gui = app.getGUIModule();
        SiteGeneratorData dataManager = (SiteGeneratorData) app.getDataComponent();
        ImageView fiv = (ImageView)gui.getGUINode(SITE_FAVICON);
        ImageView niv = (ImageView)gui.getGUINode(SITE_NAVBAR);
        ImageView liv = (ImageView)gui.getGUINode(SITE_LEFT_FOOTER);
        ImageView riv = (ImageView)gui.getGUINode(SITE_RIGHT_FOOTER);
        TextField instrName = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        TextField instrEmail = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        TextField instrRoom = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        TextField instrHome = (TextField)gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD);
        TextArea instrHours = (TextArea)gui.getGUINode(SITE_OH_TEXT_AREA);
        CheckBox homeCheck = (CheckBox)gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox syllabusCheck = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox scheduleCheck = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox hwsCheck = (CheckBox)gui.getGUINode(SITE_HWS_CHECKBOX);
        String fURL = fiv.getImage().impl_getUrl();
        if(fURL.contains("/images/")) {
            fURL = "." + fURL.substring(fURL.indexOf("/images/"));
        }
        String nURL = niv.getImage().impl_getUrl();
        if(nURL.contains("/images/")) {
            nURL = "." + nURL.substring(nURL.indexOf("/images/"));
        }
        String lURL = liv.getImage().impl_getUrl();
        if(lURL.contains("/images/")) {
            lURL = "." + lURL.substring(lURL.indexOf("/images/"));
        }
        String rURL = riv.getImage().impl_getUrl();
        if(rURL.contains("/images/")) {
            rURL = "." + rURL.substring(rURL.indexOf("/images/"));
        }
        JsonObject faviconJson = Json.createObjectBuilder()
                .add(JSON_HREF, fURL)
                .build();
        JsonObject navbarJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.stonybrook.edu")
                .add(JSON_SRC, nURL)
                .build();
        JsonObject leftJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, lURL)
                .build();
        JsonObject rightJson = Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, rURL)
                .build();
        JsonObject logosJson = Json.createObjectBuilder()
                .add(JSON_FAVICON, faviconJson)
                .add(JSON_NAVBAR, navbarJson)
                .add(JSON_BOTTOM_LEFT, leftJson)
                .add(JSON_BOTTOM_RIGHT, rightJson)
                .build();
        
        
            JsonArray hours;
        try {
            JsonReader jsonReader = Json.createReader(new StringBufferInputStream(instrHours.getText()));
            hours = jsonReader.readArray();
        } catch(Exception e) {
            hours = Json.createArrayBuilder().build();
        }
        
        JsonObject instructorJson = Json.createObjectBuilder()
                .add(JSON_NAME, instrName.getText())
                .add(JSON_LINK, instrHome.getText())
                .add(JSON_EMAIL, instrEmail.getText())
                .add(JSON_ROOM, instrRoom.getText())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS, hours)
                .build();
        
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        JsonObject homeJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Home")
                .add(JSON_LINK, "index.html")
                .build();
        JsonObject syllabusJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Syllabus")
                .add(JSON_LINK, "syllabus.html")
                .build();
        JsonObject scheduleJson = Json.createObjectBuilder()
                .add(JSON_NAME, "Schedule")
                .add(JSON_LINK, "schedule.html")
                .build();
        JsonObject hwsJson = Json.createObjectBuilder()
                .add(JSON_NAME, "HWs")
                .add(JSON_LINK, "hws.html")
                .build();
        if(homeCheck.isSelected()) {
            pagesArrayBuilder.add(homeJson);
        }
        if(syllabusCheck.isSelected()) {
            pagesArrayBuilder.add(syllabusJson);
        }
        if(scheduleCheck.isSelected()) {
            pagesArrayBuilder.add(scheduleJson);
        }
        if(hwsCheck.isSelected()) {
            pagesArrayBuilder.add(hwsJson);
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        JsonObject pageDataJSO = Json.createObjectBuilder()
                .add(JSON_SUBJECT, (String)((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO)).getValue())
                .add(JSON_NUMBER, (String)((ComboBox)gui.getGUINode(SITE_NUMBER_COMBO)).getValue())
                .add(JSON_SEMESTER, (String)((ComboBox)gui.getGUINode(SITE_SEMESTER_COMBO)).getValue())
                .add(JSON_YEAR, (String)((ComboBox)gui.getGUINode(SITE_YEAR_COMBO)).getValue())
                .add(JSON_TITLE, (String)((TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText())
		.add(JSON_LOGOS, logosJson)
                .add(JSON_INSTRUCTOR, instructorJson)
                .add(JSON_PAGES, pagesArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(pageDataJSO);
	jsonWriter.close();

	// INIT THE WRITER
        String newFilePath = "export/" + dirPath + "/js/PageData.json";
        Files.createFile(Paths.get(newFilePath));
	OutputStream os = new FileOutputStream(newFilePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(pageDataJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(newFilePath);
	pw.write(prettyPrinted);
	pw.close();
        
        //SYLABUS EXPORTING
        
        //MEETING TIMES EXPORTING
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
        TableView<Lecture> lecturesTable = (TableView)gui.getGUINode(MT_LECTURES_TABLE_VIEW);
        ObservableList<Lecture> lectures = lecturesTable.getItems();
        for(int i = 0; i < lectures.size(); i++) {
            Lecture lecture = lectures.get(i);
            JsonObject lectureJson = Json.createObjectBuilder()
                .add(JSON_SECTION, lecture.getSection())
                .add(JSON_DAYS, lecture.getDays())
                .add(JSON_TIME, lecture.getTime())
                .add(JSON_ROOM, lecture.getRoom())
                .build();
                lecturesArrayBuilder.add(lectureJson);
        }  
	JsonArray lecturesArray = lecturesArrayBuilder.build();
        
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        TableView<Lab> labsTable = (TableView)gui.getGUINode(MT_LABS_TABLE_VIEW);
        ObservableList<Lab> labs = labsTable.getItems();
        for(int i = 0; i < labs.size(); i++) {
            Lab lab = labs.get(i);
            JsonObject labJson = Json.createObjectBuilder()
                .add(JSON_SECTION, lab.getSection())
                .add(JSON_DAY_TIME, lab.getDaysTime())
                .add(JSON_LOCATION, lab.getRoom())
                .add(JSON_TA_1, lab.getTa1())
                .add(JSON_TA_2, lab.getTa2())
                .build();
                labsArrayBuilder.add(labJson);
        }  
	JsonArray labsArray = labsArrayBuilder.build();
        
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        TableView<Recitation> recitationsTable = (TableView)gui.getGUINode(MT_RECITATIONS_TABLE_VIEW);
        ObservableList<Recitation> recitations = recitationsTable.getItems();
        for(int i = 0; i < recitations.size(); i++) {
            Recitation recitation = recitations.get(i);
            JsonObject recitationJson = Json.createObjectBuilder()
                .add(JSON_SECTION, recitation.getSection())
                .add(JSON_DAY_TIME, recitation.getDaysTime())
                .add(JSON_LOCATION, recitation.getRoom())
                .add(JSON_TA_1, recitation.getTa1())
                .add(JSON_TA_2, recitation.getTa2())
                .build();
                recitationsArrayBuilder.add(recitationJson);
        }  
	JsonArray recitationsArray = recitationsArrayBuilder.build();
        
        JsonObject meetingTimesJSO = Json.createObjectBuilder()
                .add(JSON_SCH_LECTURES, lecturesArray)
                .add(JSON_LABS, labsArray)
                .add(JSON_SCH_RECITATIONS, recitationsArray)
                .build();
       
	JsonWriterFactory writerFactory2 = Json.createWriterFactory(properties);
	StringWriter sw2 = new StringWriter();
	JsonWriter jsonWriter2 = writerFactory.createWriter(sw2);
	jsonWriter2.writeObject(meetingTimesJSO);
	jsonWriter2.close();

	// INIT THE WRITER
        newFilePath = "export/" + dirPath + "/js/SectionsData.json";
        Files.createFile(Paths.get(newFilePath));
	OutputStream os2 = new FileOutputStream(newFilePath);
	JsonWriter jsonFileWriter2 = Json.createWriter(os2);
	jsonFileWriter2.writeObject(meetingTimesJSO);
	String prettyPrinted2 = sw2.toString();
	PrintWriter pw2 = new PrintWriter(newFilePath);
	pw2.write(prettyPrinted2);
	pw2.close();
        
        //OFFICE HOURS EXPORTING
        JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        /*for (Lecture lecture : lectures) {
            String day = lecture.getDays();
            String time = lecture.getTime();
            String ampm = "pm";
            if(time.contains("am")) {
                ampm = "am";
            }
            if(day.contains("M")) {
                day = "MONDAY";
            } else if(day.contains("Tu") || day.contains("TU")) {
                day = "TUESDAY";
            } else if(day.contains("W")) {
                day = "WEDNESDAY";
            } else if(day.contains("Th") || day.contains("TH")) {
                day = "THURSDAY";
            } else if(day.contains("F")) {
                day = "FRIDAY";
            }
            try {
                String[] parsedTime = time.split("-");
                String startTime = parsedTime[0];
                String endTime = parsedTime[1];
                String startTimeHour = startTime.substring(0, startTime.indexOf(':'));
                String startTimeHalfHour = startTime.substring(startTime.indexOf(":") + 1);
                if(Integer.parseInt(startTimeHalfHour) >= 45) {
                    time = Integer.parseInt(startTimeHour) + 1 + "_00" + ampm;
                } else if(Integer.parseInt(startTimeHalfHour) >= 15) {
                    time = Integer.parseInt(startTimeHour) + "_30" + ampm;
                } else {
                    time = Integer.parseInt(startTimeHour) + "_00" + ampm;
                }
                JsonObject lectureObj = Json.createObjectBuilder()
                        .add(JSON_TIME, time)
                        .build();
                officeHoursArrayBuilder.add(lectureObj);
            } catch(Exception e) { }
            
        }*/
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        JsonObject ohJSO = Json.createObjectBuilder()
            .add(JSON_START_HOUR, "" + dataManager.getStartHour())
            .add(JSON_END_HOUR, "" + dataManager.getEndHour())
            .add(JSON_INSTRUCTOR, instructorJson)
            .add(JSON_GRAD_TAS, gradTAsArray)
            .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
            .add(JSON_OFFICE_HOURS, officeHoursArray)
            .build();
        
        JsonWriterFactory writerFactory3 = Json.createWriterFactory(properties);
	StringWriter sw3 = new StringWriter();
	JsonWriter jsonWriter3 = writerFactory.createWriter(sw3);
	jsonWriter3.writeObject(ohJSO);
	jsonWriter3.close();

	// INIT THE WRITER
        newFilePath = "export/" + dirPath + "/js/OfficeHoursData.json";
        Files.createFile(Paths.get(newFilePath));
	OutputStream os3 = new FileOutputStream(newFilePath);
	JsonWriter jsonFileWriter3 = Json.createWriter(os3);
	jsonFileWriter3.writeObject(ohJSO);
	String prettyPrinted3 = sw3.toString();
	PrintWriter pw3 = new PrintWriter(newFilePath);
	pw3.write(prettyPrinted3);
	pw3.close();
        
        //SCHEDULE EXPORTING
        DatePicker mondayDate = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker fridayDate = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker date = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField schTitle = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField schTopic = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField schLink = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        SiteGeneratorData siteData = (SiteGeneratorData) app.getDataComponent();
        ObservableList<ScheduleItem> items = siteData.getAllItems();
        
        String mondayMonth = "";
        String mondayDay = "";
        String fridayMonth = "";
        String fridayDay = "";
        if(mondayDate.getValue() != null) {
            mondayMonth = mondayDate.getValue().getMonthValue() + "";
            mondayDay = mondayDate.getValue().getDayOfMonth() + "";
        }
        if(fridayDate.getValue() != null) {
            fridayMonth = fridayDate.getValue().getMonthValue() + "";
            fridayDay = fridayDate.getValue().getDayOfMonth() + "";
        }
        
        JsonArrayBuilder holidaysArrayBuilder = Json.createArrayBuilder();
        lecturesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        recitationsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
        
        for(int i = 0; i < items.size(); i++) {
            ScheduleItem item = items.get(i);
            String[] splitDate = item.getDate().split("/");
            String month = splitDate[0];
            String day = splitDate[1];
            if(item.getType().equalsIgnoreCase("Holiday")) {
                JsonObject holidayJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_LINK, item.getLink())
                        .build();
                holidaysArrayBuilder.add(holidayJson);
            } else if(item.getType().equalsIgnoreCase("Lecture")) {
                JsonObject lectureJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                lecturesArrayBuilder.add(lectureJson);
            } else if(item.getType().equalsIgnoreCase("Reference")) {
                JsonObject referenceJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                referencesArrayBuilder.add(referenceJson);
            } else if(item.getType().equalsIgnoreCase("Recitation")) {
                JsonObject recitationJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .build();
                recitationsArrayBuilder.add(recitationJson);
            } else if(item.getType().equalsIgnoreCase("Homework")) {
                JsonObject hwJson = Json.createObjectBuilder()
                        .add(JSON_MONTH, month)
                        .add(JSON_DAY, day)
                        .add(JSON_TITLE, item.getTitle())
                        .add(JSON_TOPIC, item.getTopic())
                        .add(JSON_LINK, item.getLink())
                        .add(JSON_TIME, "")
                        .add(JSON_CRITERIA, "none")
                        .build();
                hwsArrayBuilder.add(hwJson);
            }
        }
        JsonArray holidaysArray = holidaysArrayBuilder.build();
        JsonArray schLecturesArray = lecturesArrayBuilder.build();
        JsonArray referencesArray = referencesArrayBuilder.build();
	JsonArray schRecitationsArray = recitationsArrayBuilder.build();
        JsonArray hwsArray = hwsArrayBuilder.build();
        
        JsonObject schJSO = Json.createObjectBuilder()
                .add(JSON_MONDAY_MONTH, mondayMonth)
                .add(JSON_MONDAY_DAY, mondayDay)
                .add(JSON_FRIDAY_MONTH, fridayMonth)
                .add(JSON_FRIDAY_DAY, fridayDay)
                .add(JSON_HOLIDAYS, holidaysArray)
                .add(JSON_SCH_LECTURES, schLecturesArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_SCH_RECITATIONS, schRecitationsArray)
                .add(JSON_HWS, hwsArray)
		.build();
        
        JsonWriterFactory writerFactory4 = Json.createWriterFactory(properties);
	StringWriter sw4 = new StringWriter();
	JsonWriter jsonWriter4 = writerFactory.createWriter(sw4);
	jsonWriter4.writeObject(schJSO);
	jsonWriter4.close();

	// INIT THE WRITER
        newFilePath = "export/" + dirPath + "/js/ScheduleData.json";
        Files.createFile(Paths.get(newFilePath));
	OutputStream os4 = new FileOutputStream(newFilePath);
	JsonWriter jsonFileWriter4 = Json.createWriter(os4);
	jsonFileWriter4.writeObject(schJSO);
	String prettyPrinted4 = sw4.toString();
	PrintWriter pw4 = new PrintWriter(newFilePath);
	pw4.write(prettyPrinted4);
	pw4.close();
        
        //SYLLABUS EXPORTING
        TextArea descT = (TextArea) gui.getGUINode(SYL_DESCRIPTION_TEXT_AREA);
        TextArea topicsT = (TextArea) gui.getGUINode(SYL_TOPICS_TEXT_AREA);
        TextArea prereqsT = (TextArea) gui.getGUINode(SYL_PREREQS_TEXT_AREA);
        TextArea outcomesT = (TextArea) gui.getGUINode(SYL_OUTCOMES_TEXT_AREA);
        TextArea textbooksT = (TextArea) gui.getGUINode(SYL_TEXTBOOKS_TEXT_AREA);
        TextArea componentsT = (TextArea) gui.getGUINode(SYL_COMPONENTS_TEXT_AREA);
        TextArea noteT = (TextArea) gui.getGUINode(SYL_NOTE_TEXT_AREA);
        TextArea dishonestyT = (TextArea) gui.getGUINode(SYL_DISHONESTY_TEXT_AREA);
        TextArea assistanceT = (TextArea) gui.getGUINode(SYL_ASSISTANCE_TEXT_AREA);
        
        
        JsonArray topics;
        try {
            JsonReader jsonTopicsReader = Json.createReader(new StringBufferInputStream(topicsT.getText()));
            topics = jsonTopicsReader.readArray();
        } catch(Exception e) {
            topics = Json.createArrayBuilder().build();
        }
        
            JsonArray outcomes;
        try {
            JsonReader jsonOutcomesReader = Json.createReader(new StringBufferInputStream(outcomesT.getText()));
            outcomes = jsonOutcomesReader.readArray();
        } catch(Exception e) {
            outcomes = Json.createArrayBuilder().build();
        }
        
            JsonArray textbooks;
        try {
            JsonReader jsonTextbooksReader = Json.createReader(new StringBufferInputStream(textbooksT.getText()));
            textbooks = jsonTextbooksReader.readArray();
        } catch(Exception e) {
            textbooks = Json.createArrayBuilder().build();
        }
        
            JsonArray components;
        try {
            JsonReader jsonComponentsReader = Json.createReader(new StringBufferInputStream(componentsT.getText()));
            components = jsonComponentsReader.readArray();
        } catch(Exception e) {
            components = Json.createArrayBuilder().build();
        }
        
        JsonObject sylJSO = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, descT.getText()) //string
                .add(JSON_TOPICS, topics) //array
                .add(JSON_PREREQS, prereqsT.getText()) //string
                .add(JSON_OUTCOMES, outcomes) ////array
                .add(JSON_TEXTBOOKS, textbooks) //array... complicated
                .add(JSON_COMPONENTS, components) //array of objects
                .add(JSON_NOTE, noteT.getText()) //string
                .add(JSON_DISHONESTY, dishonestyT.getText()) //string
                .add(JSON_ASSISTANCE, assistanceT.getText()) //string
                .build();
        
        JsonWriterFactory writerFactory5 = Json.createWriterFactory(properties);
	StringWriter sw5 = new StringWriter();
	JsonWriter jsonWriter5 = writerFactory.createWriter(sw5);
	jsonWriter5.writeObject(sylJSO);
	jsonWriter5.close();

	// INIT THE WRITER
        newFilePath = "export/" + dirPath + "/js/SyllabusData.json";
        Files.createFile(Paths.get(newFilePath));
	OutputStream os5 = new FileOutputStream(newFilePath);
	JsonWriter jsonFileWriter5 = Json.createWriter(os5);
	jsonFileWriter5.writeObject(sylJSO);
	String prettyPrinted5 = sw5.toString();
	PrintWriter pw5 = new PrintWriter(newFilePath);
	pw5.write(prettyPrinted5);
	pw5.close();
        
        //ALMOST DONE BRO - JUST COPY OVER THE IMAGES FOLDER AND YOU'RE GUCCI
        Files.createDirectory(Paths.get("export/" + dirPath + "/images"));
        FileUtils.copyDirectory(new File("images"), new File("export/" + dirPath + "/images"));
    }
}