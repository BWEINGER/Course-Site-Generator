/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;

/**
 *
 * @author brettweinger
 */
public class ComboBox_Transaction implements jTPS_Transaction {
    ComboBox combo;
    String startText;
    String endText;
    SiteGeneratorApp app;
    
    public ComboBox_Transaction(ComboBox initCombo, String initStart, String initEnd, SiteGeneratorApp initApp) {
        combo = initCombo;
        startText = initStart;
        endText = initEnd;
        app = initApp;
    }
    
    public ComboBox getSource() {
        return combo;
    }
    
    public String getStartText() {
        return startText;
    }
    
    public String getEndText() {
        return endText;
    }
    
    @Override
    public void doTransaction() {
        app.getWorkspaceComponent().process(false);
        combo.setValue(endText);
        app.getWorkspaceComponent().process(true);
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().process(false);
        combo.setValue(startText);
        app.getWorkspaceComponent().process(true);
    }
}

