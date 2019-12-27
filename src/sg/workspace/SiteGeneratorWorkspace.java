package sg.workspace;

import static djf.AppPropertyType.APP_EXPORT_PAGE;
import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.*;
import sg.data.Lab;
import sg.data.Lecture;
import sg.data.Recitation;
import sg.data.ScheduleItem;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;
import sg.workspace.controllers.MeetingTimesController;
import sg.workspace.controllers.OfficeHoursController;
import sg.workspace.controllers.ScheduleController;
import sg.workspace.controllers.SiteController;
import sg.workspace.dialogs.TADialog;
import sg.workspace.foolproof.SiteGeneratorFoolproofDesign;
import sg.workspace.tabs.MeetingTimesTab;
import sg.workspace.tabs.OfficeHoursTab;
import sg.workspace.tabs.ScheduleTab;
import sg.workspace.tabs.SiteTab;
import sg.workspace.tabs.SyllabusTab;

/**
 *
 * @author McKillaGorilla
 */
public class SiteGeneratorWorkspace extends AppWorkspaceComponent {
    TabPane tabs;
    SiteTab site;
    SyllabusTab syllabus;
    MeetingTimesTab meetingTimes;
    OfficeHoursTab officeHours;
    ScheduleTab schedule;
    boolean changeCombo;
    boolean changeDate;
    boolean process;
    
    public SiteGeneratorWorkspace(SiteGeneratorApp app) {
        super(app);
        //Build objects
        site = new SiteTab(app);
        tabs = new TabPane();
        syllabus = new SyllabusTab(app);
        meetingTimes = new MeetingTimesTab(app);
        officeHours = new OfficeHoursTab(app);
        schedule = new ScheduleTab(app);
        changeCombo = true;
        changeDate = true;
        process = true;

        // LAYOUT THE APP
        initLayout();
        
        // LOAD SETTINGS
        initSettings();
        
        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    public void changeCombo(boolean b) {
        changeCombo = b;
    }
    
    public void changeDate(boolean b) {
        changeDate = b;
    }
    
    public void process(boolean b) {
        process = b;
    }
    
    public TabPane getTabs() {
        return tabs;
    }
    
    private void initDialogs() {
        TADialog taDialog = new TADialog((SiteGeneratorApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        //Create tabs
        Tab siteTab = new Tab();
        siteTab.setText("Site");
        Tab syllabusTab = new Tab();
        syllabusTab.setText("Syllabus");
        Tab meetingTimesTab = new Tab();
        meetingTimesTab.setText("Meeting Times");
        Tab ohTab = new Tab();
        ohTab.setText("Office Hours");
        Tab scheduleTab = new Tab();
        scheduleTab.setText("Schedule");
        //Set tab content to object data
        ScrollPane scrollSite = new ScrollPane();
        scrollSite.setFitToHeight(true);
        scrollSite.setFitToWidth(true);
        scrollSite.setContent(site.getSitePane());
        siteTab.setContent(scrollSite);
        ScrollPane scrollSyllabus = new ScrollPane();
        scrollSyllabus.setFitToHeight(true);
        scrollSyllabus.setFitToWidth(true);
        scrollSyllabus.setContent(syllabus.getSyllabusPane());
        syllabusTab.setContent(scrollSyllabus);
        ScrollPane scrollMT = new ScrollPane();
        scrollMT.setFitToHeight(true);
        scrollMT.setFitToWidth(true);
        scrollMT.setContent(meetingTimes.getMeetingTimesPane());
        meetingTimesTab.setContent(scrollMT);
        ScrollPane scrollOH = new ScrollPane();
        scrollOH.setFitToHeight(true);
        scrollOH.setFitToWidth(true);
        scrollOH.setContent(officeHours.getOhPane());
        ohTab.setContent(scrollOH);
        ScrollPane scrollSch = new ScrollPane();
        scrollSch.setFitToHeight(true);
        scrollSch.setFitToWidth(true);
        scrollSch.setContent(schedule.getSchedulePane());
        scheduleTab.setContent(scrollSch);
        tabs.getTabs().add(siteTab);
        tabs.getTabs().add(syllabusTab);
        tabs.getTabs().add(meetingTimesTab);
        tabs.getTabs().add(ohTab);
        tabs.getTabs().add(scheduleTab);
        //Tabs shouldn't be closeable
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //Now instantiate the workspace
        workspace = new BorderPane();
        // AND PUT EVERYTHING IN IT
        ((BorderPane) workspace).setCenter(tabs);
        tabs.tabMinWidthProperty().bind(workspace.widthProperty().divide(tabs.getTabs().size()).subtract(20));
    }

    public void initSettings() {
        ObservableList<String> subjectSettings = FXCollections.observableArrayList();
        ObservableList<String> numberSettings = FXCollections.observableArrayList();
        ComboBox subjectCombo = (ComboBox)(app.getGUIModule().getGUINode(SITE_SUBJECT_COMBO));
        ComboBox numberCombo = (ComboBox)(app.getGUIModule().getGUINode(SITE_NUMBER_COMBO));
        try {
            File subjects = new File("./app_data/recent/SavedSubjects.txt");
            Scanner subjectScanner = new Scanner(subjects);
            File numbers = new File("./app_data/recent/SavedNumbers.txt");
            Scanner numberScanner = new Scanner(numbers);
            while(subjectScanner.hasNextLine()) {
                subjectSettings.add(subjectScanner.nextLine());
            }
            while(numberScanner.hasNextLine()) {
                numberSettings.add(numberScanner.nextLine());
            }
            subjectScanner.close();
            numberScanner.close();
        } catch(FileNotFoundException e) { //File will be found, but just in case...
            System.out.println("Dude did you actually delete the settings?");
        }
        if(subjectSettings.size() > 1) {
            subjectCombo.setItems(subjectSettings);
            Collections.sort(subjectCombo.getItems());
        }
        if(numberSettings.size() > 1) {
            numberCombo.setItems(numberSettings);
            Collections.sort(numberCombo.getItems());
        }
    }
    
    public void initControllers() {
        OfficeHoursController controller = new OfficeHoursController((SiteGeneratorApp) app);
        MeetingTimesController mtController = new MeetingTimesController((SiteGeneratorApp)app);
        SiteController siteController = new SiteController((SiteGeneratorApp) app);
        ScheduleController schController = new ScheduleController((SiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            TeachingAssistantPrototype ta = (TeachingAssistantPrototype)tasTableView.getSelectionModel().getSelectedItem();
            controller.processToggleOfficeHours();
            SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
            data.updateTAs();
            tasTableView.getSelectionModel().select(ta);
        });

        // DON'T LET ANYONE SORT THE TABLES
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
            tasTableView.refresh();
            officeHoursTableView.refresh();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
            tasTableView.refresh();
            officeHoursTableView.refresh();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
            tasTableView.refresh();
            officeHoursTableView.refresh();
        });
        
        ComboBox startCombo = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        ComboBox endCombo = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        startCombo.setOnAction(e -> {
            if(startCombo.getValue() != null && changeCombo && process) {
                controller.processStartCombo(startCombo);
                //controller.processUpdateTimes();
                //controller.processUpdateEndTimes();
                changeCombo = true; //really don't need this, but whateva
            }
        });
        endCombo.setOnAction(e-> {
            if(endCombo.getValue() != null && changeCombo && process) {
                controller.processEndCombo(endCombo);
                //controller.processUpdateTimes();
                //controller.processUpdateStartTimes();
                changeCombo = true; //meh
            }
        });
        
        Button taRemove = (Button) gui.getGUINode(OH_REMOVE_TA_BUTTON);
        taRemove.setOnAction(e-> {
            controller.processRemoveTA();
        });
        
        //SITE TAB CONTROLS
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ComboBox subjectCombo = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBO);
        ComboBox numberCombo = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBO);
        ComboBox semesterCombo = (ComboBox) gui.getGUINode(SITE_SEMESTER_COMBO);
        ComboBox yearCombo = (ComboBox) gui.getGUINode(SITE_YEAR_COMBO);
        TextField title = (TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        Label dir = (Label) gui.getGUINode(SITE_EXPORT_LABEL);
        
        subjectCombo.setOnAction(e -> { //For selecting an option
            siteController.processUpdateDirectory();
            if(process) {
                app.getTPS().pop(); //last transaction is indentical, but a text field edit
                siteController.processComboBoxChange(subjectCombo);
            }
        });
        subjectCombo.getEditor().textProperty().addListener(e-> { //For typing
            boolean b = process;
            process = false;
            subjectCombo.setValue(subjectCombo.getEditor().getText()); //saves value w/o processing
            process = b;
            siteController.processUpdateDirectory();
            if(process)
               siteController.processEditorChange(subjectCombo);
            props.addProperty(APP_EXPORT_PAGE, dir.getText());
        });
        subjectCombo.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> { //ENTER
            if(e.getCode().equals(KeyCode.ENTER)) {
                siteController.processAddSubject();
            }
        });
        numberCombo.setOnAction(e -> {
            siteController.processUpdateDirectory();
            if(process) {
                app.getTPS().pop();
                siteController.processComboBoxChange(numberCombo);
            }
        });
        numberCombo.getEditor().textProperty().addListener(e-> {
            boolean b = process;
            process = false;
            numberCombo.setValue(numberCombo.getEditor().getText());
            process = b;
            siteController.processUpdateDirectory();
            if(process)
               siteController.processEditorChange(numberCombo);
            props.addProperty(APP_EXPORT_PAGE, dir.getText());
        });
        numberCombo.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> { //ENTER
            if(e.getCode().equals(KeyCode.ENTER)) {
                siteController.processAddNumber();
            }
        });
        semesterCombo.setOnAction(e -> {
            siteController.processUpdateDirectory();
            if(process) {
                app.getTPS().pop();
                siteController.processComboBoxChange(semesterCombo);
            }
        });
        semesterCombo.getEditor().textProperty().addListener(e-> {
            boolean b = process;
            process = false;
            semesterCombo.setValue(semesterCombo.getEditor().getText());
            process = b;
            siteController.processUpdateDirectory();
            if(process)
               siteController.processEditorChange(semesterCombo);
            props.addProperty(APP_EXPORT_PAGE, dir.getText());
        });
        yearCombo.setOnAction(e -> {
            siteController.processUpdateDirectory();
            if(process) {
                app.getTPS().pop();
                siteController.processComboBoxChange(yearCombo);
            }
        });
        yearCombo.getEditor().textProperty().addListener(e-> {
            boolean b = process;
            process = false;
            yearCombo.setValue(yearCombo.getEditor().getText());
            process = b;
            siteController.processUpdateDirectory();
            if(process)
               siteController.processEditorChange(yearCombo);
            props.addProperty(APP_EXPORT_PAGE, dir.getText());
        });
        
        title.textProperty().addListener(e->{
            if(process)
               siteController.processTextChange(title);
        });
        
        CheckBox homeCheck = (CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox sylCheck = (CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox schCheck = (CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox hwsCheck = (CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX);
        
        homeCheck.setOnAction(e->{
            if(process)
                siteController.processCheckBoxChange(homeCheck);
        });
        
        sylCheck.setOnAction(e->{
            if(process)
                siteController.processCheckBoxChange(sylCheck);
        });
        
        schCheck.setOnAction(e->{
            if(process)
                siteController.processCheckBoxChange(schCheck);
        });
        
        hwsCheck.setOnAction(e->{
            if(process)
                siteController.processCheckBoxChange(hwsCheck);
        });
        
        Button favicon = (Button) gui.getGUINode(SITE_FAVICON_BUTTON);
        Button navbar = (Button) gui.getGUINode(SITE_NAVBAR_BUTTON);
        Button leftFooter = (Button) gui.getGUINode(SITE_LEFT_FOOTER_BUTTON);
        Button rightFooter = (Button) gui.getGUINode(SITE_RIGHT_FOOTER_BUTTON);
        ComboBox cssCombo = (ComboBox) gui.getGUINode(SITE_CSS_COMBO);
        
        favicon.setOnAction(e -> {
            if(process)
            siteController.processUpdateFavicon();
        });
        navbar.setOnAction(e -> {
            if(process)
            siteController.processUpdateNavbar();
        });
        leftFooter.setOnAction(e -> {
            if(process)
            siteController.processUpdateLeftFooter();
        });
        rightFooter.setOnAction(e -> {
            if(process)
            siteController.processUpdateRightFooter();
        });
        
        cssCombo.setOnAction(e-> {
            if(process)
                siteController.processComboBoxChange(cssCombo);
        });
        
        Button ohTextButton = (Button) gui.getGUINode(SITE_OH_BUTTON);
        TextArea ohTextArea = (TextArea) gui.getGUINode(SITE_OH_TEXT_AREA);
        ohTextButton.setOnAction(e -> {
            if(ohTextButton.getText().equals("-")) {
                ohTextArea.setVisible(false);
                ohTextButton.setText("+");
            } else {
                ohTextArea.setVisible(true);
                ohTextButton.setText("-");
            }
        });
        ohTextArea.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(ohTextArea);
        });
        
        //SYLLABUS TAB CONTROLS
        
        Button descriptionButton = (Button) gui.getGUINode(SYL_DESCRIPTION_BUTTON);
        VBox descriptionPane = (VBox) gui.getGUINode(SYL_DESCRIPTION_PANE);
        TextArea descriptionText = (TextArea) gui.getGUINode(SYL_DESCRIPTION_TEXT_AREA);
        
        descriptionButton.setOnAction(e-> {
            if(descriptionButton.getText().equals("-")) { //collapse
                descriptionText.setVisible(false);
                descriptionPane.setMinHeight(50);
                descriptionPane.setMaxHeight(50);
                descriptionButton.setText("+");
            } else {
                descriptionText.setVisible(true);
                descriptionPane.setMinHeight(288);
                descriptionPane.setMaxHeight(288);
                descriptionButton.setText("-");
            }
        });
        descriptionText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(descriptionText);
        });
        
        Button topicsButton = (Button) gui.getGUINode(SYL_TOPICS_BUTTON);
        VBox topicsPane = (VBox) gui.getGUINode(SYL_TOPICS_PANE);
        TextArea topicsText = (TextArea) gui.getGUINode(SYL_TOPICS_TEXT_AREA);
        
        topicsButton.setOnAction(e-> {
            if(topicsButton.getText().equals("-")) { //collapse
                topicsText.setVisible(false);
                topicsPane.setMinHeight(50);
                topicsPane.setMaxHeight(50);
                topicsButton.setText("+");
            } else {
                topicsText.setVisible(true);
                topicsPane.setMinHeight(288);
                topicsPane.setMaxHeight(288);
                topicsButton.setText("-");
            }
        });
        topicsText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(topicsText);
        });
        
        Button prereqsButton = (Button) gui.getGUINode(SYL_PREREQS_BUTTON);
        VBox prereqsPane = (VBox) gui.getGUINode(SYL_PREREQS_PANE);
        TextArea prereqsText = (TextArea) gui.getGUINode(SYL_PREREQS_TEXT_AREA);
        
        prereqsButton.setOnAction(e-> {
            if(prereqsButton.getText().equals("-")) { //collapse
                prereqsText.setVisible(false);
                prereqsPane.setMinHeight(50);
                prereqsPane.setMaxHeight(50);
                prereqsButton.setText("+");
            } else {
                prereqsText.setVisible(true);
                prereqsPane.setMinHeight(288);
                prereqsPane.setMaxHeight(288);
                prereqsButton.setText("-");
            }
        });
        prereqsText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(prereqsText);
        });
        
        Button outcomesButton = (Button) gui.getGUINode(SYL_OUTCOMES_BUTTON);
        VBox outcomesPane = (VBox) gui.getGUINode(SYL_OUTCOMES_PANE);
        TextArea outcomesText = (TextArea) gui.getGUINode(SYL_OUTCOMES_TEXT_AREA);
        
        outcomesButton.setOnAction(e-> {
            if(outcomesButton.getText().equals("-")) { //collapse
                outcomesText.setVisible(false);
                outcomesPane.setMinHeight(50);
                outcomesPane.setMaxHeight(50);
                outcomesButton.setText("+");
            } else {
                outcomesText.setVisible(true);
                outcomesPane.setMinHeight(288);
                outcomesPane.setMaxHeight(288);
                outcomesButton.setText("-");
            }
        });
        outcomesText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(outcomesText);
        });
        
        Button textbooksButton = (Button) gui.getGUINode(SYL_TEXTBOOKS_BUTTON);
        VBox textbooksPane = (VBox) gui.getGUINode(SYL_TEXTBOOKS_PANE);
        TextArea textbooksText = (TextArea) gui.getGUINode(SYL_TEXTBOOKS_TEXT_AREA);
        
        textbooksButton.setOnAction(e-> {
            if(textbooksButton.getText().equals("-")) { //collapse
                textbooksText.setVisible(false);
                textbooksPane.setMinHeight(50);
                textbooksPane.setMaxHeight(50);
                textbooksButton.setText("+");
            } else {
                textbooksText.setVisible(true);
                textbooksPane.setMinHeight(288);
                textbooksPane.setMaxHeight(288);
                textbooksButton.setText("-");
            }
        });
        textbooksText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(textbooksText);
        });
        
        Button componentsButton = (Button) gui.getGUINode(SYL_COMPONENTS_BUTTON);
        VBox componentsPane = (VBox) gui.getGUINode(SYL_COMPONENTS_PANE);
        TextArea componentsText = (TextArea) gui.getGUINode(SYL_COMPONENTS_TEXT_AREA);
        
        componentsButton.setOnAction(e-> {
            if(componentsButton.getText().equals("-")) { //collapse
                componentsText.setVisible(false);
                componentsPane.setMinHeight(50);
                componentsPane.setMaxHeight(50);
                componentsButton.setText("+");
            } else {
                componentsText.setVisible(true);
                componentsPane.setMinHeight(288);
                componentsPane.setMaxHeight(288);
                componentsButton.setText("-");
            }
        });
        componentsText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(componentsText);
        });
        
        Button noteButton = (Button) gui.getGUINode(SYL_NOTE_BUTTON);
        VBox notePane = (VBox) gui.getGUINode(SYL_NOTE_PANE);
        TextArea noteText = (TextArea) gui.getGUINode(SYL_NOTE_TEXT_AREA);
        
        noteButton.setOnAction(e-> {
            if(noteButton.getText().equals("-")) { //collapse
                noteText.setVisible(false);
                notePane.setMinHeight(50);
                notePane.setMaxHeight(50);
                noteButton.setText("+");
            } else {
                noteText.setVisible(true);
                notePane.setMinHeight(288);
                notePane.setMaxHeight(288);
                noteButton.setText("-");
            }
        });
        noteText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(noteText);
        });
        
        Button dishonestyButton = (Button) gui.getGUINode(SYL_DISHONESTY_BUTTON);
        VBox dishonestyPane = (VBox) gui.getGUINode(SYL_DISHONESTY_PANE);
        TextArea dishonestyText = (TextArea) gui.getGUINode(SYL_DISHONESTY_TEXT_AREA);
        
        dishonestyButton.setOnAction(e-> {
            if(dishonestyButton.getText().equals("-")) { //collapse
                dishonestyText.setVisible(false);
                dishonestyPane.setMinHeight(50);
                dishonestyPane.setMaxHeight(50);
                dishonestyButton.setText("+");
            } else {
                dishonestyText.setVisible(true);
                dishonestyPane.setMinHeight(288);
                dishonestyPane.setMaxHeight(288);
                dishonestyButton.setText("-");
            }
        });
        dishonestyText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(dishonestyText);
        });
        
        Button assistanceButton = (Button) gui.getGUINode(SYL_ASSISTANCE_BUTTON);
        VBox assistancePane = (VBox) gui.getGUINode(SYL_ASSISTANCE_PANE);
        TextArea assistanceText = (TextArea) gui.getGUINode(SYL_ASSISTANCE_TEXT_AREA);
        
        assistanceButton.setOnAction(e-> {
            if(assistanceButton.getText().equals("-")) { //collapse
                assistanceText.setVisible(false);
                assistancePane.setMinHeight(50);
                assistancePane.setMaxHeight(50);
                assistanceButton.setText("+");
            } else {
                assistanceText.setVisible(true);
                assistancePane.setMinHeight(288);
                assistancePane.setMaxHeight(288);
                assistanceButton.setText("-");
            }
        });
        assistanceText.textProperty().addListener(e->{
            if(process)
                siteController.processTextAreaChange(assistanceText);
        });
        
        //MEETING TIMES TAB CONTROLS
        Button addLectureButton = (Button) gui.getGUINode(MT_LECTURES_ADD_BUTTON);
        Button addRecitationButton = (Button) gui.getGUINode(MT_RECITATIONS_ADD_BUTTON);
        Button addLabButton = (Button) gui.getGUINode(MT_LABS_ADD_BUTTON);
        Button removeLectureButton = (Button) gui.getGUINode(MT_LECTURES_REMOVE_BUTTON);
        Button removeRecitationButton = (Button) gui.getGUINode(MT_RECITATIONS_REMOVE_BUTTON);
        Button removeLabButton = (Button) gui.getGUINode(MT_LABS_REMOVE_BUTTON);
        TableView<Lecture> lectures = (TableView) gui.getGUINode(MT_LECTURES_TABLE_VIEW);
        TableColumn lectureSections = lectures.getColumns().get(0);
        TableColumn lectureDays = lectures.getColumns().get(1);
        TableColumn lectureTimes = lectures.getColumns().get(2);
        TableColumn lectureRooms = lectures.getColumns().get(3);
        TableView<Recitation> recitations = (TableView) gui.getGUINode(MT_RECITATIONS_TABLE_VIEW);
        TableColumn recitationSections = recitations.getColumns().get(0);
        TableColumn recitationDays = recitations.getColumns().get(1);
        TableColumn recitationRooms = recitations.getColumns().get(2);
        TableColumn recitationTA1 = recitations.getColumns().get(3);
        TableColumn recitationTA2 = recitations.getColumns().get(4);
        TableView<Lab> labs = (TableView) gui.getGUINode(MT_LABS_TABLE_VIEW);
        TableColumn labSections = labs.getColumns().get(0);
        TableColumn labDays = labs.getColumns().get(1);
        TableColumn labRooms = labs.getColumns().get(2);
        TableColumn labTA1 = labs.getColumns().get(3);
        TableColumn labTA2 = labs.getColumns().get(4);
        
        addLectureButton.setOnAction(e->{
            mtController.processAddLecture();
        });
        
        removeLectureButton.setOnAction(e-> {
            mtController.processRemoveLecture();
        });
        
        lectureSections.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                    mtController.processEditLectureSection(t);
                }
            }
        );
        
        lectureDays.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                    mtController.processEditLectureDay(t);
                }
            }
        );
        
        lectureTimes.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                    mtController.processEditLectureTime(t);
                }
            }
        );
        
        lectureRooms.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                    mtController.processEditLectureRoom(t);
                }
            }
        );
        
        addRecitationButton.setOnAction(e->{
            mtController.processAddRecitation();
        });
        
        removeRecitationButton.setOnAction(e-> {
            mtController.processRemoveRecitation();
        });
        
        recitationSections.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Recitation, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                    mtController.processEditRecitationSection(t);
                }
            }
        );
        
        recitationDays.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Recitation, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                    mtController.processEditRecitationDay(t);
                }
            }
        );
        
        recitationRooms.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Recitation, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                    mtController.processEditRecitationRoom(t);
                }
            }
        );
        
        recitationTA1.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Recitation, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                    mtController.processEditRecitationTA1(t);
                }
            }
        );
        
        recitationTA2.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Recitation, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                    mtController.processEditRecitationTA2(t);
                }
            }
        );
        
        addLabButton.setOnAction(e->{
            mtController.processAddLab();
        });
        
        removeLabButton.setOnAction(e-> {
            mtController.processRemoveLab();
        });
        
        labSections.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lab, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                    mtController.processEditLabSection(t);
                }
            }
        );
        
        labDays.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lab, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                    mtController.processEditLabDay(t);
                }
            }
        );
        
        labRooms.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lab, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                    mtController.processEditLabRoom(t);
                }
            }
        );
        
        labTA1.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lab, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                    mtController.processEditLabTA1(t);
                }
            }
        );
        
        labTA2.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lab, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                    mtController.processEditLabTA2(t);
                }
            }
        );
        
        //SCHEDULE TAB CONTROLS
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField titleText = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topicText = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField linkText = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        Button removeButton = (Button) gui.getGUINode(SCH_ITEMS_BUTTON);
        Button clearButton = (Button) gui.getGUINode(SCH_CLEAR_BUTTON);
        TableView<ScheduleItem> itemsTable = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        DatePicker startPicker = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker endPicker = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        
        addButton.setOnAction(e->{
            if(addButton.getText().equals(props.getProperty(SCH_ADD_BUTTON_TEXT))) {
                schController.processAddScheduleItem();
            } else { //update
                schController.processUpdateScheduleItem();
            }
        });
        
        removeButton.setOnAction(e->{
            schController.processRemoveScheduleItem();
        });
        
        itemsTable.setOnMouseClicked(e-> {
            if(itemsTable.getSelectionModel().getSelectedItem() != null) {
                schController.processFillFields();
            }
        });
        
        clearButton.setOnAction(e->{
            SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
            data.processClearFields();
        });
        
        //SCHEDULE FOOLPROOF DESIGN STUFF
        datePicker.setOnAction(e-> {
            schController.processButtonEnable();
        });
        
        startPicker.setOnAction(e-> {
            if(changeDate) {
                SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
                schController.updateStartDate();
                schController.processButtonEnable();
                data.updateItems();
            }
        });
        
        endPicker.setOnAction(e-> {
            if(changeDate) {
                SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
                schController.updateEndDate();
                schController.processButtonEnable();
                data.updateItems();
            }
        });
        
       //jTPS FUNCTIONALITY
       //Text fields
       TextField nameText = (TextField) gui.getGUINode(SITE_NAME_TEXT_FIELD);
       TextField emailText = (TextField) gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
       TextField roomText = (TextField) gui.getGUINode(SITE_ROOM_TEXT_FIELD);
       TextField homeText = (TextField) gui.getGUINode(SITE_HOME_PAGE_TEXT_FIELD);
       
       nameText.textProperty().addListener(e->{
            if(process) {
                siteController.processTextChange(nameText);
            }
       });
       emailText.textProperty().addListener(e->{
            if(process) {
                siteController.processTextChange(emailText);
            }
       });
       roomText.textProperty().addListener(e->{
            if(process) {
                siteController.processTextChange(roomText);
            }
       });
       homeText.textProperty().addListener(e->{
            if(process) {
                siteController.processTextChange(homeText);
            }
       });
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new SiteGeneratorFoolproofDesign((SiteGeneratorApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
}
