/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;

/**
 *
 * @author brettweinger
 */
public class EditTextArea_Transaction implements jTPS_Transaction {
    TextArea textArea;
    String startText;
    String endText;
    SiteGeneratorApp app;
    
    public EditTextArea_Transaction(TextArea initArea, String initStart, String initEnd, SiteGeneratorApp initApp) {
        textArea = initArea;
        startText = initStart;
        endText = initEnd;
        app = initApp;
    }
    
    public TextArea getSource() {
        return textArea;
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
        textArea.setText(endText);
        app.getWorkspaceComponent().process(true);
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().process(false);
        textArea.setText(startText);
        app.getWorkspaceComponent().process(true);
    }
}
