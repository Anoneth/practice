package com.practice.controller;

import com.practice.Vk.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class ShortUserInfoFormController {
    @FXML
    private Label fullName;
    @FXML
    private Label bdate;
    @FXML
    private ImageView photo100;

    public void fillForm(User user) {
        photo100.setImage(new Image(user.getPhoto100()));
        if (user.isDeactivated())
            fullName.setText("Страница неактивна, причина: " + user.getDeactivatedReason());
        else {
            fullName.setText(user.getFullname());
            bdate.setText("Дата рождения: " + user.getBdate());
        }
    }
}
