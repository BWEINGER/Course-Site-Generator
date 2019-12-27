package sg.workspace.tabs;
        
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import sg.SiteGeneratorPropertyType;
import static sg.SiteGeneratorPropertyType.OH_ADD_TA_BUTTON;
import static sg.SiteGeneratorPropertyType.OH_ADD_TA_PANE;
import static sg.SiteGeneratorPropertyType.OH_ALL_RADIO_BUTTON;
import static sg.SiteGeneratorPropertyType.OH_EMAIL_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_EMAIL_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.OH_END_COMBO_BOX;
import static sg.SiteGeneratorPropertyType.OH_END_TIMES;
import static sg.SiteGeneratorPropertyType.OH_END_TIME_DEFAULT;
import static sg.SiteGeneratorPropertyType.OH_END_TIME_LABEL;
import static sg.SiteGeneratorPropertyType.OH_END_TIME_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_FRIDAY_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_GRAD_RADIO_BUTTON;
import static sg.SiteGeneratorPropertyType.OH_GRAD_UNDERGRAD_TAS_PANE;
import static sg.SiteGeneratorPropertyType.OH_LEFT_PANE;
import static sg.SiteGeneratorPropertyType.OH_MONDAY_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_NAME_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_NAME_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.OH_OFFICE_HOURS_HEADER_LABEL;
import static sg.SiteGeneratorPropertyType.OH_OFFICE_HOURS_HEADER_PANE;
import static sg.SiteGeneratorPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.OH_OFFICE_HOURS_TIME_PANE;
import static sg.SiteGeneratorPropertyType.OH_OUTER_PANE;
import static sg.SiteGeneratorPropertyType.OH_REMOVE_TA_BUTTON;
import static sg.SiteGeneratorPropertyType.OH_RIGHT_PANE;
import static sg.SiteGeneratorPropertyType.OH_SLOTS_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_START_COMBO_BOX;
import static sg.SiteGeneratorPropertyType.OH_START_TIMES;
import static sg.SiteGeneratorPropertyType.OH_START_TIME_DEFAULT;
import static sg.SiteGeneratorPropertyType.OH_START_TIME_LABEL;
import static sg.SiteGeneratorPropertyType.OH_START_TIME_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_TAS_HEADER_PANE;
import static sg.SiteGeneratorPropertyType.OH_TAS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.OH_THURSDAY_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_TUESDAY_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_TYPE_TABLE_COLUMN;
import static sg.SiteGeneratorPropertyType.OH_UNDERGRAD_RADIO_BUTTON;
import static sg.SiteGeneratorPropertyType.OH_WEDNESDAY_TABLE_COLUMN;
import sg.data.TeachingAssistantPrototype;
import sg.data.TimeSlot;
import static sg.workspace.style.SGStyle.CLASS_OH_BOX;
import static sg.workspace.style.SGStyle.CLASS_OH_CENTERED_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_OH_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_OH_DAY_OF_WEEK_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_OH_OFFICE_HOURS_TABLE_VIEW;
import static sg.workspace.style.SGStyle.CLASS_OH_TABLE_VIEW;
import static sg.workspace.style.SGStyle.CLASS_OH_TIME_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOLD_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_COMBO_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_PANE;
import static sg.workspace.style.SGStyle.CLASS_SITE_RADIO_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_TEXT_FIELD;

/**
 *
 * @author Brett Weinger
 */
public class OfficeHoursTab {
    
    SiteGeneratorApp app;
    VBox ohPane;

    public OfficeHoursTab(SiteGeneratorApp initApp) {
        app = initApp;
        ohPane = new VBox();
        ohPane.setSpacing(20);
        ohPane.setStyle(("-fx-background-color: #fcd0a4"));
        ohPane.setPadding(new Insets(3, 2, 3, 2));
       
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        //initControllers();

        // INIT FOOLPROOF DESIGN
        //initFoolproofDesign();

        // INIT DIALOGS
        //initDialogs();
    }

    public VBox getOhPane() {
        return ohPane;
    }

    /*private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteGeneratorApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }*/

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();

        VBox outerPane = ohBuilder.buildVBox(OH_OUTER_PANE, ohPane, CLASS_SITE_BOX, ENABLED);
        outerPane.setSpacing(25);
        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, outerPane, CLASS_SITE_PANE, ENABLED);
        leftPane.setSpacing(5);
        GridPane tasHeaderBox = ohBuilder.buildGridPane(OH_TAS_HEADER_PANE, leftPane, CLASS_SITE_PANE, ENABLED);
        ohBuilder.buildTextButton(OH_REMOVE_TA_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button removeButton = (Button) app.getGUIModule().getGUINode(OH_REMOVE_TA_BUTTON);
        removeButton.setText("-");
        Label tasLabel = ohBuilder.buildLabel(SiteGeneratorPropertyType.OH_TAS_HEADER_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        GridPane typeHeaderBox = ohBuilder.buildGridPane(OH_GRAD_UNDERGRAD_TAS_PANE, null, CLASS_OH_BOX, ENABLED);
        tasHeaderBox.setHgap(5);
        tasHeaderBox.add(removeButton, 0, 0);
        tasHeaderBox.add(tasLabel, 1, 0);
        tasHeaderBox.add(typeHeaderBox, 2, 0);
        ToggleGroup tg = new ToggleGroup();
        RadioButton all = ohBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, null, CLASS_SITE_RADIO_BUTTON, ENABLED, tg, true);
        RadioButton undergrad = ohBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, null, CLASS_SITE_RADIO_BUTTON, ENABLED, tg, false);
        RadioButton grad = ohBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, null, CLASS_SITE_RADIO_BUTTON, ENABLED, tg, false);
        typeHeaderBox.setHgap(15);
        typeHeaderBox.add(all, 0, 0);
        typeHeaderBox.add(undergrad, 1, 0);
        typeHeaderBox.add(grad, 2, 0);
        
        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        taTable.setMinHeight(150);
        taTable.setMaxHeight(150);
        
        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_SITE_PANE, ENABLED);
        taBox.setSpacing(15);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_SITE_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_SITE_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_SITE_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, outerPane, CLASS_SITE_PANE, ENABLED);
        rightPane.setSpacing(5);
        GridPane officeHoursHeaderBox = ohBuilder.buildGridPane(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_SITE_PANE, ENABLED);
        Label ohLabel = ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        HBox officeHoursTimePane = ohBuilder.buildHBox(OH_OFFICE_HOURS_TIME_PANE, null, CLASS_SITE_PANE, ENABLED);
        
        officeHoursHeaderBox.setHgap(150);
        officeHoursHeaderBox.add(ohLabel, 0, 0);
        officeHoursHeaderBox.add(officeHoursTimePane, 1, 0);
        officeHoursTimePane.setAlignment(Pos.CENTER);
        
        ohBuilder.buildLabel(OH_START_TIME_LABEL, officeHoursTimePane, CLASS_SITE_BOLD_LABEL, ENABLED);
        ArrayList<String> startTimes = new ArrayList<String>();
        startTimes.add("9:00am");
        startTimes.add("10:00am");
        startTimes.add("11:00am");
        startTimes.add("12:00pm");
        startTimes.add("1:00pm");
        startTimes.add("2:00pm");
        startTimes.add("3:00pm");
        startTimes.add("4:00pm");
        startTimes.add("5:00pm");
        startTimes.add("6:00pm");
        startTimes.add("7:00pm");
        startTimes.add("8:00pm");
        startTimes.add("9:00pm");
        props.addProperty(OH_START_TIME_DEFAULT, "9:00am");
        props.addPropertyOptionsList(OH_START_TIMES, startTimes);
        ohBuilder.buildComboBox(OH_START_COMBO_BOX, OH_START_TIMES, OH_START_TIME_DEFAULT, officeHoursTimePane, CLASS_SITE_COMBO_BOX, ENABLED);
        
        ohBuilder.buildLabel(OH_END_TIME_LABEL, officeHoursTimePane, CLASS_SITE_BOLD_LABEL, ENABLED);
        ArrayList<String> endTimes = new ArrayList<String>();
        endTimes.add("10:00am");
        endTimes.add("11:00am");
        endTimes.add("12:00pm");
        endTimes.add("1:00pm");
        endTimes.add("2:00pm");
        endTimes.add("3:00pm");
        endTimes.add("4:00pm");
        endTimes.add("5:00pm");
        endTimes.add("6:00pm");
        endTimes.add("7:00pm");
        endTimes.add("8:00pm");
        endTimes.add("9:00pm");
        endTimes.add("10:00pm");
        props.addProperty(OH_END_TIME_DEFAULT, "10:00am");
        props.addPropertyOptionsList(OH_END_TIMES, endTimes);
        ComboBox endCombo = ohBuilder.buildComboBox(OH_END_COMBO_BOX, OH_END_TIMES, OH_END_TIME_DEFAULT, officeHoursTimePane, CLASS_SITE_COMBO_BOX, ENABLED);
        endCombo.getSelectionModel().selectLast();
        
        officeHoursTimePane.setSpacing(15);
        
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        TableColumn startTimeColumn = ohBuilder.buildTableColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn endTimeColumn = ohBuilder.buildTableColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn mondayColumn = ohBuilder.buildTableColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn tuesdayColumn = ohBuilder.buildTableColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn wednesdayColumn = ohBuilder.buildTableColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn thursdayColumn = ohBuilder.buildTableColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn fridayColumn = ohBuilder.buildTableColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("endTime"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("friday"));
        for (int i = 0; i < officeHoursTable.getColumns().size(); i++) {
            ((TableColumn)officeHoursTable.getColumns().get(i)).prefWidthProperty().bind(officeHoursTable.widthProperty().multiply(1.0/7.0));
        }
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE RIGHT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);
        rightPane.prefHeightProperty().bind(ohPane.heightProperty());
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        /*VBox v = new VBox(leftPane, rightPane);
        v.setSpacing(0.4);
        v.setStyle(("-fx-background-color: #fcd0a4"));
        v.setPadding(new Insets(3, 2, 3, 2));
        ohPane = new BorderPane();
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) ohPane).setCenter(v);*/
    }
}
