package com.practice;

import com.practice.Vk.Api;
import com.practice.controller.MainFormController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 */
public class App extends Application {

    private Stage pStage;

    public void start(Stage primaryStage) throws Exception {
        pStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(scene);
        primaryStage.show();

        MainFormController controller = loader.getController();
        controller.setMain(this);
        Api api = new Api();
        controller.setApi(api);
    }

    public void setWidth(double width) {
        pStage.setWidth(width);
    }

    public void setTitle(String title) {
        pStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
