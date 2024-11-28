/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import posdeskapp.models.PausedTransaction;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class SuspendedSalesController implements Initializable {

    @FXML
    private JFXTextField searchTextField;
    @FXML
    private HBox controlBox;
    @FXML
    private CheckBox cheakall;
    @FXML
    private TableColumn<PausedTransaction, Integer> paudedIdCol;
    @FXML
    private TableColumn<PausedTransaction, String> suspendedDateCol;
    @FXML
    private TableColumn<PausedTransaction, String> transactionTotal;
    @FXML
    private TableColumn<PausedTransaction, HBox> actions;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void searchPausedTransaction(KeyEvent event) {
    }

    @FXML
    private void deleteselectedSuspendedTransactions(ActionEvent event) {
    }
    
}
