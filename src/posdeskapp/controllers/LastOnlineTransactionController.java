/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class LastOnlineTransactionController implements Initializable {

    @FXML
    private Text invoiceNumberText;
    @FXML
    private Text invoiceDateText;
    @FXML
    private Text sellerTinText;
    @FXML
    private Text buyerTinText;
    @FXML
    private Text pacText;
    @FXML
    private Text siteIdText;
    @FXML
    private Text invoiceTotalText;
    @FXML
    private Text invoiceTotalVatText;
    @FXML
    private TableView<?> lineItemsTable;
    @FXML
    private TableColumn<?, ?> barcodeCol;
    @FXML
    private TableColumn<?, ?> descriptionCol;
    @FXML
    private TableColumn<?, ?> priceCol;
    @FXML
    private TableColumn<?, ?> quantityCol;
    @FXML
    private TableColumn<?, ?> taxRateCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void fetchProductDetails(MouseEvent event) {
    }
    
}
