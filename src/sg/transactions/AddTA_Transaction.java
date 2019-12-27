package sg.transactions;

import jtps.jTPS_Transaction;
import sg.data.SiteGeneratorData;
import sg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    SiteGeneratorData data;
    TeachingAssistantPrototype ta;
    
    public AddTA_Transaction(SiteGeneratorData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
    }
}
