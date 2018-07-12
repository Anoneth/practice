package com.practice.controller;

import com.practice.Vk.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFormController extends AnchorPane {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label fullName;
    @FXML
    private Label bdate;
    @FXML
    private Label status;
    @FXML
    private Label sex;
    @FXML
    private Label online;

    @FXML
    private void inititalize() {

    }

    public void fillForm(User user) {
        imageView.setImage(new Image(user.getPhoto100()));
        if (user.isDeactivated())
            fullName.setText("Страница неактивна, причина: " + user.getDeactivatedReason());
        else {
            fullName.setText(user.getFullname());
            bdate.setText("Дата рождения: " + user.getBdate());
            status.setText("Статус: " + user.getStatus());
            sex.setText("Пол: " + user.getSex());
            if (user.isOnline()) online.setText("Онлайн");
            else online.setText("Онлайн " + user.getLastseen());
            fillOptional(user, online.getLayoutX(), online.getLayoutY() + 27);
        }
    }
    private void fillOptional(User user, double x, double y) {
        ArrayList<Label> labels = new ArrayList<Label>();
        Label label;
        if (user.getmPhone() != null) {
            label = new Label("Мобильный телефон: " + user.getmPhone());
            fillLayout(labels, label, x, y);
            y += 27;
        }
        if (user.getCountry() != null) {
            label = new Label("Страна: " + user.getCountry());
            fillLayout(labels, label, x, y);
            y += 27;
        }
        if (user.getCity() != null) {
            label = new Label("Город: " + user.getCity());
            fillLayout(labels, label, x, y);
            y += 27;
        }
        if (user.getOccupation() != null) {
            label = new Label("Текущая занятость: " + user.getOccupation());
            fillLayout(labels, label, x, y);
            y += 27;
        }
        if (user.getSite() != null) {
            label = new Label("Веб-сайт: " + user.getSite());
            fillLayout(labels, label, x, y);
            y += 27;
        }
        for (Label l : labels)
            anchorPane.getChildren().add(l);
    }
    private void fillLayout(List<Label> labels, Label label, double x, double y) {
        label.setLayoutY(y);
        label.setLayoutX(x);
        labels.add(label);
    }
}
