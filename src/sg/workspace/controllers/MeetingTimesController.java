/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.controllers;

import djf.modules.AppGUIModule;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.*;
import sg.data.Lab;
import sg.data.Lecture;
import sg.data.Recitation;
import sg.transactions.AddLab_Transaction;
import sg.transactions.AddLecture_Transaction;
import sg.transactions.AddRecitation_Transaction;
import sg.transactions.EditLab_Transaction;
import sg.transactions.EditLecture_Transaction;
import sg.transactions.EditRecitation_Transaction;
import sg.transactions.RemoveLab_Transaction;
import sg.transactions.RemoveLecture_Transaction;
import sg.transactions.RemoveRecitation_Transaction;

/**
 *
 * @author brettweinger
 */
public class MeetingTimesController {
    SiteGeneratorApp app;
    AppGUIModule gui;
    PropertiesManager props;

    public MeetingTimesController(SiteGeneratorApp initApp) {
        app = initApp;
        gui = initApp.getGUIModule();
        props = PropertiesManager.getPropertiesManager();
    }
    
    //LECTURES
    public void processAddLecture() {
        Lecture lecture = new Lecture("?", "?", "?", "?");
        AddLecture_Transaction transaction = new AddLecture_Transaction(gui, lecture);
        app.processTransaction(transaction);
    }
    
    public void processRemoveLecture() {
        TableView<Lecture> lectureTable = (TableView)gui.getGUINode(MT_LECTURES_TABLE_VIEW);
        Lecture lecture = lectureTable.getSelectionModel().getSelectedItem();
        if(lecture != null) {
            RemoveLecture_Transaction transaction = new RemoveLecture_Transaction(gui, lecture);
            app.processTransaction(transaction);
        }
    }
    
    //LECTURE EDIT METHODS
    public void processEditLectureSection(TableColumn.CellEditEvent<Lecture, String> t) {
        Lecture oldLecture = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lecture newLecture = new Lecture (t.getNewValue(), oldLecture.getDays(), oldLecture.getTime(), oldLecture.getRoom());
        EditLecture_Transaction transaction = new EditLecture_Transaction(gui, oldLecture, newLecture);
        app.processTransaction(transaction);
    }
    
    public void processEditLectureDay(TableColumn.CellEditEvent<Lecture, String> t) {
        Lecture oldLecture = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lecture newLecture = new Lecture (oldLecture.getSection(), t.getNewValue(), oldLecture.getTime(), oldLecture.getRoom());
        EditLecture_Transaction transaction = new EditLecture_Transaction(gui, oldLecture, newLecture);
        app.processTransaction(transaction);
    }
    
    public void processEditLectureTime(TableColumn.CellEditEvent<Lecture, String> t) {
        Lecture oldLecture = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lecture newLecture = new Lecture (oldLecture.getSection(), oldLecture.getDays(), t.getNewValue(), oldLecture.getRoom());
        EditLecture_Transaction transaction = new EditLecture_Transaction(gui, oldLecture, newLecture);
        app.processTransaction(transaction);
    }
    
    public void processEditLectureRoom(TableColumn.CellEditEvent<Lecture, String> t) {
        Lecture oldLecture = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lecture newLecture = new Lecture (oldLecture.getSection(), oldLecture.getDays(), oldLecture.getTime(), t.getNewValue());
        EditLecture_Transaction transaction = new EditLecture_Transaction(gui, oldLecture, newLecture);
        app.processTransaction(transaction);
    }
    
    //RECITATIONS
    public void processAddRecitation() {
        Recitation recitation = new Recitation("?", "?", "?", "?", "?");
        AddRecitation_Transaction transaction = new AddRecitation_Transaction(gui, recitation);
        app.processTransaction(transaction);
    }
    
    public void processRemoveRecitation() {
        TableView<Recitation> recitationTable = (TableView)gui.getGUINode(MT_RECITATIONS_TABLE_VIEW);
        Recitation recitation = recitationTable.getSelectionModel().getSelectedItem();
        if(recitation != null) {
            RemoveRecitation_Transaction transaction = new RemoveRecitation_Transaction(gui, recitation);
            app.processTransaction(transaction);
        }
    }
    
    //RECITATION EDIT METHODS
    public void processEditRecitationSection(TableColumn.CellEditEvent<Recitation, String> t) {
        Recitation oldRecitation = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Recitation newRecitation = new Recitation(t.getNewValue(), oldRecitation.getDaysTime(), oldRecitation.getRoom(), oldRecitation.getTa1(), oldRecitation.getTa2());
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(gui, oldRecitation, newRecitation);
        app.processTransaction(transaction);
    }
    
    public void processEditRecitationDay(TableColumn.CellEditEvent<Recitation, String> t) {
        Recitation oldRecitation = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Recitation newRecitation = new Recitation(oldRecitation.getSection(), t.getNewValue(), oldRecitation.getRoom(), oldRecitation.getTa1(), oldRecitation.getTa2());
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(gui, oldRecitation, newRecitation);
        app.processTransaction(transaction);
    }
    
    public void processEditRecitationRoom(TableColumn.CellEditEvent<Recitation, String> t) {
        Recitation oldRecitation = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Recitation newRecitation = new Recitation(oldRecitation.getSection(), oldRecitation.getDaysTime(), t.getNewValue(), oldRecitation.getTa1(), oldRecitation.getTa2());
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(gui, oldRecitation, newRecitation);
        app.processTransaction(transaction);
    }
    
    public void processEditRecitationTA1(TableColumn.CellEditEvent<Recitation, String> t) {
        Recitation oldRecitation = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Recitation newRecitation = new Recitation(oldRecitation.getSection(), oldRecitation.getDaysTime(), oldRecitation.getRoom(), t.getNewValue(), oldRecitation.getTa2());
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(gui, oldRecitation, newRecitation);
        app.processTransaction(transaction);
    }
    
    public void processEditRecitationTA2(TableColumn.CellEditEvent<Recitation, String> t) {
        Recitation oldRecitation = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Recitation newRecitation = new Recitation(oldRecitation.getSection(), oldRecitation.getDaysTime(), oldRecitation.getRoom(), oldRecitation.getTa1(), t.getNewValue());
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(gui, oldRecitation, newRecitation);
        app.processTransaction(transaction);
    }
    
    //LABS
    public void processAddLab() {
        Lab lab = new Lab("?", "?", "?", "?", "?");
        AddLab_Transaction transaction = new AddLab_Transaction(gui, lab);
        app.processTransaction(transaction);
    }
    
    public void processRemoveLab() {
        TableView<Lab> labTable = (TableView)gui.getGUINode(MT_LABS_TABLE_VIEW);
        Lab lab = labTable.getSelectionModel().getSelectedItem();
        if(lab != null) {
            RemoveLab_Transaction transaction = new RemoveLab_Transaction(gui, lab);
            app.processTransaction(transaction);
        }
    }
    
    //RECITATION EDIT METHODS
    public void processEditLabSection(TableColumn.CellEditEvent<Lab, String> t) {
        Lab oldLab = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lab newLab = new Lab(t.getNewValue(), oldLab.getDaysTime(), oldLab.getRoom(), oldLab.getTa1(), oldLab.getTa2());
        EditLab_Transaction transaction = new EditLab_Transaction(gui, oldLab, newLab);
        app.processTransaction(transaction);
    }
    
    public void processEditLabDay(TableColumn.CellEditEvent<Lab, String> t) {
        Lab oldLab = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lab newLab = new Lab(oldLab.getSection(), t.getNewValue(), oldLab.getRoom(), oldLab.getTa1(), oldLab.getTa2());
        EditLab_Transaction transaction = new EditLab_Transaction(gui, oldLab, newLab);
        app.processTransaction(transaction);
    }
    
    public void processEditLabRoom(TableColumn.CellEditEvent<Lab, String> t) {
        Lab oldLab = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lab newLab = new Lab(oldLab.getSection(), oldLab.getDaysTime(), t.getNewValue(), oldLab.getTa1(), oldLab.getTa2());
        EditLab_Transaction transaction = new EditLab_Transaction(gui, oldLab, newLab);
        app.processTransaction(transaction);
    }
    
    public void processEditLabTA1(TableColumn.CellEditEvent<Lab, String> t) {
        Lab oldLab = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lab newLab = new Lab(oldLab.getSection(), oldLab.getDaysTime(), oldLab.getRoom(), t.getNewValue(), oldLab.getTa2());
        EditLab_Transaction transaction = new EditLab_Transaction(gui, oldLab, newLab);
        app.processTransaction(transaction);
    }
    
    public void processEditLabTA2(TableColumn.CellEditEvent<Lab, String> t) {
        Lab oldLab = t.getTableView().getItems().get(t.getTablePosition().getRow());
        Lab newLab = new Lab(oldLab.getSection(), oldLab.getDaysTime(), oldLab.getRoom(), oldLab.getTa1(), t.getNewValue());
        EditLab_Transaction transaction = new EditLab_Transaction(gui, oldLab, newLab);
        app.processTransaction(transaction);
    }
}