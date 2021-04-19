package com.shaman.mypassj;

import com.shaman.mypassj.db.DataFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainApp extends Application {




    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Password Keep");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
        DataFile.dataFile.SaveDataFile();
    }
}
