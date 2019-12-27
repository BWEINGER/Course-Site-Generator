package sg.transactions;

import jtps.jTPS_Transaction;
import static djf.AppPropertyType.APP_CLIPBOARD_FOOLPROOF_SETTINGS;
import java.util.ArrayList;
import java.util.HashMap;
import sg.SiteGeneratorApp;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;
import sg.data.TimeSlot;
import sg.data.TimeSlot.DayOfWeek;

public class CutTA_Transaction implements jTPS_Transaction {
    SiteGeneratorApp app;
    TeachingAssistantPrototype taToCut;
    HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours;

    public CutTA_Transaction(SiteGeneratorApp initApp, 
            TeachingAssistantPrototype initTAToCut, 
            HashMap<TimeSlot, ArrayList<DayOfWeek>> initOfficeHours) {
        app = initApp;
        taToCut = initTAToCut;
        officeHours = initOfficeHours;
    }

    @Override
    public void doTransaction() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        data.removeTA(taToCut, officeHours);
        app.getFoolproofModule().updateControls(APP_CLIPBOARD_FOOLPROOF_SETTINGS);
    }

    @Override
    public void undoTransaction() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        data.addTA(taToCut, officeHours);
        app.getFoolproofModule().updateControls(APP_CLIPBOARD_FOOLPROOF_SETTINGS);
    }   
}