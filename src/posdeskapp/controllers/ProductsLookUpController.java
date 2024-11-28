/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import posdeskapp.models.Product;
import posdeskapp.utils.DbHelper;
import posdeskapp.utils.POSHelper;

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
    private Product selectedProduct;
    private MainController mainController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        loadProducts();
    }

    @FXML
    private void searchProduct(KeyEvent event) {
        FilteredList<Product> filteredList = new FilteredList<>(data, p -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super Product>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filterToLowerCase = newValue.toLowerCase();
                if (item.getProductCode().toLowerCase().contains(filterToLowerCase)) {
                    return true;
                }
                if (item.getDescription().toLowerCase().contains(filterToLowerCase)) {
                    return true;
                }
                productsTable.setPlaceholder(new Text("No record match your search"));
                return false;
            });

            SortedList<Product> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(productsTable.comparatorProperty());
            productsTable.setItems(sortedList);
        }));
    }

    @FXML
    private void fetchProductDetails(MouseEvent event) {
        selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && mainController != null) {
            mainController.setSelectedProduct(selectedProduct);
        }

        // Close the lookup panel
        Stage stage = (Stage) productsTable.getScene().getWindow();
        stage.close();
    }

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    private void initializeColumns() {
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitOfMeasureCol.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasure"));
        expireCol.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        taxRateCol.setCellValueFactory(new PropertyValueFactory<>("taxRateId"));
    }

    private void loadProducts() {
        data.clear();
        data = DbHelper.fetchProducts();
        productsTable.getItems().setAll(data);
    }
}
