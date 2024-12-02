/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import posdeskapp.models.LineItem;
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
    private TableColumn<PausedTransaction, String> pausedIdCol;

    ObservableList<PausedTransaction> data = FXCollections.observableArrayList();
    ObservableList<PausedTransaction> suspendedSales = FXCollections.observableArrayList();
    @FXML
    private TableView<PausedTransaction> suspendedTransactionsTable;
    @FXML
    private TableColumn<PausedTransaction, CheckBox> check;
    int selected = 0;
    private MainController mainController;
    private ObservableList<LineItem> selectedItems;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
       
        loadPausedTransactions();

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
        FilteredList<PausedTransaction> filteredList = new FilteredList<>(data, p -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super PausedTransaction>) transaction -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filterToLowerCase = newValue.toLowerCase();
                if (String.valueOf(transaction.getPauseId()).contains(filterToLowerCase)) {
                    return true;
                }

                if (String.valueOf(transaction.getFormattedTransactionTotal()).contains(filterToLowerCase)) {
                    return true;
                }

                if (String.valueOf(transaction.getTransactionDate()).contains(filterToLowerCase)) {
                    return true;
                }

                suspendedTransactionsTable.setPlaceholder(new Text("No record match your search"));
                return false;
            });

            SortedList<PausedTransaction> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(suspendedTransactionsTable.comparatorProperty());
            suspendedTransactionsTable.setItems(sortedList);
        }));
    }

    @FXML
    private void deleteselectedSuspendedTransactions(ActionEvent event) {
        List<Integer> pausedIds = new ArrayList<>();
        int transactions_to_delete = 0;
        for (PausedTransaction transaction : data) {
            if (transaction.getCheck().isSelected()) {
                transactions_to_delete++;
            }
        }
        if (transactions_to_delete > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete suspended sales");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the selected records");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                for (PausedTransaction pausedTransaction : data) {
                    if (pausedTransaction.getCheck().isSelected()) {
                        pausedIds.add(pausedTransaction.getPauseId());
                    }
                }
                DbHelper.deletePausedTransactions(pausedIds);
                cheakall.setSelected(false);
                loadPausedTransactions();
            }
        } else {
            posdeskapp.utils.Alert alert = new posdeskapp.utils.Alert(Alert.AlertType.WARNING, "Delete Transactions", "No suspended transactions selected to delete");
        }
    }

    private void initializeColumns() {
        check.setCellValueFactory(new PropertyValueFactory<>("check"));
        pausedIdCol.setCellValueFactory(new PropertyValueFactory<>("pauseId"));
        suspendedDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void loadPausedTransactions() {
        data.clear();
        data = DbHelper.getSuspendedTransactions();
        suspendedTransactionsTable.getItems().setAll(data);
    }

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    @FXML
    private void fetchSuspendeTransactionLineItems(MouseEvent event) {
        PausedTransaction pausedTransaction = (PausedTransaction) suspendedTransactionsTable.getSelectionModel().getSelectedItem();
        if (pausedTransaction != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Recall Transaction");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to recall this transaction?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                ObservableList<LineItem> lineItems = DbHelper.getLineSuspendedTransactionLineItems(pausedTransaction.getPauseId());

                mainController.setRecaledTransactionLineItems(lineItems);
                List<Integer> pausedIds = new ArrayList<>();
                pausedIds.add(pausedTransaction.getPauseId());
                DbHelper.deletePausedTransactions(pausedIds);
                // Close the suspended transactions panel
                Stage stage = (Stage) suspendedTransactionsTable.getScene().getWindow();
                stage.close();
            }
        }
    }
}
