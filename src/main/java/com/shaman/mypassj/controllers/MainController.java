package com.shaman.mypassj.controllers;

import com.shaman.mypassj.MainApp;
import com.shaman.mypassj.db.DataFile;
import com.shaman.mypassj.db.MyPassjGroup;
import com.shaman.mypassj.db.MyPassjGroups;
import com.shaman.mypassj.db.MyPassjSetting;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MainController {

    @FXML
    private TreeView<MyPassjGroup> treeViewMain;


    @FXML
    private MenuItem addChildContextMenuTreeViewMain;

    @FXML
    private MenuItem editContextMenuTreeViewMain;

    @FXML
    private MenuItem deleteContextMenuTreeViewMain;

    @FXML
    private MenuBar menuBarMain;

    @FXML
    private Tab tabNotes;

    @FXML
    private Tab tabLogins;

    @FXML
    private TableView<?> tableViewLogins;

    private static void changed(ObservableValue<? extends TreeItem<MyPassjGroup>> v, TreeItem<MyPassjGroup> oldValue, TreeItem<MyPassjGroup> newValue) {
        if (newValue != null) System.out.println(newValue.getValue().getName());
    }

    private String callEditGroupForm(String path, String group)  {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/EditGroup.fxml"));
        Scene sceneEditGroup = null;
        try {
            sceneEditGroup = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditGroupController controller = fxmlLoader.getController();
        controller.initialize(path,group);
        Stage stageEditGroup = new Stage();
        stageEditGroup.setTitle("Edit the group");
        stageEditGroup.setScene(sceneEditGroup);
        stageEditGroup.initModality(Modality.APPLICATION_MODAL);
        stageEditGroup.showAndWait();
        return controller.getName();
    }

    private String buildFullGroupPath(TreeItem<MyPassjGroup> item){
        TreeItem<MyPassjGroup> parent = item.getParent();
        String path = parent.getValue().getName();
        parent = parent.getParent();
        while(parent != null) {
            path = parent.getValue().getName() + "/" + path;
            parent = parent.getParent();
        }
        return path + "/";
    }

    @FXML
    void addChildItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            String result = callEditGroupForm(buildFullGroupPath(item) + item.getValue().getName() + "/", "");
            if ( result != null) {
                MyPassjGroup group = new MyPassjGroup();
                group.New(MyPassjSetting.getIdCounter("GROUP"), item.getValue().getId(), result, item.getValue().getLevel() + 1, 0);
                TreeItem<MyPassjGroup> newItem = new TreeItem<>(group);
                item.getChildren().add(newItem);
                item.setExpanded(true);
                MyPassjGroups.saveGroups(treeViewMain.getRoot());
            }
        }
    }

    @FXML
    void addItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            if (item != treeViewMain.getRoot()) {
                String result = callEditGroupForm(buildFullGroupPath(item), "");
                if ( result != null) {
                    MyPassjGroup group = new MyPassjGroup();
                    group.New(MyPassjSetting.getIdCounter("GROUP"), item.getValue().getParentid(), result, item.getValue().getLevel(), 0);
                    TreeItem<MyPassjGroup> newItem = new TreeItem<>(group);
                    item.getParent().getChildren().add(newItem);
                    MyPassjGroups.saveGroups(treeViewMain.getRoot());
                }
            }
        }
    }

    @FXML
    void deleteItem() {
//        ((Stage) treeViewMain.getScene().getWindow()).close();
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<?> item = treeViewMain.getSelectionModel().getSelectedItem();
            if (item != treeViewMain.getRoot()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete the group");
                String s = "\nAll data of this group will be deleted.";
                s = s + "\n\nDelete the group '" + item.getValue().toString() + "'?";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    treeViewMain.getSelectionModel().getSelectedItem().getParent().getChildren().remove(item);
                    MyPassjGroups.saveGroups(treeViewMain.getRoot());
                }
            }
        }
    }

    @FXML
    void editItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            String result = callEditGroupForm(buildFullGroupPath(item), item.getValue().getName());
            if ( result != null){
                item.getValue().setName(result);
                treeViewMain.refresh();
                MyPassjGroups.saveGroups(treeViewMain.getRoot());
            }
        }
    }

    @FXML
    void showingPopMenu() {
        boolean sel = (treeViewMain.getSelectionModel().getSelectedItem() == null);
        addChildContextMenuTreeViewMain.setDisable(sel);
        deleteContextMenuTreeViewMain.setDisable(sel);
        editContextMenuTreeViewMain.setDisable(sel);
    }

    @FXML
    void shownPopMenu() {

    }

    @FXML
    void clickTreeViewMain(MouseEvent event) {
        if(event.getClickCount() == 2){
            editItem();
        }
    }

    private String callEditNoteForm(String name, String body, LocalDateTime createddt, LocalDateTime updateddt)  {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/EditNote.fxml"));
        Scene sceneEditNote = null;
        try {
            sceneEditNote = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditNoteController controller = fxmlLoader.getController();
        controller.initialize();
        Stage stageEditGroup = new Stage();
        stageEditGroup.setTitle("Edit the note");
        stageEditGroup.setScene(sceneEditNote);
        stageEditGroup.initModality(Modality.APPLICATION_MODAL);
        stageEditGroup.showAndWait();
        return null;
    }

    @FXML
    void addNote() {
        callEditNoteForm("","", LocalDateTime.now(), LocalDateTime.now());
    }

    @FXML
    void deleteNote() {

    }

    @FXML
    void editNote() {

    }


    @FXML
    public void initialize() {
        treeViewMain.setRoot(MyPassjGroups.buildTreeItem());
        treeViewMain.getSelectionModel().selectedItemProperty().addListener(MainController::changed);



    }
}

