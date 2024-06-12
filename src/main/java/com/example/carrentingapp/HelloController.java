package com.example.carrentingapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label loginLabel, registerLabel;
    @FXML
    private Button loginButton, registerButton;

    @FXML
    protected void test() {
        System.out.println("elo");
        loginLabel.setVisible(false);
        registerLabel.setVisible(true);
        loginButton.setVisible(false);
        registerButton.setVisible(true);
    }
}