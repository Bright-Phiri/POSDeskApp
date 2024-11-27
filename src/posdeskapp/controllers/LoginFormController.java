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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip closeApp = new Tooltip("Sign out");
        closeApp.setStyle("-fx-font-size:11");
        closeApp.setMinSize(20, 20);
        Tooltip.install(this.closeApp, closeApp);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void signIn(KeyEvent event) {
    }

    @FXML
    private void login(ActionEvent event) {
        POSHelper helper = new POSHelper();
        helper.showUserStage(username, "/posdeskapp/views/Main.fxml");
    }

    @FXML
    private void loadForgotPasswordPanel(MouseEvent event) {
    }

}
