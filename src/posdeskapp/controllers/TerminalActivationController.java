/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import posdeskapp.api.ApiClient;
import posdeskapp.api.ApiConfig;
import posdeskapp.api.ApiResponse;
import posdeskapp.api.HttpResponseResult;
import posdeskapp.api.TerminalActivationResponse;
import posdeskapp.models.ActivatedTerminal;
import posdeskapp.models.Configuration;
import posdeskapp.models.POS;
import posdeskapp.models.Platform;
import posdeskapp.models.TaxConfiguration;
import posdeskapp.models.TaxRate;
import posdeskapp.models.TaxpayerConfiguration;
import posdeskapp.models.TerminalConfiguration;
import posdeskapp.models.TerminalCredentials;
import posdeskapp.models.TerminalEnvironment;
import posdeskapp.models.TerminalRuntimeEnvironment;
import posdeskapp.models.UnActivatedTerminal;
import posdeskapp.utils.Alert;
import posdeskapp.utils.DbHelper;

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
        javafx.application.Platform.exit();
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
                        new POS("tumbativ1pos", "TUMBATI_POS__V1")
                )
        );

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String unActivatedTerminalPayload = gson.toJson(terminalActivationRequest);

        HttpResponseResult httpResponseResult = ApiClient.sendHttpPostRequest(ApiConfig.ACTIVATE_TERMINAL, unActivatedTerminalPayload);
        System.out.println(httpResponseResult.getResponseBody());
        if (httpResponseResult.getStatusCode() == 200) {
            processApiResponse(httpResponseResult.getResponseBody());
        } else {
            Alert error = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Terminal Activation", "Failed to activate the terminal");
        }
    }

    @FXML
    private void handleTerminalActivation(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            activateTerminal(new ActionEvent());
        }
    }

    private void processApiResponse(String responseBody) {
        Gson gson = new Gson();
        Type apiResponseType = new TypeToken<ApiResponse<TerminalActivationResponse>>() {
        }.getType();

        ApiResponse<TerminalActivationResponse> apiResponse = gson.fromJson(responseBody, apiResponseType);

        switch (apiResponse.getStatusCode()) {
            case 1:
                Configuration configuration = apiResponse.getData().getConfiguration();
                TerminalConfiguration terminalConfiguration = configuration.getTerminalConfiguration();
                TaxpayerConfiguration taxpayerConfiguration = configuration.getTaxpayerConfiguration();
                TaxConfiguration globalConfiguration = configuration.getGlobalConfiguration();
                ActivatedTerminal activatedTerminal = apiResponse.getData().getActivatedTerminal();
                TerminalCredentials credentials = apiResponse.getData().getActivatedTerminal().getTerminalCredentials();

                boolean isSaved = DbHelper.saveConfigurationDetails(terminalConfiguration, taxpayerConfiguration, globalConfiguration, activatedTerminal, credentials);

                if (isSaved) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Confirm terminal activation");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        //Confirm terminal activation, set this terminal as activated and download site products
                    }
                } else {
                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Terminal Activation", "Failed to save configuration details");
                }
                break;
            case -2:
                Alert error = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Terminal Activation", apiResponse.getRemark());
                terminalActivationCodeTextField.clear();
                break;
            default:
                break;
        }
    }

}
