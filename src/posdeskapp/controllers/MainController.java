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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import posdeskapp.models.LineItem;
import posdeskapp.utils.DbConnection;

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
    private TableColumn<?, ?> barcodeCol;
    @FXML
    private TableColumn<?, ?> descriptionCol;
    @FXML
    private TableColumn<?, ?> priceCol;
    @FXML
    private TableColumn<?, ?> quantityCol;
    @FXML
    private TableColumn<?, ?> discountCol;
    @FXML
    private TableColumn<?, ?> totalCol;
    @FXML
    private TableColumn<?, ?> actionCol;
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

    static {
        TABLE_DEFINITIONS.put("Products", "CREATE TABLE Products (ProductCode TEXT NOT NULL PRIMARY KEY, ProductName TEXT NOT NULL, Description TEXT NOT NULL, Quantity REAL NOT NULL, UnitOfMeasure TEXT, Price REAL NOT NULL, SiteId TEXT, ProductExpiryDate TEXT, MinimumStockLevel REAL, TaxRateId TEXT)");
        TABLE_DEFINITIONS.put("LineItems", "CREATE TABLE LineItems (ID INTEGER, ProductCode TEXT, Description TEXT, UnitPrice REAL, Quantity REAL, InvoiceNumber TEXT, TaxRateID TEXT, Discount REAL, PRIMARY KEY(ID))");
        TABLE_DEFINITIONS.put("Invoices", "CREATE TABLE Invoices (InvoiceNumber TEXT PRIMARY KEY, InvoiceDateTime DATETIME, InvoiceTotal REAL, SellerTin TEXT, BuyerTIN TEXT, TotalVat REAL, State INTEGER, PaymentId TEXT)");
        TABLE_DEFINITIONS.put("PausedLineItems", "CREATE TABLE PausedLineItems (ID INTEGER, ProductCode TEXT, Description TEXT, UnitPrice REAL, Quantity REAL, PauseID TEXT, TaxRateID TEXT, Total REAL, TotalVAT REAL, PRIMARY KEY(ID))");
        TABLE_DEFINITIONS.put("PausedTransactions", "CREATE TABLE PausedTransactions (PauseId INTEGER, Timestamp DATETIME, Total REAL, PRIMARY KEY(PauseId))");
        TABLE_DEFINITIONS.put("TaxRates", "CREATE TABLE TaxRates (Id TEXT NOT NULL, Name TEXT NOT NULL, ChargeMode TEXT NOT NULL, Ordinal INTEGER NOT NULL, Rate REAL NOT NULL)");
        TABLE_DEFINITIONS.put("TaxpayerConfiguration", "CREATE TABLE TaxpayerConfiguration (TIN TEXT NOT NULL, IsVATRegistered BOOLEAN NOT NULL, TaxOfficeCode TEXT NOT NULL, TaxOfficeName TEXT NOT NULL, VersionNo INTEGER NOT NULL)");
        TABLE_DEFINITIONS.put("TerminalConfiguration", "CREATE TABLE TerminalConfiguration (TerminalLabel TEXT NOT NULL, EmailAddress TEXT NOT NULL, PhoneNumber TEXT NOT NULL, TradingName TEXT NOT NULL, AddressLine TEXT NOT NULL, VersionNo INTEGER NOT NULL, MaxTransactionAgeInHours INTEGER NOT NULL, MaxCummulativeAmount REAL NOT NULL, IsActivated INTEGER NOT NULL, IsActiveTerminal INTEGER NOT NULL)");
        TABLE_DEFINITIONS.put("TerminalKeys", "CREATE TABLE TerminalKeys (TerminalId TEXT NOT NULL, TaxpayerId TEXT NOT NULL, ActivationDate DATETIME NOT NULL, JwtToken TEXT NOT NULL, SecretKey TEXT NOT NULL)");
        TABLE_DEFINITIONS.put("TerminalSite", "CREATE TABLE TerminalSite (SiteId TEXT NOT NULL, SiteName TEXT NOT NULL)");
        TABLE_DEFINITIONS.put("Users", "CREATE TABLE Users (UserID INT AUTO_INCREMENT PRIMARY KEY, FirstName VARCHAR(50) NOT NULL, LastName VARCHAR(50) NOT NULL, UserName VARCHAR(50) NOT NULL UNIQUE, Gender VARCHAR(25) CHECK (Gender IN ('MALE', 'FEMALE')), PhoneNumber VARCHAR(25), EmailAddress VARCHAR(100) UNIQUE, Address TEXT, Role VARCHAR(7) NOT NULL CHECK (Role IN ('ADMIN', 'CASHIER')), Password VARCHAR(255) NOT NULL)");
        TABLE_DEFINITIONS.put("ActivationCode", "CREATE TABLE ActivationCode (ActivationCode TEXT)");
        TABLE_DEFINITIONS.put("InvoiceTaxBreakDown", "CREATE TABLE InvoiceTaxBreakDown (InvoiceNumber TEXT, RateID TEXT, TaxableAmount REAL, TaxAmount REAL)");
        TABLE_DEFINITIONS.put("GlobalConfiguration", "CREATE TABLE GlobalConfiguration (Id INTEGER NOT NULL, VersionNo INTEGER NOT NULL)");
    }
    ;

     ObservableList<LineItem> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDatabase();
        Platform.runLater(() -> searchProductTextField.requestFocus());
    }

    public void initializeDatabase() {
        TABLE_DEFINITIONS.forEach(DbConnection::checkTable);
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
    }

    @FXML
    private void AddLineItem(ActionEvent event) {
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
}
