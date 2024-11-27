/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import posdeskapp.utils.Alert;
import posdeskapp.utils.POSHelper;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class LoginFormController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton loginButton;
    @FXML
    private FontAwesomeIconView closeApp;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip close = new Tooltip("Close Application");
        close.setStyle("-fx-font-size:11");
        close.setMinSize(20, 20);
        Tooltip.install(this.closeApp, close);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void signIn(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login(new ActionEvent());
        }
    }

    @FXML
    private void login(ActionEvent event) {
        if (validateFields()) {
            if (POSHelper.userSignIn(username.getText().trim(), POSHelper.encryptPassword(password.getText().trim()))) {
                POSHelper helper = new POSHelper();
                switch (POSHelper.USERTYPE) {
                    case "ADMIN":
                        helper.showUserStage(username, "/posdeskapp/views/Main.fxml");
                        break;
                    case "CASHIER":
                        helper.showUserStage(username, "/posdeskapp/views/Main.fxml");
                        break;
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Login", "Invalid username or password");
                clearFields();
            }
        }
    }

    @FXML
    private void loadForgotPasswordPanel(MouseEvent event) {
    }

    private Boolean validateFields() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING, "Fields validation", "Please enter in all fields");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void clearFields() {
        username.clear();
        password.clear();
    }

}
