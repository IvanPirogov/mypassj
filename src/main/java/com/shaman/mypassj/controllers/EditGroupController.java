package com.shaman.mypassj.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EditGroupController {

    private String name = null;
    private int resultCode  = -1; // -1 - error, 0 - press cancelButton, 1 - press okButton

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label pathLabel;

    @FXML
    private TextField nameTextEdit;


    @FXML
    void cancelGroup() {
        resultCode = 0;
        closeWindow(cancelButton);
    }

    @FXML
    void okGrroup() {
        name = nameTextEdit.getText();
        resultCode = 1;
        closeWindow(okButton);
    }

    private void closeWindow(Button btn){
        ((Stage) btn.getScene().getWindow()).close();
    }


    @FXML
    void initialize(String path, String group){
        pathLabel.setText(path);
        nameTextEdit.setText(group);
        nameTextEdit.requestFocus();
        nameTextEdit.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                okButton.fire();
                ev.consume();
            }
            if (ev.getCode() == KeyCode.ESCAPE) {
                cancelButton.fire();
                ev.consume();
            }
        });

    }

     String getName(){
        return name;
    }

    int getResultCode(){
        return resultCode;
    }


}

