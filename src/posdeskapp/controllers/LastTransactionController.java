/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.text.Text;
import posdeskapp.api.SalesResponse;
import posdeskapp.models.InvoiceLineItem;
import posdeskapp.models.LineItem;
import posdeskapp.utils.POSHelper;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class LastTransactionController implements Initializable {

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
    private TableView<LineItem> lineItemsTable;
    @FXML
    private TableColumn<LineItem, String> barcodeCol;
    @FXML
    private TableColumn<LineItem, String> descriptionCol;
    @FXML
    private TableColumn<LineItem, String> priceCol;
    @FXML
    private TableColumn<LineItem, Double> quantityCol;
    @FXML
    private TableColumn<LineItem, String> taxRateCol;
    @FXML
    private Text taxableAmountText;
    @FXML
    private Text transactionType;
    @FXML
    private TextField searchTextField;

    private SalesResponse salesResponse;
    ObservableList<LineItem> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedUnitPrice()));
        Platform.runLater(() -> {
            if (salesResponse != null) {
                populateInvoiceDetails(salesResponse);
            }
        });
    }

    public void setSalesResponse(SalesResponse salesResponse) {
        this.salesResponse = salesResponse;
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
                if (item.getUnitPrice().toString().contains(filterToLowerCase)) {
                    return true;
                }
                if (item.getTaxRateId().contains(filterToLowerCase)) {
                    return true;
                }
                lineItemsTable.setPlaceholder(new Text("No record match your search"));
                return false;
            });

            SortedList<LineItem> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(lineItemsTable.comparatorProperty());
            lineItemsTable.setItems(sortedList);
        }));
    }

    public void populateInvoiceDetails(SalesResponse salesResponse) {
        if (salesResponse.getInvoiceSummary().getOfflineSignature() != null) {
            transactionType.setText("Last Offline Transaction Details");
        } else {
            transactionType.setText("Last Online Transaction Details");
        }
        // Set Invoice Header Information
        invoiceNumberText.setText(salesResponse.getInvoiceHeader().getInvoiceNumber());
        invoiceDateText.setText(salesResponse.getInvoiceHeader().getInvoiceDateTime());
        sellerTinText.setText(salesResponse.getInvoiceHeader().getSellerTIN());
        buyerTinText.setText(salesResponse.getInvoiceHeader().getBuyerTIN());
        pacText.setText(salesResponse.getInvoiceHeader().getBuyerAuthorizationCode());
        siteIdText.setText(salesResponse.getInvoiceHeader().getSiteId());

        // Populate Line Items Table
        salesResponse.getInvoiceLineItems().stream().map((invoiceLineItem) -> {
            LineItem lineItem = new LineItem();
            lineItem.setProductCode(invoiceLineItem.getProductCode());
            lineItem.setDescription(invoiceLineItem.getDescription());
            lineItem.setUnitPrice(invoiceLineItem.getUnitPrice());
            lineItem.setQuantity(invoiceLineItem.getQuantity());
            lineItem.setTaxRateId(invoiceLineItem.getTaxRateId());
            return lineItem;
        }).forEachOrdered((lineItem) -> {
            data.add(lineItem);
        });
        lineItemsTable.setItems(data);

        // Set Invoice Summary Information
        double taxableAmount = salesResponse.getInvoiceSummary().getInvoiceTotal() - salesResponse.getInvoiceSummary().getTotalVAT();
        invoiceTotalText.setText(POSHelper.formatValue(salesResponse.getInvoiceSummary().getInvoiceTotal()));
        taxableAmountText.setText(POSHelper.formatValue(taxableAmount));
        invoiceTotalVatText.setText(POSHelper.formatValue(salesResponse.getInvoiceSummary().getTotalVAT()));
    }

    private void initializeColumns() {
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxRateCol.setCellValueFactory(new PropertyValueFactory<>("taxRateId"));
    }

}
