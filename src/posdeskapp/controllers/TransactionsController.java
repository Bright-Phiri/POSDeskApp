/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import posdeskapp.models.Invoice;
import posdeskapp.utils.POSHelper;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class TransactionsController implements Initializable {

    @FXML
    private JFXTextField searchTextField;
    @FXML
    private TableView<Invoice> transactionTable;
    @FXML
    private TableColumn<Invoice, String> invoiceNumberCol;
    @FXML
    private TableColumn<Invoice, String> transactionDateCol;
    @FXML
    private TableColumn<Invoice, String> invoiceTotalCol;
    @FXML
    private TableColumn<Invoice, String> totalVATcol;
    @FXML
    private TableColumn<Invoice, String> buyerTinCol;

    ObservableList<Invoice> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        loadAllTransactions();

        invoiceTotalCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedInvoiceTotal()));
        totalVATcol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedInvoiceVAT()));
    }

    @FXML
    private void searchTransaction(KeyEvent event) {
    }

    private void initializeColumns() {
        invoiceNumberCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDateTime"));
        invoiceTotalCol.setCellValueFactory(new PropertyValueFactory<>("invoiceTotal"));
        buyerTinCol.setCellValueFactory(new PropertyValueFactory<>("buyerTin"));
        totalVATcol.setCellValueFactory(new PropertyValueFactory<>("totalVat"));
    }

    private void loadAllTransactions() {
        data.clear();
        data = POSHelper.fetchInvoices();
        transactionTable.getItems().setAll(data);
    }
}
