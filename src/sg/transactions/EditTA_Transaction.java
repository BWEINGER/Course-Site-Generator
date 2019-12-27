package sg.transactions;

import jtps.jTPS_Transaction;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class EditTA_Transaction implements jTPS_Transaction {
    TeachingAssistantPrototype taToEdit;
    String oldName, newName;
    String oldEmail, newEmail;
    String oldType, newType;
    SiteGeneratorData data;
    
    public EditTA_Transaction(TeachingAssistantPrototype initTAToEdit, 
            String name, String email, String type, SiteGeneratorData initData) {
        taToEdit = initTAToEdit;
        oldName = initTAToEdit.getName();
        oldEmail = initTAToEdit.getEmail();
        oldType = initTAToEdit.getType();
        newName = name;
        newEmail = email;
        newType = type;
        data = initData;
    }


    @Override
    public void doTransaction() {
        taToEdit.setName(newName);
        taToEdit.setEmail(newEmail);
        taToEdit.setType(newType);
        data.updateTAs();
    }

    @Override
    public void undoTransaction() {
        taToEdit.setName(oldName);
        taToEdit.setEmail(oldEmail);
        taToEdit.setType(oldType);
        data.updateTAs();
    }
}