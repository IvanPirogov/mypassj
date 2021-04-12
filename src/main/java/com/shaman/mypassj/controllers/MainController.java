package com.shaman.mypassj.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;

public class MainController {

    @FXML
    private TreeView<?> treeViewMain;

    @FXML
    private MenuBar menuBarMain;

    @FXML
    private TabPane tabPaneMain;

    @FXML
    private Tab tabNotes;

    @FXML
    private Tab tabLogins;

    @FXML
    private TableView<?> tableViewLogins;

}
