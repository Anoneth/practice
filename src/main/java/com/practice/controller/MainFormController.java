package com.practice.controller;

import com.practice.App;
import com.practice.Vk.Api;
import com.practice.Vk.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class MainFormController extends AnchorPane {

    private App main;
    private Api api;
    private ArrayList<Node> nodes;

    @FXML
    private Button backButton;
    @FXML
    private TextField textField;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane infoAnchorPane;
    @FXML
    private WebView webView;
    @FXML
    private Label mutualFriendsLabel;
    @FXML
    private Label errorLabel;

    public void setMain(App main) {
        this.main = main;
    }
    public void setApi(Api api) {
        this.api = api;
    }

    @FXML
    private void initialize() {
        nodes = new ArrayList<Node>();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(Api.AUTH_URL);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue))
                if (webEngine.getLocation().contains("access_token=")) {
                    changeView(-600);
                    main.setTitle("Введите индентификаторы");
                    api.parseToken(webEngine.getLocation());
                    webEngine.load("");
                    main.setWidth(300);
                }
        });
    }

    @FXML
    private void clickBackButton() {
        infoAnchorPane.getChildren().removeAll(nodes);
        changeView(-600);
        main.setWidth(300);
    }

    @FXML
    private void handleButton() throws Exception {
        if (check()) {
            main.setTitle("Результат");
            changeView(-900);
            main.setWidth(910);
            String[] ids = textField.getText().split(",");
            User[] users;
            if (ids.length == 1) users = api.getUsersInfo(textField.getText()+ "," + api.getOwnerId(), true);
            else users = api.getUsersInfo(textField.getText(), true);
            if (users == null || users.length == 0) {
                clickBackButton();
                errorLabel.setText("Неправильные идентификаторы");
            } else {
                double x = 0;
                for (User user : users) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/UserInfoForm.fxml"));
                    Node node = loader.load();
                    node.setLayoutX(x);
                    x += 300;
                    ((UserInfoFormController) loader.getController()).fillForm(user);
                    infoAnchorPane.getChildren().add(node);
                    nodes.add(node);
                }
                mutualFriendsLabel.setLayoutX(x);
                if (users.length == 1) {
                    if (ids.length == 2 && ids[0].equals(ids[1]))
                        fillMutualFriends(api.getMutualFriends(users[0].getId(), users[0].getId()));
                    else {
                        clickBackButton();
                        errorLabel.setText("Один из идентификаторов неправильный");
                    }
                } else fillMutualFriends(api.getMutualFriends(users[0].getId(), users[1].getId()));
            }
        }
    }

    private boolean check() {
        errorLabel.setText("");
        String input = textField.getText();
        String[] strings = input.split(",");
        for (String str : strings)
            System.out.println(str);
        if (strings.length > 2) {
            errorLabel.setText("Только 2 идентификатора");
            return false;
        } if (strings.length == 2 && (strings[0].isEmpty() || strings[1].isEmpty())) {
            errorLabel.setText("Идентификатор не может быть пустым");
            return false;
        }
        return true;
    }

    private void fillMutualFriends(User[] users) throws Exception {
        double x = mutualFriendsLabel.getLayoutX();
        double y = mutualFriendsLabel.getLayoutY() + 27;
        mutualFriendsLabel.setText("Общие друзья: " + users.length);
        int i = 0;
        for (User user : users) {
            System.out.println(i++);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ShortUserInfoForm.fxml"));
            Node node = loader.load();
            node.setLayoutX(x);
            node.setLayoutY(y);
            y += 104;
            ((ShortUserInfoFormController) loader.getController()).fillForm(user);
            infoAnchorPane.getChildren().add(node);
            nodes.add(node);
        }
    }
    private void changeView(double endValue) {
        Duration duration = new Duration(500);
        Timeline timeline = new Timeline(
        new KeyFrame(duration, new KeyValue(anchorPane.layoutXProperty(), endValue))
        );
        timeline.play();
    }
}
