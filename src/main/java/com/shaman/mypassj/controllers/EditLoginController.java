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

import javax.sound.sampled.Port;

public class EditLoginController {

    private int resultCode  = -1; // -1 - error, 0 - press cancelButton, 1 - press okButton

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameLoginTextField;

    @FXML
    private TextField sourceLoginTextField;

    @FXML
    private TextField portLoginTextField;

    @FXML
    private TextField loginLoginTextField;

    @FXML
    private TextField passwordLoginTextField;

    @FXML
    private TextArea memoLoginTextArea;

    @FXML
    private Label createddtLabel;

    @FXML
    private Label updateddtLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    void clickCancelButton() {
        resultCode = 0;
        closeWindow(cancelButton);
    }

    @FXML
    void clickOkButton() {
        resultCode = 1;
        closeWindow(okButton);
    }

    private void closeWindow(Button btn){
        ((Stage) btn.getScene().getWindow()).close();
    }

    @FXML
    void initialize(String name, String source, String login, String password, String port, String memo, Date createddt, Date updateddt) {
        nameLoginTextField.setText(name);
        sourceLoginTextField.setText(source);
        SimpleDateFormat dateFor = new SimpleDateFormat("MMM dd yyyy hh:mm:ss a");
        createddtLabel.setText("Created date:  " + dateFor.format(createddt));
        updateddtLabel.setText("Updated date: " + dateFor.format(updateddt));
        sourceLoginTextField.setText(source);
        loginLoginTextField.setText(login);
        passwordLoginTextField.setText(password);
        portLoginTextField.setText(port);
    }
    int getResultCode(){
        return resultCode;
    }
    String getName(){ return nameLoginTextField.getText().trim();}
    String getSource(){ return sourceLoginTextField.getText().trim();}
    String getLogin(){ return loginLoginTextField.getText().trim();}
    String getPassword(){ return passwordLoginTextField.getText().trim();}
    String getPort(){ return portLoginTextField.getText().trim();}
    String getMemo(){ return memoLoginTextArea.getText().trim();}

}

