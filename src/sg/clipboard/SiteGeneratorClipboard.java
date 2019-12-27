package sg.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import java.util.HashMap;
import sg.SiteGeneratorApp;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;
import sg.data.TimeSlot;
import sg.data.TimeSlot.DayOfWeek;
import sg.transactions.CutTA_Transaction;
import sg.transactions.PasteTA_Transaction;


public class SiteGeneratorClipboard implements AppClipboardComponent {
    SiteGeneratorApp app;
    TeachingAssistantPrototype clipboardCutTA;
    TeachingAssistantPrototype clipboardCopiedTA;

    public SiteGeneratorClipboard(SiteGeneratorApp initApp) {
        app = initApp;
        clipboardCutTA = null;
        clipboardCopiedTA = null;
    }

    @Override
    public void cut() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            clipboardCutTA = data.getSelectedTA();
            clipboardCopiedTA = null;
            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(clipboardCutTA);
            CutTA_Transaction transaction = new CutTA_Transaction((SiteGeneratorApp)app, clipboardCutTA, officeHours);
            app.processTransaction(transaction);
        }
    }

    @Override
    public void copy() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype tempTA = data.getSelectedTA();
            copyToCopiedClipboard(tempTA);
        }
    }
    
    private void copyToCopiedClipboard(TeachingAssistantPrototype taToCopy) {
        clipboardCutTA = null;
        clipboardCopiedTA = copyTA(taToCopy);
        app.getFoolproofModule().updateAll();        
    }
    
    private TeachingAssistantPrototype copyTA(TeachingAssistantPrototype taToCopy) {
        TeachingAssistantPrototype tempCopy = (TeachingAssistantPrototype)taToCopy.clone(); 
        tempCopy.setName(tempCopy.getName() + "_1");
        tempCopy.setEmail(tempCopy.getEmail() + "_1");
        return tempCopy;
    }

    @Override
    public void paste() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        if (clipboardCutTA != null) {
            PasteTA_Transaction transaction = new PasteTA_Transaction((SiteGeneratorApp)app, clipboardCutTA);
            app.processTransaction(transaction);
            
            this.clipboardCutTA = null;
            app.getFoolproofModule().updateAll();
        }
        else if (clipboardCopiedTA != null) {
            // FIGURE OUT THE PROPER NAME AND
            // EMAIL ADDRESS SO THAT THERE ISN'T A DUPLICATE             
            PasteTA_Transaction transaction = new PasteTA_Transaction((SiteGeneratorApp)app, clipboardCopiedTA);
            app.processTransaction(transaction);
            
            this.clipboardCopiedTA = null;
            app.getFoolproofModule().updateAll();
        }
    }    

    @Override
    public boolean hasSomethingToCut() {
        return ((SiteGeneratorData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToCopy() {
        return ((SiteGeneratorData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToPaste() {
        if ((clipboardCutTA != null) || (clipboardCopiedTA != null))
            return true;
        else
            return false;
    }
}