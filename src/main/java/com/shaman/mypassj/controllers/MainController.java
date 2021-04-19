package com.shaman.mypassj.controllers;

import com.shaman.mypassj.MainApp;
import com.shaman.mypassj.db.*;
import com.sun.javafx.menu.MenuItemBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    private Long noteid = null;
    private Long loginid = null;
    private Long groupid = null;

    private ObservableList<MyPassjLogin> loginData = FXCollections.observableArrayList();

    @FXML
    private TreeView<MyPassjGroup> treeViewMain;


    @FXML
    private MenuItem addChildContextMenuTreeViewMain;

    @FXML
    private MenuItem addItemContextMenuTreeViewMain;

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
    private ScrollPane noteScrollPane;

    @FXML
    private Button noteAddButton;

    @FXML
    private Button editNodeButton;

    @FXML
    private Button deleteNoteButton;


    @FXML
    private TilePane noteTilePane;

    private void showNotes(Long groupid){
        List<MyPassjNote> listNote = MyPassjNotes.getMyPassjNotes().stream()
                .filter(note -> note.getGroupid() == groupid)
                .sorted(Comparator.comparing(MyPassjNote::getUpdateddt).reversed())
                .collect(Collectors.toList());
        noteTilePane.getChildren().clear();
        for (MyPassjNote note : listNote){ showNote(note); }
        disableNoteButtons();
    }

    private void changed(ObservableValue<? extends TreeItem<MyPassjGroup>> v, TreeItem<MyPassjGroup> oldValue, TreeItem<MyPassjGroup> newValue) {
        if (newValue != null) {
            groupid = newValue.getValue().getId();
            showNotes(newValue.getValue().getId());
            showLogins(newValue.getValue().getId());
            noteid = null;
            loginid = null;
            showLogins(groupid);
            disableNoteButtons();
        }
    }

    private String callEditGroupForm(String path, String group)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/EditGroup.fxml"));
            Scene sceneEditGroup = new Scene(fxmlLoader.load());
            EditGroupController controller = (EditGroupController) fxmlLoader.getController();
            controller.initialize(path,group);
            Stage stageEditGroup = new Stage();
            stageEditGroup.setTitle("Edit the group");
            stageEditGroup.setScene(sceneEditGroup);
            stageEditGroup.initModality(Modality.APPLICATION_MODAL);
            stageEditGroup.showAndWait();
            return controller.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String buildFullGroupPath(TreeItem<MyPassjGroup> item){
        TreeItem<MyPassjGroup> parent = item.getParent();
        if (parent == null) return "";
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
                treeViewMain.getSelectionModel().select(newItem);
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
                    treeViewMain.getSelectionModel().select(newItem);
                }
            }
        }
    }

    @FXML
    void deleteItem() {
//        ((Stage) treeViewMain.getScene().getWindow()).close();
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            TreeItem<MyPassjGroup> item = treeViewMain.getSelectionModel().getSelectedItem();
            if (item != treeViewMain.getRoot()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete the group");
                String s = "\nAll data of this group will be deleted.";
                s = s + "\n\nDelete the group '" + item.getValue().toString() + "'?";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    TreeItem<MyPassjGroup> selectItem =  item.getParent();;
                    if (item.previousSibling().getValue() != null) {selectItem = item.previousSibling();
                    } else if (item.nextSibling() != null) {selectItem = item.nextSibling();}
                    treeViewMain.getSelectionModel().getSelectedItem().getParent().getChildren().remove(item);
                    MyPassjGroups.saveGroups(treeViewMain.getRoot());
                    treeViewMain.getSelectionModel().select(selectItem);

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
        addChildContextMenuTreeViewMain.setDisable(true);
        deleteContextMenuTreeViewMain.setDisable(true);
        editContextMenuTreeViewMain.setDisable(true);
        addItemContextMenuTreeViewMain.setDisable(true);
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            addChildContextMenuTreeViewMain.setDisable(false);
            if (treeViewMain.getSelectionModel().getSelectedItem() != treeViewMain.getRoot()) {
                addItemContextMenuTreeViewMain.setDisable(false);
                deleteContextMenuTreeViewMain.setDisable(false);
                editContextMenuTreeViewMain.setDisable(false);
            }
        }
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

    @FXML
    private ScrollPane loginScrollPane;

    @FXML
    private AnchorPane loginAnchorPane;

    private MyPassjNote callEditNoteForm(Long id, Long groupid, String name, String body, Date createddt, Date updateddt)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/EditNote.fxml"));
            Scene sceneEditNote = new Scene(fxmlLoader.load());
            EditNoteController controller = (EditNoteController) fxmlLoader.getController();
            controller.initialize(name,body,createddt,updateddt);
            Stage stageEditGroup = new Stage();
            stageEditGroup.setTitle("Edit the note");
            stageEditGroup.setScene(sceneEditNote);
            stageEditGroup.initModality(Modality.APPLICATION_MODAL);
            stageEditGroup.showAndWait();
            if (controller.getResultCode() == 1){
                MyPassjNote note = new MyPassjNote();
                if (id < 0) id = MyPassjSetting.getIdCounter("NOTE");
                note.New(id, groupid, controller.getName(),controller.getBody());
                note.setCreateddt(controller.getCreateddt());
                note.setUpdateddt(new Date());
                return note;
            } else {return null;}
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void addNote() {

    }

    @FXML
    void deleteNote() {

    }

    @FXML
    void editNote() {

    }

    private void showNote(MyPassjNote note) {
        SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String txtBody = note.getBody().lines().limit(20).collect(Collectors.joining ());
        if (txtBody.length() > 380) {
            txtBody = txtBody.substring(0,380);
        }
        String txt = note.getName();
        txt += "\n  ________________________________\n\n";
        txt += "\nUpdated: " + dateFor.format(note.getUpdateddt());
        txt += " Created: " + dateFor.format(note.getCreateddt());
        txt += "\n  ________________________________\n\n";
        txt += txtBody;
        TextArea noteText = new TextArea();
        noteText.setId("node" + note.getId());
        noteText.setPrefWidth(250);
        noteText.setPrefHeight(380);
        noteText.setText(txt);
        noteText.setWrapText(true);
        noteText.setEditable(false);
        noteText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    noteid = note.getId();
                    disableNoteButtons();
                    if(mouseEvent.getClickCount() == 2){
                        clickEditNode();
                    }
                }
            }
        });

        noteTilePane.getChildren().add(noteText);
    }

    private void disableNoteButtons(){
        noteAddButton.setDisable(groupid == null);
        editNodeButton.setDisable(noteid == null);
        deleteNoteButton.setDisable(noteid == null);
    }
    private void disableLoginButtons(){
        loginAddButton.setDisable(groupid == null);
        editLoginButton.setDisable(loginid == null);
        deleteLoginButton.setDisable(loginid == null);
        copyLoginPopMenu.setDisable(groupid == null);
        copyPasswordPopMenu.setDisable(groupid == null);

    }

    private void openDialogDB(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/DialogDB.fxml"));
            Scene sceneEditNote = null;
            sceneEditNote = new Scene(fxmlLoader.load());
            DialogDBController controller = (DialogDBController) fxmlLoader.getController();
            controller.initialize();
            Stage stageEditGroup = new Stage();
            stageEditGroup.setTitle("Dialog DB");
            stageEditGroup.setScene(sceneEditNote);
            stageEditGroup.initModality(Modality.APPLICATION_MODAL);
            stageEditGroup.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        String password = "Pasport";
        String path = "/home/shaman/tmp/";
        String dbname = "mypassj";
        if (MyPassjSetting.readOuterSettings() == 1){
            openDialogDB();
        }
        DataFile.dataFile = new DataFile(dbname, path, password);
//        DataFile.dataFile.readData()
//        DataFile.dataFile.CreateDatafile();
        DataFile.dataFile.OpenDatafile();

        try {
            noteAddButton.setGraphic(new ImageView(new Image("forms/pics/noteAdd.png")));
            editNodeButton.setGraphic(new ImageView(new Image("forms/pics/noteEdit.png")));
            deleteNoteButton.setGraphic(new ImageView(new Image("forms/pics/noteDelete.png")));
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            loginAddButton.setGraphic(new ImageView(new Image("forms/pics/loginAdd.png")));
            editLoginButton.setGraphic(new ImageView(new Image("forms/pics/loginEdit.png")));
            deleteLoginButton.setGraphic(new ImageView(new Image("forms/pics/loginDelete.png")));
        }catch (Exception e){
            e.printStackTrace();
        }

        disableNoteButtons();
        MyPassjNotes.readNotesFromDB();
        MyPassjLogins.readLoginsFromDB();
        treeViewMain.setRoot(MyPassjGroups.buildTreeItem());
        treeViewMain.getSelectionModel().selectedItemProperty().addListener(this::changed);
        treeViewMain.getSelectionModel().select(treeViewMain.getRoot());

        ChangeListener<Number> noteSizeListener = this::changedNoteSize;
        noteScrollPane.widthProperty().addListener(noteSizeListener);
        noteScrollPane.heightProperty().addListener(noteSizeListener);

        loginTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loginid = newSelection.getId();
                disableLoginButtons();
            }
        });

        nameLoginTable.setCellValueFactory(new PropertyValueFactory<MyPassjLogin, String>("name"));
        sourceLoginTable.setCellValueFactory(new PropertyValueFactory<MyPassjLogin, String>("source"));
        loginLoginTable.setCellValueFactory(new PropertyValueFactory<MyPassjLogin, String>("login"));
        passwordLoginTable.setCellValueFactory(new PropertyValueFactory<MyPassjLogin, String>("password"));

        loginTable.setItems(loginData);
        disableLoginButtons();
    }

    @FXML
    void clickAddNote() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            MyPassjNote note = callEditNoteForm(-1L,groupid,"","", new Date(), new Date());
            if ( note != null) {
                MyPassjNotes.addNote(note);
                showNotes(groupid);
                noteid = null;
                disableNoteButtons();
            }
        }
    }

    @FXML
    void clickEditNode() {
        if (noteid != null) {
            MyPassjNote note = MyPassjNotes.getNode(noteid);
            if (note != null) {
                note = callEditNoteForm(noteid, groupid, note.getName(), note.getBody(), note.getCreateddt(), note.getUpdateddt());
                if (note !=null) {
                    MyPassjNotes.editNote(note);
                    showNotes(groupid);
                }
            }
        }
    }

    @FXML
    void clickDeleteNote(){
        if (noteid != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete the note");
            String  s = "Delete this note ?";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                MyPassjNotes.deleteNote(noteid);
                showNotes(groupid);
                noteid = null;
                disableNoteButtons();
            }
        }
    }

    @FXML
    void clickNoteTilePine(){
        noteid = null;
        disableNoteButtons();
    }

    private void changedNoteSize(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        noteTilePane.setMinWidth(noteScrollPane.getWidth() - 25);
    }

    @FXML
    private Button loginAddButton;

    @FXML
    private Button editLoginButton;

    @FXML
    private Button deleteLoginButton;

    @FXML
    private TableView<MyPassjLogin> loginTable;

    @FXML
    private TableColumn<MyPassjLogin, String> nameLoginTable;

    @FXML
    private TableColumn<MyPassjLogin, String> sourceLoginTable;

    @FXML
    private TableColumn<MyPassjLogin, String> loginLoginTable;

    @FXML
    private TableColumn<MyPassjLogin, String> passwordLoginTable;

    @FXML
    void clickAddLogin() {
        if (treeViewMain.getSelectionModel().getSelectedItem() != null) {
            MyPassjLogin login = callEditLoginForm(-1L,groupid,"","", "","","","",new Date(), new Date());
            if ( login != null) {
                MyPassjLogins.addLogin(login);
                showLogins(groupid);
                loginid = null;
                disableLoginButtons();

            }
        }

    }

    @FXML
    void clickDeleteLogin() {
        if (loginid != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete the login");
            String  s = "Delete this login ?";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                MyPassjLogins.deleteLogin(loginid);
                showLogins(groupid);
                loginid = null;
                disableLoginButtons();
            }
        }

    }

    @FXML
    void clickEditLogin() {
        MyPassjLogin login = loginTable.getSelectionModel().getSelectedItem();
        if (login != null) {
            login = callEditLoginForm(login.getId(), groupid, login.getName(), login.getSource(), login.getLogin()
                    , login.getPassword(), login.getPort(), login.getMemo(), login.getCreateddt(), login.getUpdateddt());
            if (login !=null) {
                MyPassjLogins.editLogin(login);
                showLogins(groupid);
                disableLoginButtons();
            }
        }
    }

    private MyPassjLogin callEditLoginForm(Long id, Long groupid, String name, String source, String login, String password, String port, String memo, Date createddt, Date updateddt)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/EditLogin.fxml"));
            Scene sceneEditLogin = new Scene(fxmlLoader.load());
            EditLoginController controller = (EditLoginController) fxmlLoader.getController();
            controller.initialize(name, source, login, password, port, memo, createddt, updateddt);
            Stage stageEditLogin = new Stage();
            stageEditLogin.setTitle("Edit the login");
            stageEditLogin.setScene(sceneEditLogin);
            stageEditLogin.initModality(Modality.APPLICATION_MODAL);
            stageEditLogin.showAndWait();
            if (controller.getResultCode() == 1){
                if (id < 0) id = MyPassjSetting.getIdCounter("LOGIN");
                MyPassjLogin newlogin = new MyPassjLogin(id, groupid, controller.getName(),controller.getSource()
                        , controller.getLogin(), controller.getPassword(), controller.getPort()
                        , controller.getMemo(), createddt, new Date());
                return newlogin;
            } else {return null;}
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void clickLoginTable(MouseEvent event) {
        if (loginid != null){
            if(event.getClickCount() == 2){
                clickEditLogin();
                disableLoginButtons();
            }
        }
    }

    private void showLogins(Long groupid){
        List<MyPassjLogin> listLogin = MyPassjLogins.getMyPassjLogins().stream()
                .filter(login -> login.getGroupid() == groupid)
                .sorted(Comparator.comparing(MyPassjLogin::getUpdateddt).reversed())
                .collect(Collectors.toList());
        loginTable.getItems().clear();
        for (MyPassjLogin login: listLogin){ loginData.add(login); }
        disableLoginButtons();
    }

    @FXML
    private MenuItem copyLoginPopMenu;

    @FXML
    private MenuItem copyPasswordPopMenu;

    @FXML
    void copyLogin() {
        MyPassjLogin login = loginTable.getSelectionModel().getSelectedItem();
        if (login != null) {
            StringSelection stringSelection = new StringSelection(login.getLogin());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    @FXML
    void copyPassword() {
            MyPassjLogin login = loginTable.getSelectionModel().getSelectedItem();
            if (login != null) {
                StringSelection stringSelection = new StringSelection(login.getPassword());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
        }
    }


}

