package com.shaman.mypassj.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditNoteController {

    private Date dtCreated = null;
    private int resultCode  = -1; // -1 - error, 0 - press cancelButton, 1 - press okButton

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea bodyTextArea;

    @FXML
    private Button cancelButtonEditNote;

    @FXML
    private Button okButtonEditNote;

    @FXML
    private Label createddtLabel;

    @FXML
    private Label updateddtLabel;

    @FXML
    void cancelEditNote() {
        resultCode = 0;
        closeWindow(cancelButtonEditNote);

    }

    @FXML
    void okEditNote() {
        resultCode = 1;
        closeWindow(okButtonEditNote);

    }

    private void closeWindow(Button btn){
        ((Stage) btn.getScene().getWindow()).close();
    }

    @FXML
    void initialize(String name, String body, Date createddt, Date updatedt) {
        nameTextField.setText(name);
        bodyTextArea.setText(body);
        this.dtCreated = createddt;
        SimpleDateFormat dateFor = new SimpleDateFormat("MMM dd yyyy hh:mm:ss a");
        createddtLabel.setText("Created date:  " + dateFor.format(createddt));
        updateddtLabel.setText("Updated date: " + dateFor.format(updatedt));
    }
    int getResultCode(){
        return resultCode;
    }
    String getName(){ return nameTextField.getText().trim();}
    String getBody(){ return bodyTextArea.getText().trim();}
    Date getCreateddt(){ return this.dtCreated;}
}
