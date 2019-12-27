/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SCH_UPDATE_BUTTON_TEXT;
import sg.data.SiteGeneratorData;

/**
 *
 * @author brettweinger
 */
public class ChangeStartDate_Transaction implements jTPS_Transaction {
    LocalDate oldDate;
    LocalDate newDate;
    DatePicker datePicker;
    SiteGeneratorData data;
    SiteGeneratorApp app;
    PropertiesManager props;
    Button addButton;
    
    public ChangeStartDate_Transaction(DatePicker initPicker, LocalDate initOld, LocalDate initNew, SiteGeneratorData initData, SiteGeneratorApp initApp, Button initButton) {
        datePicker = initPicker;
        oldDate = initOld;
        newDate = initNew;
        data = initData;
        app = initApp;
        addButton = initButton;
        props = PropertiesManager.getPropertiesManager();
    }
    
    @Override
    public void doTransaction() {
        app.getWorkspaceComponent().changeDate(false);
        datePicker.setValue(newDate);
        data.setStartDate(newDate);
        data.updateItems();
        app.getWorkspaceComponent().changeDate(true);
        if(addButton.getText().equals(props.getProperty(SCH_UPDATE_BUTTON_TEXT))) {
            data.processClearFields();
        }
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().changeDate(false);
        datePicker.setValue(oldDate);
        data.setStartDate(oldDate);
        data.updateItems();
        app.getWorkspaceComponent().changeDate(true);
        if(addButton.getText().equals(props.getProperty(SCH_UPDATE_BUTTON_TEXT))) {
            data.processClearFields();
        }
    }
}
