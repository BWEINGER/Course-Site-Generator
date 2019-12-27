package sg.transactions;

import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;

public class PasteTA_Transaction implements jTPS_Transaction {
    SiteGeneratorApp app;
    TeachingAssistantPrototype taToPaste;

    public PasteTA_Transaction(  SiteGeneratorApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        SiteGeneratorData data = (SiteGeneratorData)app.getDataComponent();
        data.removeTA(taToPaste);
    }   
}