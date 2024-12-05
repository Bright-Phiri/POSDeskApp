/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
        ObservableList<LineItem> data = FXCollections.observableArrayList();
        for (InvoiceLineItem invoiceLineItem : salesResponse.getInvoiceLineItems()) {
            LineItem lineItem = new LineItem();
            lineItem.setProductCode(invoiceLineItem.getProductCode());
            lineItem.setDescription(invoiceLineItem.getDescription());
            lineItem.setUnitPrice(invoiceLineItem.getUnitPrice());
            lineItem.setQuantity(invoiceLineItem.getQuantity());
            lineItem.setTaxRateId(invoiceLineItem.getTaxRateId());
            data.add(lineItem);
        }
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
