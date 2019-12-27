/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.controllers;

import djf.modules.AppGUIModule;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SCH_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.SCH_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_END_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.SCH_LINK_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_START_DATE_PICKER;
import static sg.SiteGeneratorPropertyType.SCH_TITLE_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_TOPIC_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SCH_TYPE_COMBO;
import static sg.SiteGeneratorPropertyType.SCH_UPDATE_BUTTON_TEXT;
import sg.data.ScheduleItem;
import sg.data.SiteGeneratorData;
import sg.transactions.AddScheduleItem_Transaction;
import sg.transactions.ChangeEndDate_Transaction;
import sg.transactions.ChangeStartDate_Transaction;
import sg.transactions.EditScheduleItem_Transaction;
import sg.transactions.RemoveScheduleItem_Transaction;

/**
 *
 * @author brettweinger
 */
public class ScheduleController {
    SiteGeneratorApp app;
    AppGUIModule gui;
    PropertiesManager props;

    public ScheduleController(SiteGeneratorApp initApp) {
        app = initApp;
        gui = initApp.getGUIModule();
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void processAddScheduleItem() {
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField titleText = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topicText = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField linkText = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        String type = (String) typeCombo.getValue();
        int month = datePicker.getValue().getMonthValue();
        int day = datePicker.getValue().getDayOfMonth();
        int year = datePicker.getValue().getYear();
        String date = month + "/" + day + "/" + year;
        String title = titleText.getText();
        String topic = topicText.getText();
        String link = linkText.getText();
        ScheduleItem item = new ScheduleItem(type, date, title, topic, link);
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        AddScheduleItem_Transaction transaction = new AddScheduleItem_Transaction(gui, item, data);
        app.processTransaction(transaction);
        data.updateItems();
        data.processClearFields();
    }
    
    public void processRemoveScheduleItem() {
        TableView<ScheduleItem> itemsTable = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ScheduleItem item = itemsTable.getSelectionModel().getSelectedItem();
        if(item != null) {
            SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
            RemoveScheduleItem_Transaction transaction = new RemoveScheduleItem_Transaction(gui, item, data);
            app.processTransaction(transaction);
            data.updateItems();
            data.processClearFields();
        }
    }
    
    public void processFillFields() {
        TableView<ScheduleItem> itemsTable = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ScheduleItem item = itemsTable.getSelectionModel().getSelectedItem();
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField titleText = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topicText = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField linkText = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        //Parse date and get ints
        String date = item.getDate();
        String[] parsedDate = date.split("/");
        int month = Integer.parseInt(parsedDate[0]);
        int day = Integer.parseInt(parsedDate[1]);
        int year = Integer.parseInt(parsedDate[2]);
        typeCombo.setValue(item.getType());
        datePicker.setValue(LocalDate.of(year, month, day));
        titleText.setText(item.getTitle());
        topicText.setText(item.getTopic());
        linkText.setText(item.getLink());
        addButton.setText(props.getProperty(SCH_UPDATE_BUTTON_TEXT));
    }
    
    public void processUpdateScheduleItem() {
        TableView<ScheduleItem> itemsTable = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCH_TYPE_COMBO);
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        TextField titleText = (TextField) gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topicText = (TextField) gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField linkText = (TextField) gui.getGUINode(SCH_LINK_TEXT_FIELD);
        String type = (String) typeCombo.getValue();
        int month = datePicker.getValue().getMonthValue();
        int day = datePicker.getValue().getDayOfMonth();
        int year = datePicker.getValue().getYear();
        String date = month + "/" + day + "/" + year;
        String title = titleText.getText();
        String topic = topicText.getText();
        String link = linkText.getText();
        ScheduleItem oldItem = itemsTable.getSelectionModel().getSelectedItem();
        ScheduleItem newItem = new ScheduleItem(type, date, title, topic, link);
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        EditScheduleItem_Transaction transaction = new EditScheduleItem_Transaction(gui, oldItem, newItem, data);
        app.processTransaction(transaction);
        data.processClearFields();
    }
    
    public void processButtonEnable() {
        DatePicker datePicker = (DatePicker) gui.getGUINode(SCH_DATE_PICKER);
        DatePicker startPicker = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        DatePicker endPicker = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        LocalDate currentDate = datePicker.getValue();
        LocalDate startDate = startPicker.getValue();
        LocalDate endDate = endPicker.getValue();
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        if(currentDate == null) {
            addButton.setDisable(true); //can't add if no date is selected
        } else if(startDate == null && endDate == null) {
            addButton.setDisable(false); //enable if any date is available
        } else if((startDate == null && currentDate.isBefore(endDate)) || (startDate == null && currentDate.equals(endDate))) { //before end
            addButton.setDisable(false);
        } else if((endDate == null && currentDate.isAfter(startDate)) || (endDate == null && currentDate.equals(startDate)))  {//after start
            addButton.setDisable(false);
        } else if(startDate == null || endDate == null) {
            addButton.setDisable(true);
        } else if(currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) { //valid
            addButton.setDisable(false);
        } else if(currentDate.isAfter(startDate) && currentDate.equals(endDate) || currentDate.equals(startDate) && currentDate.isBefore(endDate)) {
            addButton.setDisable(false);
        } else if(currentDate.equals(startDate) && currentDate.equals(endDate)) {
            addButton.setDisable(false);
        } else {
            addButton.setDisable(true);
        }
    }
    
    public void updateStartDate() {
        DatePicker startPicker = (DatePicker) gui.getGUINode(SCH_START_DATE_PICKER);
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        ChangeStartDate_Transaction transaction = new ChangeStartDate_Transaction(startPicker, data.getStartDate(), startPicker.getValue(), data, app, addButton);
        app.processTransaction(transaction);
    }
    
    public void updateEndDate() {
        DatePicker endPicker = (DatePicker) gui.getGUINode(SCH_END_DATE_PICKER);
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        Button addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        ChangeEndDate_Transaction transaction = new ChangeEndDate_Transaction(endPicker, data.getEndDate(), endPicker.getValue(), data, app, addButton);
        app.processTransaction(transaction);
    }
}
