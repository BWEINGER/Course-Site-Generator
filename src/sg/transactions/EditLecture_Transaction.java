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
import static sg.SiteGeneratorPropertyType.MT_LECTURES_TABLE_VIEW;
import sg.data.Lecture;

/**
 *
 * @author brettweinger
 */
public class EditLecture_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    Lecture oldLecture;
    Lecture newLecture;
    TableView<Lecture> lectures;
    
    public EditLecture_Transaction(AppGUIModule initGUI, Lecture initOld, Lecture initNew) {
        gui = initGUI;
        oldLecture = initOld;
        newLecture = initNew;
        lectures = (TableView<Lecture>) gui.getGUINode(MT_LECTURES_TABLE_VIEW);
    }
    
    @Override
    public void doTransaction() {
        lectures.getItems().remove(oldLecture);
        lectures.getItems().add(newLecture);
        Collections.sort(lectures.getItems());
    }
    
    @Override
    public void undoTransaction() {
        lectures.getItems().remove(newLecture);
        lectures.getItems().add(oldLecture);
        Collections.sort(lectures.getItems());
    }
}