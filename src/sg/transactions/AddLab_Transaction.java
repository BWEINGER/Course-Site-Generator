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
public class AddLab_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    Lab lab;
    TableView<Lab> labs;
    
    public AddLab_Transaction(AppGUIModule initGUI, Lab initLab) {
        gui = initGUI;
        lab = initLab;
        labs = (TableView<Lab>) gui.getGUINode(MT_LABS_TABLE_VIEW);
    }
    
    @Override
    public void doTransaction() {
        labs.getItems().add(lab);
        Collections.sort(labs.getItems());
    }
    
    @Override
    public void undoTransaction() {
        labs.getItems().remove(lab);
        Collections.sort(labs.getItems());
    }
}
