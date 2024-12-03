/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import posdeskapp.models.POS;
import posdeskapp.models.Platform;
import posdeskapp.models.TerminalEnvironment;
import posdeskapp.models.TerminalRuntimeEnvironment;
import posdeskapp.models.UnActivatedTerminal;
import posdeskapp.utils.Alert;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class TerminalActivationController implements Initializable {

    @FXML
    private FontAwesomeIconView closeApp;
    @FXML
    private JFXButton activateButton;
    @FXML
    private Text activationStatus;
    @FXML
    private JFXTextField terminalActivationCodeTextField;

    /**
     * Initializes the controller class.
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
    private void activateTerminal(ActionEvent event) {
        String terminalActionCode = terminalActivationCodeTextField.getText().trim();
        if (terminalActionCode.isEmpty()) {
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING, "Terminal Activation", "Please enter the terminal action code");
            return;
        }
        TerminalEnvironment terminalEnvironment = new TerminalEnvironment();

        UnActivatedTerminal terminalActivationRequest = new UnActivatedTerminal(
                terminalActionCode,
                new TerminalRuntimeEnvironment(
                        new Platform(
                                terminalEnvironment.getOsName(),
                                terminalEnvironment.getOsVersion(),
                                terminalEnvironment.getOsBuild(),
                                terminalEnvironment.getMacAddressValue()
                        ),
                        new POS("uwiueiewoeoe", "MRA_POINTOFSALE_V1")
                )
        );

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String terminalActivationPayload = gson.toJson(terminalActivationRequest);

        System.out.println(terminalActivationRequest);
    }

    @FXML
    private void handleTerminalActivation(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            activateTerminal(new ActionEvent());
        }
    }

}
