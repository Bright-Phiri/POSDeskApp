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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class MainController implements Initializable {

    @FXML
    private Text tillName;
    @FXML
    private Text cashierLabel;
    @FXML
    private Text date;
    @FXML
    private TextField searchTextField;
    @FXML
    private Text totalNoOfItems;
    @FXML
    private JFXTextField searchProductTextField;
    @FXML
    private TableView<?> productsTable;
    @FXML
    private TableColumn<?, ?> barcodeCol;
    @FXML
    private TableColumn<?, ?> descriptionCol;
    @FXML
    private TableColumn<?, ?> priceCol;
    @FXML
    private TableColumn<?, ?> quantityCol;
    @FXML
    private TableColumn<?, ?> discountCol;
    @FXML
    private TableColumn<?, ?> totalCol;
    @FXML
    private TableColumn<?, ?> actionCol;
    @FXML
    private Text totalLabel;
    @FXML
    private Text subTotalLabel;
    @FXML
    private Text discountLabel;
    @FXML
    private Text vatLabel;
    @FXML
    private TextField tenderedAmountTextField;
    @FXML
    private TextField buyerTINTextField;
    @FXML
    private Text changeLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeApp(ActionEvent event) {
    }

    @FXML
    private void viewAllTransactions(ActionEvent event) {
    }

    @FXML
    private void viewSuspendedSales(ActionEvent event) {
    }

    @FXML
    private void loadHomePage(ActionEvent event) {
    }

    @FXML
    private void loadItemsLookupScreen(ActionEvent event) {
    }

    @FXML
    private void fetchProduct(KeyEvent event) {
    }

    @FXML
    private void AddLineItem(ActionEvent event) {
    }

    @FXML
    private void checkOut(ActionEvent event) {
    }

    @FXML
    private void voidTransaction(ActionEvent event) {
    }

    @FXML
    private void suspendTransaction(ActionEvent event) {
    }
    
}
