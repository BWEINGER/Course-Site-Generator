/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.tabs;

import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SITE_BANNER_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_BANNER_PANE;
import static sg.SiteGeneratorPropertyType.SITE_COMBO_BOX_PANE;
import static sg.SiteGeneratorPropertyType.SITE_CSS_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_CSS_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SITE_EMAIL_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_EMAIL_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SITE_EXPORT_DIR_BOLD;
import static sg.SiteGeneratorPropertyType.SITE_EXPORT_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_EXPORT_PANE;
import static sg.SiteGeneratorPropertyType.SITE_FAVICON;
import static sg.SiteGeneratorPropertyType.SITE_FAVICON_BUTTON;
import static sg.SiteGeneratorPropertyType.SITE_FONTS_AND_COLORS_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_HOME_CHECKBOX;
import static sg.SiteGeneratorPropertyType.SITE_HOME_PAGE_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_HOME_PAGE_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SITE_HWS_CHECKBOX;
import static sg.SiteGeneratorPropertyType.SITE_INSTRUCTOR_INFO_PANE;
import static sg.SiteGeneratorPropertyType.SITE_INSTRUCTOR_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_INSTRUCTOR_OH_PANE;
import static sg.SiteGeneratorPropertyType.SITE_INSTRUCTOR_PANE;
import static sg.SiteGeneratorPropertyType.SITE_LEFT_FOOTER;
import static sg.SiteGeneratorPropertyType.SITE_LEFT_FOOTER_BUTTON;
import static sg.SiteGeneratorPropertyType.SITE_NAME_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_NAME_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SITE_NAVBAR;
import static sg.SiteGeneratorPropertyType.SITE_NAVBAR_BUTTON;
import static sg.SiteGeneratorPropertyType.SITE_NUMBER_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_NUMBER_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SITE_NUMBER_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_OH_BUTTON;
import static sg.SiteGeneratorPropertyType.SITE_OH_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_OH_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SITE_PAGES_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_PAGES_PANE;
import static sg.SiteGeneratorPropertyType.SITE_RIGHT_FOOTER;
import static sg.SiteGeneratorPropertyType.SITE_RIGHT_FOOTER_BUTTON;
import static sg.SiteGeneratorPropertyType.SITE_ROOM_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_ROOM_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SITE_SCHEDULE_CHECKBOX;
import static sg.SiteGeneratorPropertyType.SITE_SEMESTER_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_SEMESTER_COMBO_DEFAULT;
import static sg.SiteGeneratorPropertyType.SITE_SEMESTER_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SITE_SEMESTER_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_STYLESHEETS_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_STYLE_IMAGES_PANE;
import static sg.SiteGeneratorPropertyType.SITE_STYLE_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_STYLE_NOTE_PANE;
import static sg.SiteGeneratorPropertyType.SITE_STYLE_PANE;
import static sg.SiteGeneratorPropertyType.SITE_SUBJECT_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_SUBJECT_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SITE_SUBJECT_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_SYLLABUS_CHECKBOX;
import static sg.SiteGeneratorPropertyType.SITE_TITLE_LABEL;
import static sg.SiteGeneratorPropertyType.SITE_TITLE_TEXT_FIELD;
import static sg.SiteGeneratorPropertyType.SITE_YEAR_COMBO;
import static sg.SiteGeneratorPropertyType.SITE_YEAR_COMBO_DEFAULT;
import static sg.SiteGeneratorPropertyType.SITE_YEAR_COMBO_OPTIONS;
import static sg.SiteGeneratorPropertyType.SITE_YEAR_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOLD_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_CHECK_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_COMBO_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_HEADER_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_IMAGE;
import static sg.workspace.style.SGStyle.CLASS_SITE_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_PANE;
import static sg.workspace.style.SGStyle.CLASS_SITE_TEXT_AREA;
import static sg.workspace.style.SGStyle.CLASS_SITE_TEXT_FIELD;

/**
 *
 * @author brettweinger
 */
public class SiteTab {
    
    SiteGeneratorApp app;
    VBox sitePane;

    public SiteTab(SiteGeneratorApp initApp) {
        app = initApp;
        sitePane = new VBox();
        sitePane.setSpacing(5);
        sitePane.setStyle("-fx-background-color: #fcd0a4");
        sitePane.setPadding(new Insets(3, 2, 3, 2));
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        //initControllers();

        // INIT FOOLPROOF DESIGN
        //initFoolproofDesign();

        // INIT DIALOGS
        //initDialogs();
    }

    public VBox getSitePane() {
        return sitePane;
    }
    
    public void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        //START CONSTRUCTING THE UI
        VBox bannerPane = ohBuilder.buildVBox(SITE_BANNER_PANE, sitePane, CLASS_SITE_BOX, ENABLED);
        ohBuilder.buildLabel(SITE_BANNER_LABEL, bannerPane, CLASS_SITE_HEADER_LABEL, ENABLED);
        
        bannerPane.setSpacing(10);
        
        //Construct combo box pane. Add some options first.
        ArrayList<String> subjectOptions = new ArrayList<>();
        subjectOptions.add("AMS");
        subjectOptions.add("CSE");
        subjectOptions.add("ISE");
        props.addPropertyOptionsList(SITE_SUBJECT_COMBO_OPTIONS, subjectOptions);
        
        ArrayList<String> numberOptions = new ArrayList<>();
        numberOptions.add("114");
        numberOptions.add("214");
        numberOptions.add("219");
        props.addPropertyOptionsList(SITE_NUMBER_COMBO_OPTIONS, numberOptions);
        
        ArrayList<String> semesterOptions = new ArrayList<>();
        semesterOptions.add("Fall");
        semesterOptions.add("Spring");
        semesterOptions.add("Summer");
        semesterOptions.add("Winter");
        props.addPropertyOptionsList(SITE_SEMESTER_COMBO_OPTIONS, semesterOptions);
        
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        ArrayList<String> yearOptions = new ArrayList<>();
        yearOptions.add("" + year);
        yearOptions.add("" + ++year);
        props.addPropertyOptionsList(SITE_YEAR_COMBO_OPTIONS, yearOptions);
        
        GridPane comboBoxPane = ohBuilder.buildGridPane(SITE_COMBO_BOX_PANE, bannerPane, CLASS_SITE_PANE, ENABLED);
        Label subjectLabel = ohBuilder.buildLabel(SITE_SUBJECT_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ComboBox subjectCombo = ohBuilder.buildComboBox(SITE_SUBJECT_COMBO, SITE_SUBJECT_COMBO_OPTIONS, "", null, CLASS_SITE_COMBO_BOX, ENABLED);
        Label numberLabel = ohBuilder.buildLabel(SITE_NUMBER_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ComboBox numberCombo = ohBuilder.buildComboBox(SITE_NUMBER_COMBO, SITE_NUMBER_COMBO_OPTIONS, "", null, CLASS_SITE_COMBO_BOX, ENABLED);
        Label semesterLabel = ohBuilder.buildLabel(SITE_SEMESTER_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ComboBox semesterCombo = ohBuilder.buildComboBox(SITE_SEMESTER_COMBO, SITE_SEMESTER_COMBO_OPTIONS, SITE_SEMESTER_COMBO_DEFAULT, null, CLASS_SITE_COMBO_BOX, ENABLED);
        Label yearLabel = ohBuilder.buildLabel(SITE_YEAR_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ComboBox yearCombo = ohBuilder.buildComboBox(SITE_YEAR_COMBO, SITE_YEAR_COMBO_OPTIONS, SITE_YEAR_COMBO_DEFAULT, null, CLASS_SITE_COMBO_BOX, ENABLED);
        //Label subjectLabel = ohBuilder.buildLabel(SITE_SUBJECT_LABEL, null, CLASS_SITE_LABEL, ENABLED);
        //ComboBox subjectCombo = ohBuilder.buildComboBox(SITE_SUBJECT_COMBO, SITE_SUBJECT_COMBO_OPTIONS, SITE_SUBJECT_COMBO_DEFAULT, null, CLASS_SITE_COMBO_BOX, ENABLED);
        comboBoxPane.setHgap(20);
        comboBoxPane.setVgap(10);
        subjectCombo.setEditable(ENABLED);
        numberCombo.setEditable(ENABLED);
        semesterCombo.setEditable(ENABLED);
        yearCombo.setEditable(ENABLED);
        
        comboBoxPane.add(subjectLabel, 0, 0);
        comboBoxPane.add(subjectCombo, 1, 0);
        comboBoxPane.add(numberLabel, 2, 0);
        comboBoxPane.add(numberCombo, 3, 0);
        comboBoxPane.add(semesterLabel, 0, 1);
        comboBoxPane.add(semesterCombo, 1, 1);
        comboBoxPane.add(yearLabel, 2, 1);
        comboBoxPane.add(yearCombo, 3, 1);
        
        HBox titlePane = ohBuilder.buildHBox(SITE_COMBO_BOX_PANE, bannerPane, CLASS_SITE_PANE, ENABLED);
        Label titleLabel = ohBuilder.buildLabel(SITE_TITLE_LABEL, titlePane, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField titleTextField = ohBuilder.buildTextField(SITE_TITLE_TEXT_FIELD, titlePane, CLASS_SITE_TEXT_FIELD, ENABLED);
        HBox exportPane = ohBuilder.buildHBox(SITE_EXPORT_PANE, bannerPane, CLASS_SITE_PANE, ENABLED);
        Label exportDirLabel = ohBuilder.buildLabel(SITE_EXPORT_DIR_BOLD, exportPane, CLASS_SITE_BOLD_LABEL, ENABLED);
        Label exportLabel = ohBuilder.buildLabel(SITE_EXPORT_LABEL, exportPane, CLASS_SITE_LABEL, ENABLED);
        titleTextField.setMinWidth(225);
        titleTextField.setMaxWidth(225);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        
        titlePane.setSpacing(49);
        exportPane.setSpacing(18);
        
        //titlePane.setPadding(new Insets(-20, 0, 0, 0));
        
        HBox pagesPane = ohBuilder.buildHBox(SITE_PAGES_PANE, sitePane, CLASS_SITE_BOX, ENABLED);
        pagesPane.setSpacing(25);
        ohBuilder.buildLabel(SITE_PAGES_LABEL, pagesPane, CLASS_SITE_HEADER_LABEL, ENABLED);
        CheckBox homeBox = ohBuilder.buildCheckBox(SITE_HOME_CHECKBOX, pagesPane, CLASS_SITE_CHECK_BOX, ENABLED);
        CheckBox syllabusBox = ohBuilder.buildCheckBox(SITE_SYLLABUS_CHECKBOX, pagesPane, CLASS_SITE_CHECK_BOX, ENABLED);
        CheckBox scheduleBox = ohBuilder.buildCheckBox(SITE_SCHEDULE_CHECKBOX, pagesPane, CLASS_SITE_CHECK_BOX, ENABLED);
        CheckBox hwsBox = ohBuilder.buildCheckBox(SITE_HWS_CHECKBOX, pagesPane, CLASS_SITE_CHECK_BOX, ENABLED);
     
        VBox stylePane = ohBuilder.buildVBox(SITE_STYLE_PANE, sitePane, CLASS_SITE_BOX, ENABLED);
        ohBuilder.buildLabel(SITE_STYLE_LABEL, stylePane, CLASS_SITE_HEADER_LABEL, ENABLED);
        
        stylePane.setSpacing(10);
        
        GridPane imagesPane = ohBuilder.buildGridPane(SITE_STYLE_IMAGES_PANE, stylePane, CLASS_SITE_PANE, ENABLED);
        Button faviconButton = ohBuilder.buildTextButton(SITE_FAVICON_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button navbarButton = ohBuilder.buildTextButton(SITE_NAVBAR_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button leftFooterButton = ohBuilder.buildTextButton(SITE_LEFT_FOOTER_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button rightFooterButton = ohBuilder.buildTextButton(SITE_RIGHT_FOOTER_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        
        Image favicon = new Image("https://pbs.twimg.com/profile_images/1054070756848607233/wHfu_1gp_400x400.jpg");
        ImageView faviconIV = ohBuilder.buildImageView(SITE_FAVICON, null, CLASS_SITE_IMAGE, ENABLED);
        Image navbar = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUDarkRedShieldLogo.png");
        ImageView navbarIV = ohBuilder.buildImageView(SITE_NAVBAR, null, CLASS_SITE_IMAGE, ENABLED);
        Image leftFooter = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUWhiteShieldLogo.jpg");
        ImageView leftFooterIV = ohBuilder.buildImageView(SITE_LEFT_FOOTER, null, CLASS_SITE_IMAGE, ENABLED);
        Image rightFooter = new Image("http://www3.cs.stonybrook.edu/~cse219/images/SBUCSLogo.png");
        ImageView rightFooterIV = ohBuilder.buildImageView(SITE_RIGHT_FOOTER, null, CLASS_SITE_IMAGE, ENABLED);
        
        faviconIV.setImage(favicon);
        faviconIV.setPreserveRatio(true);
        faviconIV.setFitWidth(27);
        navbarIV.setImage(navbar);
        navbarIV.setPreserveRatio(true);
        navbarIV.setFitWidth(150);
        leftFooterIV.setImage(leftFooter);
        leftFooterIV.setPreserveRatio(true);
        leftFooterIV.setFitWidth(150);
        rightFooterIV.setImage(rightFooter);
        rightFooterIV.setPreserveRatio(true);
        rightFooterIV.setFitWidth(150);
        
        ArrayList<String> cssOptions = new ArrayList<String>();
        try {
            File directory = new File("work/css");
            File[] fList = directory.listFiles();
            for(File file : fList) {
                if(file.isFile()) {
                    cssOptions.add(file.getName());
                }
            }
        } catch(Exception e) { }
        
        props.addPropertyOptionsList(SITE_CSS_COMBO_OPTIONS, cssOptions);
      
        Label fontsAndColors = ohBuilder.buildLabel(SITE_FONTS_AND_COLORS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ComboBox cssCombo = ohBuilder.buildComboBox(SITE_CSS_COMBO, SITE_CSS_COMBO_OPTIONS, "", null, CLASS_SITE_COMBO_BOX, ENABLED);
        cssCombo.setEditable(false);
        
        imagesPane.setHgap(15);
        imagesPane.setVgap(10);
        imagesPane.add(faviconButton, 0, 0);
        imagesPane.add(faviconIV, 1, 0);
        imagesPane.add(navbarButton, 0, 1);
        imagesPane.add(navbarIV, 1, 1);
        imagesPane.add(leftFooterButton, 0, 2);
        imagesPane.add(leftFooterIV, 1, 2);
        imagesPane.add(rightFooterButton, 0, 3);
        imagesPane.add(rightFooterIV, 1, 3);
        imagesPane.add(fontsAndColors, 0, 4);
        imagesPane.add(cssCombo, 1, 4);
        
        HBox noteBox = ohBuilder.buildHBox(SITE_STYLE_NOTE_PANE, stylePane, CLASS_SITE_PANE, ENABLED);
        Label note = ohBuilder.buildLabel(SITE_STYLESHEETS_LABEL, noteBox, CLASS_SITE_BOLD_LABEL, ENABLED);
          
        VBox instructorPane = ohBuilder.buildVBox(SITE_INSTRUCTOR_PANE, sitePane, CLASS_SITE_BOX, ENABLED);
        ohBuilder.buildLabel(SITE_INSTRUCTOR_LABEL, instructorPane, CLASS_SITE_HEADER_LABEL, ENABLED);
        instructorPane.setSpacing(25);
        
        GridPane instructorInfoPane = ohBuilder.buildGridPane(SITE_INSTRUCTOR_INFO_PANE, instructorPane, CLASS_SITE_PANE, ENABLED);
        Label name = ohBuilder.buildLabel(SITE_NAME_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField nameText = ohBuilder.buildTextField(SITE_NAME_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        Label email = ohBuilder.buildLabel(SITE_EMAIL_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField emailText = ohBuilder.buildTextField(SITE_EMAIL_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        Label room = ohBuilder.buildLabel(SITE_ROOM_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField roomText = ohBuilder.buildTextField(SITE_ROOM_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        Label homePage = ohBuilder.buildLabel(SITE_HOME_PAGE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        TextField homePageText = ohBuilder.buildTextField(SITE_HOME_PAGE_TEXT_FIELD, null, CLASS_SITE_TEXT_FIELD, ENABLED);
        
        instructorInfoPane.setHgap(20);
        instructorInfoPane.setVgap(25);
        instructorInfoPane.add(name, 0, 0);
        instructorInfoPane.add(nameText, 1, 0);
        instructorInfoPane.add(room, 2, 0);
        instructorInfoPane.add(roomText, 3, 0);
        instructorInfoPane.add(email, 0, 1);
        instructorInfoPane.add(emailText, 1, 1);
        instructorInfoPane.add(homePage, 2, 1);
        instructorInfoPane.add(homePageText, 3, 1);
        
        GridPane instructorOHPane = ohBuilder.buildGridPane(SITE_INSTRUCTOR_OH_PANE, instructorPane, CLASS_SITE_PANE, ENABLED);
        Button ohButton = ohBuilder.buildTextButton(SITE_OH_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Label ohLabel = ohBuilder.buildLabel(SITE_OH_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        ohButton.setText("+");
        TextArea ohText = ohBuilder.buildTextArea(SITE_OH_TEXT_AREA, instructorPane, CLASS_SITE_TEXT_AREA, ENABLED);
        ohText.setMinHeight(100);
        ohText.setMaxHeight(100);
        ohText.setVisible(false);
        instructorOHPane.setHgap(5);
        instructorOHPane.add(ohButton, 0, 0);
        instructorOHPane.add(ohLabel, 1, 0);
        
        ohButton.setMinWidth(30);
        ohButton.setMaxWidth(30);
        instructorPane.prefHeightProperty().bind(sitePane.heightProperty());
     }
}