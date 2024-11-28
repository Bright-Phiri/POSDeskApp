/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import posdeskapp.models.Invoice;
import posdeskapp.models.LineItem;
import posdeskapp.utils.DbHelper;
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
        FilteredList<Invoice> filteredList = new FilteredList<>(data, p -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super Invoice>) invoice -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filterToLowerCase = newValue.toLowerCase();
                if (invoice.getBuyerTin().toLowerCase().contains(filterToLowerCase)) {
                    return true;
                }
                if (invoice.getInvoiceNumber().toLowerCase().contains(filterToLowerCase)) {
                    return true;
                }
                if (String.valueOf(invoice.getInvoiceTotal()).contains(filterToLowerCase)) {
                    return true;
                }
                if (String.valueOf(invoice.getTotalVat()).contains(filterToLowerCase)) {
                    return true;
                }
                transactionTable.setPlaceholder(new Text("No record match your search"));
                return false;
            });

            SortedList<Invoice> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(transactionTable.comparatorProperty());
            transactionTable.setItems(sortedList);
        }));
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
        data = DbHelper.fetchInvoices();
        transactionTable.getItems().setAll(data);
    }
}
