/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;
import sg.data.SiteGeneratorData;

/**
 *
 * @author brettweinger
 */
public class ChangeEndTime_Transaction implements jTPS_Transaction {
    ComboBox combo;
    String oldTime;
    String newTime;
    SiteGeneratorApp app;
    
    public ChangeEndTime_Transaction(ComboBox initCombo, String initOld, String initNew, SiteGeneratorApp initApp) {
        combo = initCombo;
        oldTime = initOld;
        newTime = initNew;
        app = initApp;
    }
    
    public String getOldTime() {
        return oldTime;
    }
    
    public String getNewTime() {
        return newTime;
    }
    
    @Override
    public void doTransaction() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        app.getWorkspaceComponent().process(false);
        combo.setValue(newTime);
        data.processUpdateTimes();
        data.processUpdateStartTimes();
        app.getWorkspaceComponent().process(true);
        app.getWorkspaceComponent().changeCombo(true);
    }
    
    @Override
    public void undoTransaction() {
        SiteGeneratorData data = (SiteGeneratorData) app.getDataComponent();
        app.getWorkspaceComponent().process(false);
        combo.setValue(oldTime);
        data.processUpdateTimes();
        data.processUpdateStartTimes();
        app.getWorkspaceComponent().process(true);
        app.getWorkspaceComponent().changeCombo(true);
    }
}
