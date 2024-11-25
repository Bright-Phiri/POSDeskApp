/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
    public static Text text;

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
        text = totalNoOfItems;
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
        LineItem lineItem = POSHelper.searchProductByCode(barcode, data);
        if (lineItem == null) {
            searchProductTextField.clear();
            Notification notification = new Notification("Message", "Product not found", 3);
            return;
        }
        searchProductTextField.clear();
        data.add(lineItem);
        productsTable.setItems(data);
        POSHelper.updateTotalQuantity(data, totalNoOfItems);
    }

}
