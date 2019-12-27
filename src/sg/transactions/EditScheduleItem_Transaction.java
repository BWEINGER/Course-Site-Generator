/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import djf.modules.AppGUIModule;
import java.util.Collections;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import static sg.SiteGeneratorPropertyType.SCH_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.SCH_ITEMS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.SCH_UPDATE_BUTTON_TEXT;
import sg.data.ScheduleItem;
import sg.data.SiteGeneratorData;

/**
 *
 * @author brettweinger
 */
public class EditScheduleItem_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    ScheduleItem oldItem;
    ScheduleItem newItem;
    TableView<ScheduleItem> items;
    SiteGeneratorData data;
    Button addButton;
    PropertiesManager props;
    
    public EditScheduleItem_Transaction(AppGUIModule initGUI, ScheduleItem initOld, ScheduleItem initNew, SiteGeneratorData initData) {
        gui = initGUI;
        oldItem = initOld;
        newItem = initNew;
        items = (TableView<ScheduleItem>) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        data = initData;
        addButton = (Button) gui.getGUINode(SCH_ADD_BUTTON);
        props = PropertiesManager.getPropertiesManager();
    }
    
    @Override
    public void doTransaction() {
        //items.getItems().remove(oldItem);
        //items.getItems().add(newItem);
        if(addButton.getText().equals(props.getProperty(SCH_UPDATE_BUTTON_TEXT))) {
            data.processClearFields();
        }
        data.getAllItems().remove(oldItem);
        data.getAllItems().add(newItem);
        data.updateItems();
    }
    
    @Override
    public void undoTransaction() {
        //items.getItems().remove(newItem);
        //items.getItems().add(oldItem);
        if(addButton.getText().equals(props.getProperty(SCH_UPDATE_BUTTON_TEXT))) {
            data.processClearFields();
        }
        data.getAllItems().remove(newItem);
        data.getAllItems().add(oldItem);
        data.updateItems();
    }
}
