/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class TerminalActivationController implements Initializable {

    @FXML
    private FontAwesomeIconView closeApp;
    @FXML
    private JFXTextField terminalActivationCode;
    @FXML
    private JFXButton activateButton;
    @FXML
    private Text activationStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeApp(MouseEvent event) {
    }

    @FXML
    private void activateTerminal(ActionEvent event) {
    }

    @FXML
    private void handleTerminalActivation(KeyEvent event) {
    }
    
}
