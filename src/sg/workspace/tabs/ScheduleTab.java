/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.tabs;

import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SCH_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.SCH_ADD_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_ADD_PANE;
import static sg.SiteGeneratorPropertyType.SCH_BUTTONS_PANE;
import static sg.SiteGeneratorPropertyType.SCH_CALENDAR_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_CALENDAR_PANE;
import static sg.SiteGeneratorPropertyType.SCH_CLEAR_BUTTON;
import static sg.SiteGeneratorPropertyType.SCH_DATE_COLUMN;
import static sg.SiteGeneratorPropertyType.SCH_DATE_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_DATE_PANE;
import static sg.SiteGeneratorPropertyType.SCH_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_END_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_FIELDS_PANE;
import static sg.SiteGeneratorPropertyType.SCH_FRIDAY_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_BUTTON;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_INFO;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_PANE;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.SCH_LINK_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_LINK_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_MONDAY_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_START_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_TITLE_COLUMN;
import static sg.SiteGeneratorPropertyType.SCH_TITLE_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_TITLE_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_TOPIC_COLUMN;
import static sg.SiteGeneratorPropertyType.SCH_TOPIC_LABEL;
import static sg.SiteGeneratorPropertyType.SCH_TOPIC_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_TYPE_COLUMN;
import static sg.SiteGeneratorPropertyType.SCH_TYPE_COMBO;
import static sg.SiteGeneratorPropertyType.SCH_TYPE_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SCH_TYPE_LABEL;
import sg.data.ScheduleItem;
import sg.data.TimeSlot.DayOfWeek;
import static sg.workspace.style.SGStyle.CLASS_OH_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_OH_TABLE_VIEW;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOLD_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_COMBO_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_DATE_PICKER;
import static sg.workspace.style.SGStyle.CLASS_SITE_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_PANE;
import static sg.workspace.style.SGStyle.CLASS_SITE_TEXT_FIELD;

/**
 *
 * @author brettweinger
 */
public class ScheduleTab {
    
    SiteGeneratorApp app;
    VBox schedulePane;

    public ScheduleTab(SiteGeneratorApp initApp) {
        app = initApp;
        schedulePane = new VBox();
        schedulePane.setSpacing(5);
        schedulePane.setStyle("-fx-background-color: #fcd0a4");
        schedulePane.setPadding(new Insets(3, 2, 3, 2));
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        //initControllers();

        // INIT FOOLPROOF DESIGN
        //initFoolproofDesign();

        // INIT DIALOGS
        //initDialogs();
    }

    public VBox getSchedulePane() {
        return schedulePane;
    }
    
    public void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        
        //START CONSTRUCTING THE UI
        VBox calendarPane = builder.buildVBox(SCH_CALENDAR_PANE, schedulePane, CLASS_SITE_BOX, ENABLED);
        calendarPane.setSpacing(5);
        builder.buildLabel(SCH_CALENDAR_LABEL, calendarPane, CLASS_SITE_BOLD_LABEL, ENABLED);
        GridPane datePane = builder.buildGridPane(SCH_DATE_PANE, calendarPane, CLASS_SITE_PANE, ENABLED);
        datePane.setHgap(10);
        Label mondayLabel = builder.buildLabel(SCH_MONDAY_LABEL, null, CLASS_SITE_LABEL, ENABLED);
        DatePicker startDatePicker = builder.buildDatePicker(SCH_START_DATE_PICKER, null, CLASS_SITE_DATE_PICKER, ENABLED);
        Label fridayLabel = builder.buildLabel(SCH_FRIDAY_LABEL, null, CLASS_SITE_LABEL, ENABLED);
        DatePicker endDatePicker = builder.buildDatePicker(SCH_END_DATE_PICKER, null, CLASS_SITE_DATE_PICKER, ENABLED);
        datePane.add(mondayLabel, 0, 0);
        datePane.add(startDatePicker, 1, 0);
        datePane.add(fridayLabel, 2, 0);
        datePane.add(endDatePicker, 3, 0);
        
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if(date != null && endDatePicker.getValue() != null) {
                    setDisable(empty || date.isAfter(endDatePicker.getValue()));
                }
            }
        });
        
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if(date != null && startDatePicker.getValue() != null) {
                    setDisable(empty || date.isBefore(startDatePicker.getValue()));
                }
            }
        });
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
        
        VBox itemsPane = builder.buildVBox(SCH_ITEMS_PANE, schedulePane, CLASS_SITE_BOX, ENABLED);
        itemsPane.setSpacing(5);
        GridPane itemsInfo = builder.buildGridPane(SCH_ITEMS_INFO, itemsPane, CLASS_SITE_PANE, ENABLED);
        itemsInfo.setHgap(5);
        Button itemsButton = builder.buildTextButton(SCH_ITEMS_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        itemsButton.setText("-");
        Label itemsLabel = builder.buildLabel(SCH_ITEMS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        itemsInfo.add(itemsButton, 0, 0);
        itemsInfo.add(itemsLabel, 1, 0);
       
        TableView<ScheduleItem> itemsTable = builder.buildTableView(SCH_ITEMS_TABLE_VIEW, itemsPane, CLASS_OH_TABLE_VIEW, ENABLED);
        TableColumn itemsTypeColumn = builder.buildTableColumn(SCH_TYPE_COLUMN, itemsTable, CLASS_OH_COLUMN);
        TableColumn itemsDateColumn = builder.buildTableColumn(SCH_DATE_COLUMN, itemsTable, CLASS_OH_COLUMN);
        TableColumn itemsTitleColumn = builder.buildTableColumn(SCH_TITLE_COLUMN, itemsTable, CLASS_OH_COLUMN);
        TableColumn itemsTopicColumn = builder.buildTableColumn(SCH_TOPIC_COLUMN, itemsTable, CLASS_OH_COLUMN);
        itemsTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        itemsDateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        itemsTitleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        itemsTopicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        itemsTypeColumn.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1.0 / 4.0));
        itemsDateColumn.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1.0 / 4.0));
        itemsTitleColumn.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1.0 / 4.0));
        itemsTopicColumn.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1.0 / 4.0));
        itemsTable.setMinHeight(230);
        itemsTypeColumn.setSortable(false);
        itemsDateColumn.setSortable(false);
        itemsTitleColumn.setSortable(false);
        itemsTopicColumn.setSortable(false);
        
        VBox addPane = builder.buildVBox(SCH_ADD_PANE, schedulePane, CLASS_SITE_BOX, ENABLED);
        addPane.setSpacing(30);
        GridPane fieldsPane = builder.buildGridPane(SCH_FIELDS_PANE, addPane, CLASS_SITE_PANE, ENABLED);
        Label addLabel = builder.buildLabel(SCH_ADD_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        Label typeLabel = builder.buildLabel(SCH_TYPE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        addLabel.setPadding(new Insets(10, 0, 0, 0));
        
        ArrayList<String> options = new ArrayList<String>();
        options.add("Holiday");
        options.add("Homework");
        options.add("Lecture");
        options.add("Recitation");
        options.add("Reference");
        props.addPropertyOptionsList(SCH_TYPE_COMBO_OPTIONS, options);
        ComboBox typeCombo = builder.buildComboBox(SCH_TYPE_COMBO, SCH_TYPE_COMBO_OPTIONS, "", null, CLASS_SITE_COMBO_BOX, ENABLED);
        typeCombo.setEditable(true);
        
        Label dateLabel = builder.buildLabel(SCH_DATE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        DatePicker datePicker = builder.buildDatePicker(SCH_DATE_PICKER, null, CLASS_SITE_DATE_PICKER, ENABLED);
        Label titleLabel = builder.buildLabel(SCH_TITLE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField titleTextField = builder.buildTextField(SCH_TITLE_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        Label topicLabel = builder.buildLabel(SCH_TOPIC_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField topicTextField = builder.buildTextField(SCH_TOPIC_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        Label linkLabel = builder.buildLabel(SCH_LINK_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField linkTextField = builder.buildTextField(SCH_LINK_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        
        fieldsPane.setHgap(20);
        fieldsPane.setVgap(20);
        fieldsPane.add(addLabel, 0, 0);
        fieldsPane.add(typeLabel, 0, 1);
        fieldsPane.add(typeCombo, 1, 1);
        fieldsPane.add(dateLabel, 0, 2);
        fieldsPane.add(datePicker, 1, 2);
        fieldsPane.add(titleLabel, 0, 3);
        fieldsPane.add(titleTextField, 1, 3);
        fieldsPane.add(topicLabel, 0, 4);
        fieldsPane.add(topicTextField, 1, 4);
        fieldsPane.add(linkLabel, 0, 5);
        fieldsPane.add(linkTextField, 1, 5);
        
        HBox buttonsPane = builder.buildHBox(SCH_BUTTONS_PANE, addPane, CLASS_SITE_PANE, ENABLED);
        buttonsPane.setSpacing(35);
        builder.buildTextButton(SCH_ADD_BUTTON, buttonsPane, CLASS_SITE_BUTTON, !ENABLED);
        builder.buildTextButton(SCH_CLEAR_BUTTON, buttonsPane, CLASS_SITE_BUTTON, ENABLED);
        itemsPane.prefHeightProperty().bind(schedulePane.heightProperty());
        addPane.prefHeightProperty().bind(schedulePane.heightProperty());
    }
}