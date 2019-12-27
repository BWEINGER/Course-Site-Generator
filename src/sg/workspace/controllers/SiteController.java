/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.controllers;

import djf.modules.AppGUIModule;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SITE_EXPORT_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_FAVICON;
import static sg.SiteGeneratorPropertyType.SITE_LEFT_FOOTER;
import static sg.SiteGeneratorPropertyType.SITE_NAVBAR;
import static sg.SiteGeneratorPropertyType.SITE_NUMBER_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_RIGHT_FOOTER;
import static sg.SiteGeneratorPropertyType.SITE_SEMESTER_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_SUBJECT_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_YEAR_COMBO;
import sg.transactions.CheckBox_Transaction;
import sg.transactions.ComboBox_Transaction;
import sg.transactions.EditTextArea_Transaction;
import sg.transactions.EditTextField_Transaction;
import sg.transactions.Image_Transaction;

/**
 *
 * @author brettweinger
 */
public class SiteController {
    SiteGeneratorApp app;
    AppGUIModule gui;
    PropertiesManager props;

    public SiteController(SiteGeneratorApp initApp) {
        app = initApp;
        gui = initApp.getGUIModule();
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void processUpdateDirectory() {
        ComboBox subjectCombo = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBO);
        ComboBox numberCombo = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBO);
        ComboBox semesterCombo = (ComboBox) gui.getGUINode(SITE_SEMESTER_COMBO);
        ComboBox yearCombo = (ComboBox) gui.getGUINode(SITE_YEAR_COMBO);
        String exportDir = ".\\export\\";
        if(subjectCombo.getValue() != null) {
            if(!subjectCombo.getValue().equals("")) {
                exportDir += subjectCombo.getValue();
            }
        }
        if(numberCombo.getValue() != null) {
            if(!numberCombo.getValue().equals("")) {
                exportDir += "_" + numberCombo.getValue();
            }
        }
        if(semesterCombo.getValue() != null) {
            if(!semesterCombo.getValue().equals("")) {
                exportDir += "_" + semesterCombo.getValue();
            }
        }
        if(yearCombo.getValue() != null) {
            if(!yearCombo.getValue().equals("")) {
                exportDir += "_" + yearCombo.getValue();
            }
        }
        if(exportDir.equals(".\\export\\")) { //empty
            exportDir = "";
        } else {
            if(exportDir.contains(".\\export\\_")) {
                exportDir = exportDir.substring(0, 9) + exportDir.substring(10);
            }
            exportDir += "\\public_html";
        }
        Label exportDirLabel = (Label) gui.getGUINode(SITE_EXPORT_LABEL);
        String newExportDir = exportDir.replaceAll(" ", "_");
        exportDirLabel.setText(newExportDir);
    }
    
    public void processUpdateFavicon() {
        ImageView faviconIV = (ImageView) gui.getGUINode(SITE_FAVICON);
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG); 
        File imgFile = chooser.showOpenDialog(null);
        Image image;
        if(imgFile != null) {
            image = new Image(imgFile.toURI().toString());
        } else {
            return; //imgFile is null - no change/transaction needs to be made
        }
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        Image initImg = new Image("https://pbs.twimg.com/profile_images/1054070756848607233/wHfu_1gp_400x400.jpg");
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof Image_Transaction) {
                Image_Transaction transaction = (Image_Transaction) t;
                if(transaction.getSource() == faviconIV) {
                    initImg = transaction.getNew();
                    break;
                }
            }
        }
        Image_Transaction transaction = new Image_Transaction(faviconIV, initImg, image, app); 
        app.processTransaction(transaction);
    }
    
    public void processUpdateNavbar() {
        ImageView navbarIV = (ImageView) gui.getGUINode(SITE_NAVBAR);
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG); 
     
        File imgFile = chooser.showOpenDialog(null);
        Image image;
        if(imgFile != null) {
            image = new Image(imgFile.toURI().toString());
        } else {
            return;
        }
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        Image initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUDarkRedShieldLogo.png");
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof Image_Transaction) {
                Image_Transaction transaction = (Image_Transaction) t;
                if(transaction.getSource() == navbarIV) {
                    initImg = transaction.getNew();
                    break;
                }
            }
        }
        Image_Transaction transaction = new Image_Transaction(navbarIV, initImg, image, app); 
        app.processTransaction(transaction);
    }
    
    public void processUpdateLeftFooter() {
        ImageView leftFooterIV = (ImageView) gui.getGUINode(SITE_LEFT_FOOTER);
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG); 
     
        File imgFile = chooser.showOpenDialog(null);
        Image image;
        if(imgFile != null) {
            image = new Image(imgFile.toURI().toString());
        } else {
            return;
        }
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        Image initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUWhiteShieldLogo.jpg");
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof Image_Transaction) {
                Image_Transaction transaction = (Image_Transaction) t;
                if(transaction.getSource() == leftFooterIV) {
                    initImg = transaction.getNew();
                    break;
                }
            }
        }
        Image_Transaction transaction = new Image_Transaction(leftFooterIV, initImg, image, app); 
        app.processTransaction(transaction);
    }
    
    public void processUpdateRightFooter() {
        ImageView rightFooterIV = (ImageView) gui.getGUINode(SITE_RIGHT_FOOTER);
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG); 
     
        File imgFile = chooser.showOpenDialog(null);
        Image image;
        if(imgFile != null) {
            image = new Image(imgFile.toURI().toString());
        } else {
            return;
        }
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        Image initImg = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUCSLogo.png");
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof Image_Transaction) {
                Image_Transaction transaction = (Image_Transaction) t;
                if(transaction.getSource() == rightFooterIV) {
                    initImg = transaction.getNew();
                    break;
                }
            }
        }
        Image_Transaction transaction = new Image_Transaction(rightFooterIV, initImg, image, app); 
        app.processTransaction(transaction);
    }
    
    public void processAddSubject() {
        ComboBox subjectCombo = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBO);
        ObservableList<String> list = FXCollections.observableArrayList();
        ObservableList<String> ops = subjectCombo.getItems();
        String op = (String) subjectCombo.getValue();
        if(!ops.contains(op)) {
            ops.add(op);
        }
        for(int i = 0; i < ops.size(); i++) {
            list.add(ops.get(i));
        }
        subjectCombo.setItems(list);
        
        //NOW SAVE THE OPTION
        try {
        File f = new File("app_data/recent/SavedSubjects.txt");
            FileWriter fw = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fw);
            for(int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }
            pw.close();
        } catch(IOException e) {
        }
        Collections.sort(subjectCombo.getItems());
    }
    
    public void processAddNumber() {
        ComboBox numberCombo = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBO);
        ObservableList<String> list = FXCollections.observableArrayList();
        ObservableList<String> ops = numberCombo.getItems();
        String op = (String) numberCombo.getValue();
        if(!ops.contains(op)) {
            ops.add(op);
        }
        for(int i = 0; i < ops.size(); i++) {
            list.add(ops.get(i));
        }
        numberCombo.setItems(list);
        
        //NOW SAVE THE OPTION
        try {
        File f = new File("app_data/recent/SavedNumbers.txt");
            FileWriter fw = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fw);
            for(int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }
            pw.close();
        } catch(IOException e) {
        }
        Collections.sort(numberCombo.getItems());
    }
    
    public void processTextChange(TextField textField) {
        //Check to see if a transaction with the same is on the top of the stack
        jTPS_Transaction mostRecent = app.getTPS().peek();
        if(mostRecent instanceof EditTextField_Transaction) { //A text field was edited last
            EditTextField_Transaction transaction = (EditTextField_Transaction) mostRecent;
            if(transaction.getSource() == textField) { //identical source - remove it
                String initText = transaction.getStartText();
                EditTextField_Transaction newTransaction = new EditTextField_Transaction(textField, initText, textField.getText(), app);
                app.getTPS().pop();
                app.processTransaction(newTransaction);
                return;
            }
        } //otherwise, make a new transaction and process it
        //but what if the transaction isn't at the top of the stack?
        //the new transaction's start text needs to be the final instance of that transaction's end text
        //if no transaction with that event source is found, use "" to indicate an empty field
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof EditTextField_Transaction) {
                EditTextField_Transaction transaction = (EditTextField_Transaction) t;
                if(transaction.getSource() == textField) {
                    initString = transaction.getEndText();
                    break;
                }
            }
        }
        EditTextField_Transaction transaction = new EditTextField_Transaction(textField, initString, textField.getText(), app); 
        app.processTransaction(transaction);
   }
    
    public void processTextAreaChange(TextArea textArea) {
        //Check to see if a transaction with the same is on the top of the stack
        jTPS_Transaction mostRecent = app.getTPS().peek();
        if(mostRecent instanceof EditTextArea_Transaction) { //A text field was edited last
            EditTextArea_Transaction transaction = (EditTextArea_Transaction) mostRecent;
            if(transaction.getSource() == textArea) { //identical source - remove it
                String initText = transaction.getStartText();
                EditTextArea_Transaction newTransaction = new EditTextArea_Transaction(textArea, initText, textArea.getText(), app);
                app.getTPS().pop();
                app.processTransaction(newTransaction);
                return;
            }
        }
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof EditTextArea_Transaction) {
                EditTextArea_Transaction transaction = (EditTextArea_Transaction) t;
                if(transaction.getSource() == textArea) {
                    initString = transaction.getEndText();
                    break;
                }
            }
        }
        EditTextArea_Transaction transaction = new EditTextArea_Transaction(textArea, initString, textArea.getText(), app); 
        app.processTransaction(transaction);
   }
    
    public void processComboBoxChange(ComboBox combo) {
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof ComboBox_Transaction) {
                ComboBox_Transaction transaction = (ComboBox_Transaction) t;
                if(transaction.getSource() == combo) {
                    initString = transaction.getEndText();
                    break;
                }
            } else if(t instanceof EditTextField_Transaction) {
                EditTextField_Transaction transaction = (EditTextField_Transaction) t;
                if(transaction.getSource() == combo.getEditor()) {
                    initString = transaction.getEndText();
                    break;
                }
            }
        }
        ComboBox_Transaction transaction = new ComboBox_Transaction(combo, initString, (String)combo.getValue(), app); 
        app.processTransaction(transaction);
   }
    
    public void processEditorChange(ComboBox combo) {
        TextField textField = combo.getEditor();
        //Check to see if a transaction with the same is on the top of the stack
        jTPS_Transaction mostRecent = app.getTPS().peek();
        if(mostRecent instanceof EditTextField_Transaction) { //A text field was edited last
            EditTextField_Transaction transaction = (EditTextField_Transaction) mostRecent;
            if(transaction.getSource() == textField) { //identical source - remove it
                String initText = transaction.getStartText();
                EditTextField_Transaction newTransaction = new EditTextField_Transaction(textField, initText, textField.getText(), app);
                app.getTPS().pop();
                app.processTransaction(newTransaction);
                return;
            }
        } //otherwise, make a new transaction and process it
        //but what if the transaction isn't at the top of the stack?
        //the new transaction's start text needs to be the final instance of that transaction's end text
        //if no transaction with that event source is found, use "" to indicate an empty field
        ArrayList<jTPS_Transaction> transactions = app.getTPS().getTransactions();
        int start = app.getTPS().getMostRecent();
        String initString = "";
        for(int i = start; i >= 0; i--) { //iterate down the stack
            jTPS_Transaction t = transactions.get(i);
            if(t instanceof EditTextField_Transaction) {
                EditTextField_Transaction transaction = (EditTextField_Transaction) t;
                if(transaction.getSource() == textField) {
                    initString = transaction.getEndText();
                    break;
                }
            } else if(t instanceof ComboBox_Transaction) {
                ComboBox_Transaction transaction = (ComboBox_Transaction) t;
                if(transaction.getSource() == combo) {
                    initString = transaction.getEndText();
                    break;
                }
            }
        }
        EditTextField_Transaction transaction = new EditTextField_Transaction(textField, initString, textField.getText(), app); 
        app.processTransaction(transaction);
   }
    
    public void processCheckBoxChange(CheckBox box) {
        CheckBox_Transaction transaction = new CheckBox_Transaction(box, box.isSelected(), app);
        app.processTransaction(transaction);
    }
}
