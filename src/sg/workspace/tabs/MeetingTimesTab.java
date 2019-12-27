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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import sg.SiteGeneratorApp;
import static sg.SiteGeneratorPropertyType.MT_LABS_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_LABS_DAYS_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LABS_INFO;
import static sg.SiteGeneratorPropertyType.MT_LABS_LABEL;
import static sg.SiteGeneratorPropertyType.MT_LABS_PANE;
import static sg.SiteGeneratorPropertyType.MT_LABS_REMOVE_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_LABS_ROOM_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LABS_SECTION_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LABS_TA1_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LABS_TA2_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LABS_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_DAYS_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_INFO;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_LABEL;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_PANE;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_REMOVE_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_ROOM_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_SECTION_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_TABLE_VIEW;
import static sg.SiteGeneratorPropertyType.MT_LECTURES_TIME_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_ADD_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_DAYS_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_INFO;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_LABEL;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_PANE;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_REMOVE_BUTTON;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_ROOM_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_SECTION_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_TA1_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_TA2_COLUMN;
import static sg.SiteGeneratorPropertyType.MT_RECITATIONS_TABLE_VIEW;
import sg.data.Lab;
import sg.data.Lecture;
import sg.data.Recitation;
import static sg.workspace.style.SGStyle.CLASS_OH_COLUMN;
import static sg.workspace.style.SGStyle.CLASS_OH_TABLE_VIEW;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOLD_LABEL;
import static sg.workspace.style.SGStyle.CLASS_SITE_BOX;
import static sg.workspace.style.SGStyle.CLASS_SITE_BUTTON;
import static sg.workspace.style.SGStyle.CLASS_SITE_PANE;

/**
 *
 * @author brettweinger
 */
public class MeetingTimesTab {
    
    SiteGeneratorApp app;
    VBox meetingTimesPane;

    public MeetingTimesTab(SiteGeneratorApp initApp) {
        app = initApp;
        meetingTimesPane = new VBox();
        meetingTimesPane.setSpacing(5);
        meetingTimesPane.setStyle("-fx-background-color: #fcd0a4");
        meetingTimesPane.setPadding(new Insets(3, 2, 3, 2));
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        //initControllers();

        // INIT FOOLPROOF DESIGN
        //initFoolproofDesign();

        // INIT DIALOGS
        //initDialogs();
    }

    public VBox getMeetingTimesPane() {
        return meetingTimesPane;
    }
    
    public void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        
        //START CONSTRUCTING THE UI
        VBox lecturesPane = builder.buildVBox(MT_LECTURES_PANE, meetingTimesPane, CLASS_SITE_BOX, ENABLED);
        lecturesPane.setSpacing(10);
        GridPane lecturesInfo = builder.buildGridPane(MT_LECTURES_INFO, lecturesPane, CLASS_SITE_PANE, ENABLED);
        Button lecturesAdd = builder.buildTextButton(MT_LECTURES_ADD_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button lecturesRemove = builder.buildTextButton(MT_LECTURES_REMOVE_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        lecturesAdd.setText("+");
        lecturesRemove.setText("-");
        Label lecturesLabel = builder.buildLabel(MT_LECTURES_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        lecturesInfo.setHgap(5);
        lecturesInfo.add(lecturesAdd, 0, 0);
        lecturesInfo.add(lecturesRemove, 1, 0);
        lecturesInfo.add(lecturesLabel, 2, 0);
        
        //CONSTRUCT LECTURES TABLE VIEW
        TableView<Lecture> lecturesTable = builder.buildTableView(MT_LECTURES_TABLE_VIEW, lecturesPane, CLASS_OH_TABLE_VIEW, ENABLED);
        TableColumn lecturesSectionColumn = builder.buildTableColumn(MT_LECTURES_SECTION_COLUMN, lecturesTable, CLASS_OH_COLUMN);
        TableColumn lecturesDaysColumn = builder.buildTableColumn(MT_LECTURES_DAYS_COLUMN, lecturesTable, CLASS_OH_COLUMN);
        TableColumn lecturesTimeColumn = builder.buildTableColumn(MT_LECTURES_TIME_COLUMN, lecturesTable, CLASS_OH_COLUMN);
        TableColumn lecturesRoomColumn = builder.buildTableColumn(MT_LECTURES_ROOM_COLUMN, lecturesTable, CLASS_OH_COLUMN);
        lecturesSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        lecturesDaysColumn.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        lecturesTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
        lecturesRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        lecturesSectionColumn.prefWidthProperty().bind(lecturesTable.widthProperty().multiply(1.0 / 4.0));
        lecturesDaysColumn.prefWidthProperty().bind(lecturesTable.widthProperty().multiply(1.0 / 4.0));
        lecturesTimeColumn.prefWidthProperty().bind(lecturesTable.widthProperty().multiply(1.0 / 4.0));
        lecturesRoomColumn.prefWidthProperty().bind(lecturesTable.widthProperty().multiply(1.0 / 4.0));
        lecturesTable.setEditable(true);
        lecturesSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lecturesDaysColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lecturesTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lecturesRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //CONSTRUCT RECITATIONS TABLE VIEW
        VBox recitationsPane = builder.buildVBox(MT_RECITATIONS_PANE, meetingTimesPane, CLASS_SITE_BOX, ENABLED);
        recitationsPane.setSpacing(10);
        GridPane recitationsInfo = builder.buildGridPane(MT_RECITATIONS_INFO, recitationsPane, CLASS_SITE_PANE, ENABLED);
        Button recitationsAdd = builder.buildTextButton(MT_RECITATIONS_ADD_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button recitationsRemove = builder.buildTextButton(MT_RECITATIONS_REMOVE_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        recitationsAdd.setText("+");
        recitationsRemove.setText("-");
        Label recitationsLabel = builder.buildLabel(MT_RECITATIONS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        recitationsInfo.setHgap(5);
        recitationsInfo.add(recitationsAdd, 0, 0);
        recitationsInfo.add(recitationsRemove, 1, 0);
        recitationsInfo.add(recitationsLabel, 2, 0);
        
        TableView<Recitation> recitationsTable = builder.buildTableView(MT_RECITATIONS_TABLE_VIEW, recitationsPane, CLASS_OH_TABLE_VIEW, ENABLED);
        TableColumn recitationsSectionColumn = builder.buildTableColumn(MT_RECITATIONS_SECTION_COLUMN, recitationsTable, CLASS_OH_COLUMN);
        TableColumn recitationsDaysColumn = builder.buildTableColumn(MT_RECITATIONS_DAYS_COLUMN, recitationsTable, CLASS_OH_COLUMN);
        TableColumn recitationsRoomColumn = builder.buildTableColumn(MT_RECITATIONS_ROOM_COLUMN, recitationsTable, CLASS_OH_COLUMN);
        TableColumn recitationsTA1Column = builder.buildTableColumn(MT_RECITATIONS_TA1_COLUMN, recitationsTable, CLASS_OH_COLUMN);
        TableColumn recitationsTA2Column = builder.buildTableColumn(MT_RECITATIONS_TA2_COLUMN, recitationsTable, CLASS_OH_COLUMN);
        recitationsSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        recitationsDaysColumn.setCellValueFactory(new PropertyValueFactory<String, String>("daysTime"));
        recitationsRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        recitationsTA1Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta1"));
        recitationsTA2Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta2"));
        recitationsSectionColumn.prefWidthProperty().bind(recitationsTable.widthProperty().multiply(1.0 / 5.0));
        recitationsDaysColumn.prefWidthProperty().bind(recitationsTable.widthProperty().multiply(1.0 / 5.0));
        recitationsRoomColumn.prefWidthProperty().bind(recitationsTable.widthProperty().multiply(1.0 / 5.0));
        recitationsTA1Column.prefWidthProperty().bind(recitationsTable.widthProperty().multiply(1.0 / 5.0));
        recitationsTA2Column.prefWidthProperty().bind(recitationsTable.widthProperty().multiply(1.0 / 5.0));
        recitationsTable.setEditable(true);
        recitationsSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationsDaysColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationsRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationsTA1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationsTA2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //CONSTRUCT LABS TABLE VIEW  
        VBox labsPane = builder.buildVBox(MT_LABS_PANE, meetingTimesPane, CLASS_SITE_BOX, ENABLED);
        labsPane.setSpacing(10);
        GridPane labsInfo = builder.buildGridPane(MT_LABS_INFO, labsPane, CLASS_SITE_PANE, ENABLED);
        Button labsAdd = builder.buildTextButton(MT_LABS_ADD_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        Button labsRemove = builder.buildTextButton(MT_LABS_REMOVE_BUTTON, null, CLASS_SITE_BUTTON, ENABLED);
        labsAdd.setText("+");
        labsRemove.setText("-");
        Label labsLabel = builder.buildLabel(MT_LABS_LABEL, null, CLASS_SITE_BOLD_LABEL, ENABLED);
        labsInfo.setHgap(5);
        labsInfo.add(labsAdd, 0, 0);
        labsInfo.add(labsRemove, 1, 0);
        labsInfo.add(labsLabel, 2, 0);
        
        TableView<Lab> labsTable = builder.buildTableView(MT_LABS_TABLE_VIEW, labsPane, CLASS_OH_TABLE_VIEW, ENABLED);
        TableColumn labsSectionColumn = builder.buildTableColumn(MT_LABS_SECTION_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsDaysColumn = builder.buildTableColumn(MT_LABS_DAYS_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsRoomColumn = builder.buildTableColumn(MT_LABS_ROOM_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsTA1Column = builder.buildTableColumn(MT_LABS_TA1_COLUMN, labsTable, CLASS_OH_COLUMN);
        TableColumn labsTA2Column = builder.buildTableColumn(MT_LABS_TA2_COLUMN, labsTable, CLASS_OH_COLUMN);
        labsSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        labsDaysColumn.setCellValueFactory(new PropertyValueFactory<String, String>("daysTime"));
        labsRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        labsTA1Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta1"));
        labsTA2Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta2"));
        labsSectionColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 5.0));
        labsDaysColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 5.0));
        labsRoomColumn.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 5.0));
        labsTA1Column.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 5.0));
        labsTA2Column.prefWidthProperty().bind(labsTable.widthProperty().multiply(1.0 / 5.0));
        labsTable.setEditable(true);
        labsSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labsDaysColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labsRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labsTA1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        labsTA2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        
    }
}
