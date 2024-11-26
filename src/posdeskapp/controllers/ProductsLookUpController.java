/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import posdeskapp.models.Product;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class ProductsLookUpController implements Initializable {

    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> barcodeCol;
    @FXML
    private TableColumn<Product, String> descriptionCol;
    @FXML
    private TableColumn<Product, Double> quantityCol;
    @FXML
    private TableColumn<Product, String> unitOfMeasureCol;
    @FXML
    private TableColumn<Product, Double> priceCol;
    @FXML
    private TableColumn<Product, String> expireCol;
    @FXML
    private TableColumn<Product, String> taxRateCol;

    ObservableList<Product> data = FXCollections.observableArrayList();
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void searchProduct(KeyEvent event) {
    }
    
}
