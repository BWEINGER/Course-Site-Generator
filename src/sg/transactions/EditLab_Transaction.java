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
import static sg.SiteGeneratorPropertyType.MT_LABS_TABLE_VIEW;
import sg.data.Lab;

/**
 *
 * @author brettweinger
 */
public class EditLab_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    Lab oldLab;
    Lab newLab;
    TableView<Lab> labs;
    
    public EditLab_Transaction(AppGUIModule initGUI, Lab initOld, Lab initNew) {
        gui = initGUI;
        oldLab = initOld;
        newLab = initNew;
        labs = (TableView<Lab>) gui.getGUINode(MT_LABS_TABLE_VIEW);
    }
    
    @Override
    public void doTransaction() {
        labs.getItems().remove(oldLab);
        labs.getItems().add(newLab);
        Collections.sort(labs.getItems());
    }
    
    @Override
    public void undoTransaction() {
        labs.getItems().remove(newLab);
        labs.getItems().add(oldLab);
        Collections.sort(labs.getItems());
    }
}
