package com.shaman.mypassj.controllers;

import com.shaman.mypassj.db.DataFile;
import com.shaman.mypassj.db.MyPassjGroup;
import com.shaman.mypassj.db.MyPassjGroups;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @FXML
    void addChildItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            MyPassjGroup group = new MyPassjGroup();
            group.New(DataFile.getIdCounter(), item.getValue().getId(), "New Item", item.getValue().getLevel() + 1, 0);
            TreeItem<MyPassjGroup> newItem = new TreeItem<>(group);
            item.getChildren().add(newItem);
            item.setExpanded(true);
        }
    }

    @FXML
    void addItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            if (item != treeViewMain.getRoot()) {
                MyPassjGroup group = new MyPassjGroup();
                group.New(DataFile.getIdCounter(), item.getValue().getParentid(), "New Item", item.getValue().getLevel(), 0);
                TreeItem<MyPassjGroup> newItem = new TreeItem<>(group);
                item.getParent().getChildren().add(newItem);
            }
        }
    }

    @FXML
    void deleteItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<?> item = treeViewMain.getSelectionModel().getSelectedItem();
            if (item != treeViewMain.getRoot()) {
                treeViewMain.getSelectionModel().getSelectedItem().getParent().getChildren().remove(item);
            }
        }
    }

    @FXML
    void editItem() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {

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
    public void initialize() {
        treeViewMain.setRoot(MyPassjGroups.buildTreeItem());
/*  editItem  --- TreeItem must be String
        treeViewMain.setCellFactory(TextFieldTreeCell.forTreeView());
        treeViewMain.setEditable(true);
 */
        treeViewMain.getSelectionModel().selectedItemProperty().addListener(MainController::changed);

    }
}

