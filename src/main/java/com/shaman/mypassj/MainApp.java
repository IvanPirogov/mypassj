package com.shaman.mypassj;

import com.shaman.mypassj.db.MyPassjGroup;
import com.shaman.mypassj.db.MyPassjGroups;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/forms/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        TreeView treeViewMain = (TreeView) scene.lookup("#treeViewMain");
        treeViewMain.setRoot(MyPassjGroups.buildTreeItem());
        treeViewMain.setEditable(true);
        stage.setScene(scene);
        stage.show();

        MyPassjGroups.buildGroupObjects(treeViewMain.getRoot());
    }
    public static void main(String[] args) {
        launch(args);
    }
}
