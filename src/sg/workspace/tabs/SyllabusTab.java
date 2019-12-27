/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.workspace.tabs;

import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.SYL_ASSISTANCE_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_ASSISTANCE_INFO;
import static sg.SiteGeneratorPropertyType.SYL_ASSISTANCE_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_ASSISTANCE_PANE;
import static sg.SiteGeneratorPropertyType.SYL_ASSISTANCE_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_COMPONENTS_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_COMPONENTS_INFO;
import static sg.SiteGeneratorPropertyType.SYL_COMPONENTS_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_COMPONENTS_PANE;
import static sg.SiteGeneratorPropertyType.SYL_COMPONENTS_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_DESCRIPTION_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_DESCRIPTION_INFO;
import static sg.SiteGeneratorPropertyType.SYL_DESCRIPTION_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_DESCRIPTION_PANE;
import static sg.SiteGeneratorPropertyType.SYL_DESCRIPTION_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_DISHONESTY_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_DISHONESTY_INFO;
import static sg.SiteGeneratorPropertyType.SYL_DISHONESTY_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_DISHONESTY_PANE;
import static sg.SiteGeneratorPropertyType.SYL_DISHONESTY_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_NOTE_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_NOTE_INFO;
import static sg.SiteGeneratorPropertyType.SYL_NOTE_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_NOTE_PANE;
import static sg.SiteGeneratorPropertyType.SYL_NOTE_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_OUTCOMES_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_OUTCOMES_INFO;
import static sg.SiteGeneratorPropertyType.SYL_OUTCOMES_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_OUTCOMES_PANE;
import static sg.SiteGeneratorPropertyType.SYL_OUTCOMES_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_PREREQS_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_PREREQS_INFO;
import static sg.SiteGeneratorPropertyType.SYL_PREREQS_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_PREREQS_PANE;
import static sg.SiteGeneratorPropertyType.SYL_PREREQS_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_TEXTBOOKS_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_TEXTBOOKS_INFO;
import static sg.SiteGeneratorPropertyType.SYL_TEXTBOOKS_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_TEXTBOOKS_PANE;
import static sg.SiteGeneratorPropertyType.SYL_TEXTBOOKS_TEXT_AREA;
import static sg.SiteGeneratorPropertyType.SYL_TOPICS_BUTTON;
import static sg.SiteGeneratorPropertyType.SYL_TOPICS_INFO;
import static sg.SiteGeneratorPropertyType.SYL_TOPICS_LABEL;
import static sg.SiteGeneratorPropertyType.SYL_TOPICS_PANE;
import static sg.SiteGeneratorPropertyType.SYL_TOPICS_TEXT_AREA;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOLD_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_PANE;
import static sg.workspace.style.SGStyle.CLASS_SYL_TEXT_AREA;

/**
 *
 * @author brettweinger
 */
public class SyllabusTab {
    
    SiteGeneratorApp app;
    VBox syllabusPane;

    public SyllabusTab(SiteGeneratorApp initApp) {
        app = initApp;
        syllabusPane = new VBox();
        syllabusPane.setSpacing(5);
        syllabusPane.setStyle("-fx-background-color: #fcd0a4");
        syllabusPane.setPadding(new Insets(3, 2, 3, 2));
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        //initControllers();

        // INIT FOOLPROOF DESIGN
        //initFoolproofDesign();

        // INIT DIALOGS
        //initDialogs();
    }

    public VBox getSyllabusPane() {
        return syllabusPane;
    }
    
    public void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        
        //START CONSTRUCTING THE UI
        VBox descriptionPane = builder.buildVBox(SYL_DESCRIPTION_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        descriptionPane.setSpacing(5);
        GridPane descriptionInfo = builder.buildGridPane(SYL_DESCRIPTION_INFO, descriptionPane, CLASS_SITE_PANE, ENABLED);
        descriptionInfo.setHgap(5);
        Button descriptionButton = builder.buildTextButton(SYL_DESCRIPTION_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        descriptionButton.setText("-");
        Label descriptionLabel = builder.buildLabel(SYL_DESCRIPTION_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        descriptionInfo.add(descriptionButton, 0, 0);
        descriptionInfo.add(descriptionLabel, 1, 0);
        TextArea txtDesc = builder.buildTextArea(SYL_DESCRIPTION_TEXT_AREA, descriptionPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtDesc.setMinHeight(235);
        txtDesc.setMaxHeight(235);
        txtDesc.setWrapText(true);
        
        VBox topicsPane = builder.buildVBox(SYL_TOPICS_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        topicsPane.setSpacing(5);
        GridPane topicsInfo = builder.buildGridPane(SYL_TOPICS_INFO, topicsPane, CLASS_SITE_PANE, ENABLED);
        topicsInfo.setHgap(5);
        Button topicsButton = builder.buildTextButton(SYL_TOPICS_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        topicsButton.setText("+");
        Label topicsLabel = builder.buildLabel(SYL_TOPICS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        topicsInfo.add(topicsButton, 0, 0);
        topicsInfo.add(topicsLabel, 1, 0);
        TextArea txtTop = builder.buildTextArea(SYL_TOPICS_TEXT_AREA, topicsPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtTop.setMinHeight(235);
        txtTop.setMaxHeight(235);
        txtTop.setVisible(false);
        topicsPane.setMinHeight(50);
        topicsPane.setMaxHeight(50);
        txtTop.setWrapText(true);
        
        VBox prereqsPane = builder.buildVBox(SYL_PREREQS_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        prereqsPane.setSpacing(5);
        GridPane prereqsInfo = builder.buildGridPane(SYL_PREREQS_INFO, prereqsPane, CLASS_SITE_PANE, ENABLED);
        prereqsInfo.setHgap(5);
        Button prereqsButton = builder.buildTextButton(SYL_PREREQS_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        prereqsButton.setText("+");
        Label prereqsLabel = builder.buildLabel(SYL_PREREQS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        prereqsInfo.add(prereqsButton, 0, 0);
        prereqsInfo.add(prereqsLabel, 1, 0);
        TextArea txtPre = builder.buildTextArea(SYL_PREREQS_TEXT_AREA, prereqsPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtPre.setMinHeight(235);
        txtPre.setMaxHeight(235);
        txtPre.setVisible(false);
        prereqsPane.setMinHeight(50);
        prereqsPane.setMaxHeight(50);
        txtPre.setWrapText(true);
        
        VBox outcomesPane = builder.buildVBox(SYL_OUTCOMES_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        outcomesPane.setSpacing(5);
        GridPane outcomesInfo = builder.buildGridPane(SYL_OUTCOMES_INFO, outcomesPane, CLASS_SITE_PANE, ENABLED);
        outcomesInfo.setHgap(5);
        Button outcomesButton = builder.buildTextButton(SYL_OUTCOMES_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        outcomesButton.setText("+");
        Label outcomesLabel = builder.buildLabel(SYL_OUTCOMES_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        outcomesInfo.add(outcomesButton, 0, 0);
        outcomesInfo.add(outcomesLabel, 1, 0);
        TextArea txtOut = builder.buildTextArea(SYL_OUTCOMES_TEXT_AREA, outcomesPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtOut.setMinHeight(235);
        txtOut.setMaxHeight(235);
        txtOut.setVisible(false);
        outcomesPane.setMinHeight(50);
        outcomesPane.setMaxHeight(50);
        txtOut.setWrapText(true);
        
        VBox textbooksPane = builder.buildVBox(SYL_TEXTBOOKS_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        textbooksPane.setSpacing(5);
        GridPane textbooksInfo = builder.buildGridPane(SYL_TEXTBOOKS_INFO, textbooksPane, CLASS_SITE_PANE, ENABLED);
        textbooksInfo.setHgap(5);
        Button textbooksButton = builder.buildTextButton(SYL_TEXTBOOKS_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        textbooksButton.setText("+");
        Label textbooksLabel = builder.buildLabel(SYL_TEXTBOOKS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        textbooksInfo.add(textbooksButton, 0, 0);
        textbooksInfo.add(textbooksLabel, 1, 0);
        TextArea txtText = builder.buildTextArea(SYL_TEXTBOOKS_TEXT_AREA, textbooksPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtText.setMinHeight(235);
        txtText.setMaxHeight(235);
        txtText.setVisible(false);
        textbooksPane.setMinHeight(50);
        textbooksPane.setMaxHeight(50);
        txtText.setWrapText(true);
        
        VBox componentsPane = builder.buildVBox(SYL_COMPONENTS_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        componentsPane.setSpacing(5);
        GridPane componentsInfo = builder.buildGridPane(SYL_COMPONENTS_INFO, componentsPane, CLASS_SITE_PANE, ENABLED);
        componentsInfo.setHgap(5);
        Button componentsButton = builder.buildTextButton(SYL_COMPONENTS_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        componentsButton.setText("+");
        Label componentsLabel = builder.buildLabel(SYL_COMPONENTS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        componentsInfo.add(componentsButton, 0, 0);
        componentsInfo.add(componentsLabel, 1, 0);
        TextArea txtComp = builder.buildTextArea(SYL_COMPONENTS_TEXT_AREA, componentsPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtComp.setMinHeight(235);
        txtComp.setMaxHeight(235);
        txtComp.setVisible(false);
        componentsPane.setMinHeight(50);
        componentsPane.setMaxHeight(50);
        txtComp.setWrapText(true);
        
        VBox notePane = builder.buildVBox(SYL_NOTE_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        notePane.setSpacing(5);
        GridPane noteInfo = builder.buildGridPane(SYL_NOTE_INFO, notePane, CLASS_SITE_PANE, ENABLED);
        noteInfo.setHgap(5);
        Button noteButton = builder.buildTextButton(SYL_NOTE_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        noteButton.setText("+");
        Label noteLabel = builder.buildLabel(SYL_NOTE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        noteInfo.add(noteButton, 0, 0);
        noteInfo.add(noteLabel, 1, 0);
        TextArea txtInfo = builder.buildTextArea(SYL_NOTE_TEXT_AREA, notePane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtInfo.setMinHeight(235);
        txtInfo.setMaxHeight(235);
        txtInfo.setVisible(false);
        notePane.setMinHeight(50);
        notePane.setMaxHeight(50);
        txtInfo.setWrapText(true);
        
        VBox dishonestyPane = builder.buildVBox(SYL_DISHONESTY_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        dishonestyPane.setSpacing(5);
        GridPane dishonestyInfo = builder.buildGridPane(SYL_DISHONESTY_INFO, dishonestyPane, CLASS_SITE_PANE, ENABLED);
        dishonestyInfo.setHgap(5);
        Button dishonestyButton = builder.buildTextButton(SYL_DISHONESTY_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        dishonestyButton.setText("+");
        Label dishonestyLabel = builder.buildLabel(SYL_DISHONESTY_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        dishonestyInfo.add(dishonestyButton, 0, 0);
        dishonestyInfo.add(dishonestyLabel, 1, 0);
        TextArea txtDis = builder.buildTextArea(SYL_DISHONESTY_TEXT_AREA, dishonestyPane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtDis.setMinHeight(235);
        txtDis.setMaxHeight(235);
        txtDis.setVisible(false);
        dishonestyPane.setMinHeight(50);
        dishonestyPane.setMaxHeight(50);
        txtDis.setWrapText(true);
        
        VBox assistancePane = builder.buildVBox(SYL_ASSISTANCE_PANE, syllabusPane, CLASS_SITE_BOX, ENABLED);
        assistancePane.setSpacing(5);
        GridPane assistanceInfo = builder.buildGridPane(SYL_ASSISTANCE_INFO, assistancePane, CLASS_SITE_PANE, ENABLED);
        assistanceInfo.setHgap(5);
        Button assistanceButton = builder.buildTextButton(SYL_ASSISTANCE_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        assistanceButton.setText("+");
        Label assistanceLabel = builder.buildLabel(SYL_ASSISTANCE_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        assistanceInfo.add(assistanceButton, 0, 0);
        assistanceInfo.add(assistanceLabel, 1, 0);
        TextArea txtA = builder.buildTextArea(SYL_ASSISTANCE_TEXT_AREA, assistancePane, CLASS_SYL_TEXT_AREA, ENABLED);
        txtA.setMinHeight(235);
        txtA.setMaxHeight(235);
        txtA.setVisible(false);
        assistancePane.setMinHeight(50);
        assistancePane.setMaxHeight(50);
        txtA.setWrapText(true);
    }
}