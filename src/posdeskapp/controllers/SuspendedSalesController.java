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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import posdeskapp.models.PausedTransaction;
import posdeskapp.utils.DbHelper;

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
    private TableColumn<PausedTransaction, String> suspendedDateCol;
    @FXML
    private TableColumn<PausedTransaction, String> transactionTotal;
    @FXML
    private TableColumn<PausedTransaction, HBox> actions;
    @FXML
    private TableColumn<PausedTransaction, String> pausedIdCol;

    ObservableList<PausedTransaction> data = FXCollections.observableArrayList();
    ObservableList<PausedTransaction> suspendedSales = FXCollections.observableArrayList();
    @FXML
    private TableView<PausedTransaction> suspendedTransactionsTable;
    @FXML
    private TableColumn<PausedTransaction, CheckBox> check;
    int selected = 0;

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

        transactionTotal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedTransactionTotal()));

        suspendedTransactionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cheakall.selectedProperty().addListener((observable, oldValue, newValue) -> {
            suspendedSales = suspendedTransactionsTable.getItems();
            suspendedSales.forEach((sale) -> {
                if (cheakall.isSelected()) {
                    sale.getCheck().setSelected(true);
                    selected++;
                    suspendedTransactionsTable.getSelectionModel().select(sale);
                } else {
                    sale.getCheck().setSelected(false);
                    selected--;
                    ObservableList<Integer> indices = suspendedTransactionsTable.getSelectionModel().getSelectedIndices();
                    indices.forEach((i) -> {
                        suspendedTransactionsTable.getSelectionModel().clearSelection(i);
                    });
                }
            });
        });
    }

    @FXML
    private void searchPausedTransaction(KeyEvent event) {
    }

    @FXML
    private void deleteselectedSuspendedTransactions(ActionEvent event) {
    }

    private void initializeColumns() {
        check.setCellValueFactory(new PropertyValueFactory<>("check"));
        pausedIdCol.setCellValueFactory(new PropertyValueFactory<>("pauseId"));
        suspendedDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        actions.setCellValueFactory(new PropertyValueFactory<>("controlsPane"));
    }

    private void loadProducts() {
        data.clear();
        data = DbHelper.getSuspendedTransactions();
        suspendedTransactionsTable.getItems().setAll(data);
    }

}
