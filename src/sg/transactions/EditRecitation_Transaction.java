/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import djf.modules.AppGUIModule;
import java.util.Collections;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_TABLE_VIEW;
import sg.data.Recitation;

/**
 *
 * @author brettweinger
 */
public class EditRecitation_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    Recitation oldRecitation;
    Recitation newRecitation;
    TableView<Recitation> recitations;
    
    public EditRecitation_Transaction(AppGUIModule initGUI, Recitation initOld, Recitation initNew) {
        gui = initGUI;
        oldRecitation = initOld;
        newRecitation = initNew;
        recitations = (TableView<Recitation>) gui.getGUINode(MT_RECITATIONS_TABLE_VIEW);
    }
    
    @Override
    public void doTransaction() {
        recitations.getItems().remove(oldRecitation);
        recitations.getItems().add(newRecitation);
        Collections.sort(recitations.getItems());
    }
    
    @Override
    public void undoTransaction() {
        recitations.getItems().remove(newRecitation);
        recitations.getItems().add(oldRecitation);
        Collections.sort(recitations.getItems());
    }
}
