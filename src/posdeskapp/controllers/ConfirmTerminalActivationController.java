/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import posdeskapp.api.ApiClient;
import posdeskapp.api.ApiResponse;
import posdeskapp.api.HttpResponseResult;
import posdeskapp.utils.Alert;
import posdeskapp.utils.DbHelper;
import posdeskapp.utils.POSHelper;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class ConfirmTerminalActivationController implements Initializable {

    @FXML
    private FontAwesomeIconView closeApp;
    @FXML
    private Text activationStatus;
    @FXML
    private JFXButton confirmActivateButton;

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
    private void confirmaTerminalActivation(ActionEvent event) {
        String terminalId = DbHelper.fetchTerminalId();
        String activationCode = DbHelper.fetchActivationCode();
        String secretKey = DbHelper.fetchTerminalSecretKey();
        String xSignature = POSHelper.computeXSignature(activationCode, secretKey);

        Map<String, String> confirmActivation = new HashMap<>();
        confirmActivation.put("terminalId", terminalId);

        Gson gSon = new Gson();
        String confirmTerminalActivationPayload = gSon.toJson(confirmActivation);
        HttpResponseResult httpResponseResult = ApiClient.confirmTerminalActivation(confirmTerminalActivationPayload, xSignature);

        Gson gson = new Gson();

        ApiResponse<Boolean> response = gson.fromJson(httpResponseResult.getResponseBody(), new TypeToken<ApiResponse<Boolean>>() {
        }.getType());
        if (httpResponseResult.getStatusCode() == 200 && response.getStatusCode() == 1) {
            if (DbHelper.activateTerminalConfiguration()) {
                javafx.scene.control.Alert activatedalert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                activatedalert.setHeaderText(null);
                activatedalert.setContentText("Terminal is now fully activated and ready for use!");
                Optional<ButtonType> activatedalertOption = activatedalert.showAndWait();
                if (activatedalertOption.get() == ButtonType.OK) {
                    String siteId = DbHelper.fetchTerminalSiteId();
                    //Download terminal site products
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/posdeskapp/views/LoginForm.fxml"));
                        Parent parent = loader.load();
                        LoginFormController controller = (LoginFormController) loader.getController();
                        LoginController.root.setRight(parent);
                    } catch (IOException ex) {
                        Logger.getLogger(ConfirmTerminalActivationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //Download site products still
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Terminal activation", "Failed to fully activate the terminal");
            }
        } else {
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Confirma terminal activation", response.getRemark());
        }
    }

}
