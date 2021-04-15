package com.shaman.mypassj.controllers;

import java.net.URL;
        import java.util.ResourceBundle;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextArea;
        import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditNoteController {

    private String name = null;
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
    void initialize() {
        assert nameTextField != null : "fx:id=\"nameTextField\" was not injected: check your FXML file 'EditNote.fxml'.";
        assert bodyTextArea != null : "fx:id=\"bodyTextArea\" was not injected: check your FXML file 'EditNote.fxml'.";
        assert cancelButtonEditNote != null : "fx:id=\"cancelButtonEditNote\" was not injected: check your FXML file 'EditNote.fxml'.";
        assert okButtonEditNote != null : "fx:id=\"okButtonEditNote\" was not injected: check your FXML file 'EditNote.fxml'.";
        assert createddtLabel != null : "fx:id=\"createddtLabel\" was not injected: check your FXML file 'EditNote.fxml'.";
        assert updateddtLabel != null : "fx:id=\"updateddtLabel\" was not injected: check your FXML file 'EditNote.fxml'.";

    }
}
