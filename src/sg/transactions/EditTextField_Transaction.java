/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;

/**
 *
 * @author brettweinger
 */
public class EditTextField_Transaction implements jTPS_Transaction {
    TextField textField;
    String startText;
    String endText;
    SiteGeneratorApp app;
    
    public EditTextField_Transaction(TextField initField, String initStart, String initEnd, SiteGeneratorApp initApp) {
        textField = initField;
        startText = initStart;
        endText = initEnd;
        app = initApp;
    }
    
    public TextField getSource() {
        return textField;
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
        textField.setText(endText);
        app.getWorkspaceComponent().process(true);
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().process(false);
        textField.setText(startText);
        app.getWorkspaceComponent().process(true);
    }
}
