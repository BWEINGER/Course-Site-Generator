package sg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.OH_EMAIL_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.OH_FOOLPROOF_SETTINGS;
import static sg.SiteGeneratorPropertyType.OH_NAME_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.OH_NO_TA_SELECTED_CONTENT;
import static sg.SiteGeneratorPropertyType.OH_NO_TA_SELECTED_TITLE;
import static sg.SiteGeneratorPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.OH_TAS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.OH_TA_EDIT_DIALOG;
import sg.data.SiteGeneratorData;
import sg.data.TAType;
import sg.data.TeachingAssistantPrototype;
import sg.data.TimeSlot;
import sg.data.TimeSlot.DayOfWeek;
import sg.transactions.AddTA_Transaction;
import sg.transactions.ChangeEndTime_Transaction;
import sg.transactions.ChangeStartTime_Transaction;
import sg.transactions.EditTA_Transaction;
import sg.transactions.RemoveTA_Transaction;
import sg.transactions.ToggleOfficeHours_Transaction;
import sg.workspace.dialogs.TADialog;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursController {

    SiteGeneratorApp app;

    public OfficeHoursController(SiteGeneratorApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
        data.updateTAs();
    }

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                } else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog) app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType(), data);
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }

    /*public void processUpdateTimes() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        ComboBox startBox = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        ComboBox endBox = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        String startValue = (String) startBox.getValue();
        String endValue = (String) endBox.getValue();
        data.changeHours(startValue, endValue);
    }

    public void processUpdateEndTimes() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        ComboBox endBox = (ComboBox) gui.getGUINode(OH_END_COMBO_BOX);
        String savedEndTime = (String) endBox.getValue();
        ObservableList<String> endTimes = endBox.getItems();
        int startTime = data.getStartHour();
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
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        ComboBox startBox = (ComboBox) gui.getGUINode(OH_START_COMBO_BOX);
        String savedStartTime = (String) startBox.getValue();
        ObservableList<String> startTimes = startBox.getItems();
        int endTime = data.getEndHour();
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
    }*/
    
    public void processRemoveTA() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype cutTA = data.getSelectedTA();
            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(cutTA);
            RemoveTA_Transaction transaction = new RemoveTA_Transaction((SiteGeneratorApp)app, cutTA, officeHours);
            app.processTransaction(transaction);
        }
    }
    
    public void processStartCombo(ComboBox combo) {
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "9:00am";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof ChangeStartTime_Transaction) {
                ChangeStartTime_Transaction transaction = (ChangeStartTime_Transaction) t;
                initString = transaction.getNewTime();
                break;
            }
        }
        ChangeStartTime_Transaction transaction = new ChangeStartTime_Transaction(combo, initString, (String)combo.getValue(), app); 
        app.processTransaction(transaction);
    }
    
    public void processEndCombo(ComboBox combo) {
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "10:00pm";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof ChangeEndTime_Transaction) {
                ChangeEndTime_Transaction transaction = (ChangeEndTime_Transaction) t;
                initString = transaction.getNewTime();
                break;
            }
        }
        ChangeEndTime_Transaction transaction = new ChangeEndTime_Transaction(combo, initString, (String)combo.getValue(), app); 
        app.processTransaction(transaction);
    }
}
