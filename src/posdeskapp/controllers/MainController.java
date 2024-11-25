/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import posdeskapp.models.LineItem;
import posdeskapp.utils.DbConnection;
import posdeskapp.utils.Notification;
import posdeskapp.utils.POSHelper;

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
    private TableView<LineItem> productsTable;
    @FXML
    private TableColumn<LineItem, String> barcodeCol;
    @FXML
    private TableColumn<LineItem, String> descriptionCol;
    @FXML
    private TableColumn<LineItem, Double> priceCol;
    @FXML
    private TableColumn<LineItem, Double> quantityCol;
    @FXML
    private TableColumn<LineItem, Double> discountCol;
    @FXML
    private TableColumn<LineItem, Double> totalCol;
    @FXML
    private TableColumn<LineItem, HBox> actionCol;
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

    private static final Map<String, String> TABLE_DEFINITIONS = new HashMap<>();
    public static Text totalItemsText;
    public static Text taxableAmountText;
    public static Text invoiceTotalText;
    public static Text totalVAText;

    static {
       
    }
    ;

     ObservableList<LineItem> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDatabase();
        initializeColumns();
        Platform.runLater(() -> searchProductTextField.requestFocus());
        totalItemsText = totalNoOfItems;
        taxableAmountText = subTotalLabel;
        invoiceTotalText = totalLabel;
        totalVAText = vatLabel;
    }

    @FXML
    private void closeApp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close POS");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close the application ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
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
        if (event.getCode() == KeyCode.ENTER) {
            String barcode = searchProductTextField.getText().trim();
            addProductToTable(barcode);
        }
    }

    @FXML
    private void AddLineItem(ActionEvent event) {
        String barcode = searchProductTextField.getText().trim();
        addProductToTable(barcode);
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

    @FXML
    private void searchLineItem(KeyEvent event) {
        FilteredList<LineItem> filteredList = new FilteredList<>(data, p -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super LineItem>) item -> {
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

            SortedList<LineItem> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(productsTable.comparatorProperty());
            productsTable.setItems(sortedList);
        }));
    }

    private void initializeDatabase() {
        TABLE_DEFINITIONS.forEach(DbConnection::checkTable);
    }

    private void initializeColumns() {
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("controlsPane"));
    }

    private void addProductToTable(String barcode) {
        try {
            LineItem lineItem = POSHelper.searchProductByCode(barcode, data);

            if (lineItem == null) {
                searchProductTextField.clear();
                Notification notification = new Notification("Message", "Product not found", 3);
                return;
            }

            // Calculate line item quantity already in the data list
            double lineItemsQuantity = data.stream()
                    .filter(item -> item.getProductCode().equals(barcode))
                    .mapToDouble(LineItem::getQuantity)
                    .sum();

            double productQuantity = POSHelper.getProductQuantity(barcode);
            double remainingQuantity = productQuantity - lineItemsQuantity;

            if (remainingQuantity < 1) {
                searchProductTextField.clear();
                Notification notification = new Notification("Error", "Inventory quantity is not sufficient", 3);
                return;
            }

            // Check if the product already exists in the list
            LineItem existingLineItem = data.stream()
                    .filter(item -> item.getProductCode().equals(barcode))
                    .findFirst()
                    .orElse(null);

            if (existingLineItem != null) {
                // If the product exists, update its quantity
                existingLineItem.setQuantity(existingLineItem.getQuantity() + 1);

                double quantity = existingLineItem.getQuantity();
                double taxRate = POSHelper.getTaxRateById(existingLineItem.getTaxRateId());
                boolean isVATRegistered = POSHelper.isVATRegistered();

                // Calculate VAT for the line item
                double lineItemVAT = isVATRegistered
                        ? POSHelper.extractVATAmount(existingLineItem.getUnitPrice() - (existingLineItem.getDiscount() / quantity), quantity, taxRate)
                        : 0;

                existingLineItem.setTotalVAT(lineItemVAT);
                existingLineItem.setTotal((quantity * existingLineItem.getUnitPrice()) - existingLineItem.getDiscount());
                productsTable.refresh(); //Update existing line item on the table
            } else {

                double lineItemVAT = POSHelper.isVATRegistered()
                        ? POSHelper.extractVATAmount(lineItem.getUnitPrice() - (lineItem.getDiscount() / lineItem.getQuantity()), lineItem.getQuantity(), POSHelper.getTaxRateById(lineItem.getTaxRateId()))
                        : 0;

                lineItem.setTotalVAT(lineItemVAT);
                lineItem.setTotal((lineItem.getQuantity() * lineItem.getUnitPrice()) - lineItem.getDiscount());

                // Add the new line item
                data.add(lineItem);
                productsTable.setItems(data);
            }

            // Update invoice summary after adding/updating line items
            POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
            searchProductTextField.clear();

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

}
