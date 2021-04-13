package com.shaman.mypassj;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
//        MyPassjGroups.buildGroupObjects(treeViewMain.getRoot());
    }
    public static void main(String[] args) {
        launch(args);
    }
}
