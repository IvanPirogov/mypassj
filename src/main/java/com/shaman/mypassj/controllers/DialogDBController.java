package com.shaman.mypassj.controllers;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.shaman.mypassj.db.MyPassjSetting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class DialogDBController {

    private String pathname = null;
    private String dbname = null;
    private String password = null;

    public String getPathname() {
        return pathname;
    }

    public String getDbname() {
        return dbname;
    }

    public String getPassword() {
        return password;
    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button openButton;

    @FXML
    private Button createButton;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView imgDB;

    @FXML
    void clickCloseButton() {
        Platform.exit();
        System.exit(0);
    }

    private boolean validateForm(String namedb, String passw1, String passw2){
        return (!namedb.isEmpty() && !passw1.isEmpty() && passw1.equals(passw2));
    }

    private void DialogPassword() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Create a new DB");
        dialog.setHeaderText("Enter DB name and password");
        try {
            dialog.setGraphic(new ImageView(new Image("forms/pics/databaseLock44.png")));
        }catch (Exception e){
            e.printStackTrace();
        }

        ButtonType okButtonType = new ButtonType("ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, okButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField dbNameTextField = new TextField();
        dbNameTextField.setPromptText("DB name");
        PasswordField passwordPasswordField = new PasswordField();
        passwordPasswordField.setPromptText("Password");
        PasswordField confirmPasswordPasswordField = new PasswordField();
        confirmPasswordPasswordField.setPromptText("Confirm password");

        Label labelNameDB = new Label("isEmpty");
        labelNameDB.setTextFill(Color.web("#d00"));
        Label labelPassword = new Label("isEmpty");
        labelPassword.setTextFill(Color.web("#d00"));
        Label labelConfirmPassword = new Label("notMatch");
        labelConfirmPassword.setTextFill(Color.web("#d00"));

        grid.add(new Label("DB name:"), 0, 0);
        grid.add(dbNameTextField, 1, 0);
        grid.add(labelNameDB, 2, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordPasswordField, 1, 1);
        grid.add(labelPassword, 2, 1);
        grid.add(new Label("Confirm password:"), 0, 2);
        grid.add(confirmPasswordPasswordField, 1, 2);
        grid.add(labelConfirmPassword, 2, 2);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        dbNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            labelNameDB.setVisible(newValue.trim().isEmpty());
            okButton.setDisable(!validateForm(newValue.trim(), passwordPasswordField.getText().trim(), confirmPasswordPasswordField.getText().trim()));
        });

        passwordPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            labelPassword.setVisible(newValue.trim().isEmpty());
            labelConfirmPassword.setVisible(!newValue.trim().equals(confirmPasswordPasswordField.getText().trim()));
            okButton.setDisable(!validateForm(dbNameTextField.getText().trim(), newValue.trim(), confirmPasswordPasswordField.getText().trim()));
        });

        confirmPasswordPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            labelConfirmPassword.setVisible(!newValue.trim().equals(passwordPasswordField.getText().trim()));
            okButton.setDisable(!validateForm(dbNameTextField.getText().trim(), passwordPasswordField.getText().trim(), newValue.trim()));
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> dbNameTextField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(dbNameTextField.getText(), passwordPasswordField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(dbNamePassword -> {
            password =  dbNamePassword.getValue();
            MyPassjSetting.setDbName(dbNamePassword.getKey());
            MyPassjSetting.setDbPath(pathname);
        });
    }

    public void createDB(Window window){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a directory");
        File selectedDirectory = chooser.showDialog(window);
        pathname = selectedDirectory.getPath();
        DialogPassword();
    }
    @FXML
    void clickCreateButton() {
        createDB(openButton.getScene().getWindow());
        ((Stage) closeButton.getScene().getWindow()).close();

    }

    public void openDB(Window window){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open DB");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DB files (*.mpj)", "*.mpj"));
        //new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            MyPassjSetting.setDbPath(file.getParent());
            MyPassjSetting.setDbName(file.getName().substring(0,file.getName().length() - 4 ));
            pathname = file.getParent();
            dbname = file.getName().substring(0,file.getName().length() - 4 );
            password = null;

        }
    }
    @FXML
    void clickOpenButton() {
        openDB(openButton.getScene().getWindow());
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    void initialize() {
        try {
            imgDB.setImage(new Image("forms/pics/db.png"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
