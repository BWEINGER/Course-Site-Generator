/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.transactions;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jtps.jTPS_Transaction;
import sg.SiteGeneratorApp;

/**
 *
 * @author brettweinger
 */
public class Image_Transaction implements jTPS_Transaction {
    ImageView iv;
    Image oldImage;
    Image newImage;
    SiteGeneratorApp app;
    
    public Image_Transaction(ImageView initIV, Image initOld, Image initNew, SiteGeneratorApp initApp) {
        iv = initIV;
        oldImage = initOld;
        newImage = initNew;
        app = initApp;
    }
    
    public ImageView getSource() {
        return iv;
    }
    
    public Image getOld() {
        return oldImage;
    }
    
    public Image getNew() {
        return newImage;
    }
    
    @Override
    public void doTransaction() {
        app.getWorkspaceComponent().process(false);
        iv.setImage(newImage);
        app.getWorkspaceComponent().process(true);
    }
    
    @Override
    public void undoTransaction() {
        app.getWorkspaceComponent().process(false);
        iv.setImage(oldImage);
        app.getWorkspaceComponent().process(true);
    }
}
