/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.control.CheckBox;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;

/**
 *
 * @author brettweinger
 */
public class CheckBox_Transaction implements jTPS_Transaction {
    CheckBox box;
    boolean checked;
    SiteGeneratorApp app;
    
    public CheckBox_Transaction(CheckBox initBox, boolean initChecked, SiteGeneratorApp initApp) {
        box = initBox;
        checked = initChecked;
        app = initApp;
    }
    
    @Override
    public void doTransaction() {
        app.getWorkspaceComponent().process(false);
        box.setSelected(checked);
        app.getWorkspaceComponent().process(true);
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().process(false);
        box.setSelected(!checked);
        app.getWorkspaceComponent().process(true);
    }
}
