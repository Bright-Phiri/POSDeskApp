/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import posdeskapp.api.HttpResponseResult;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.LineItem;
import posdeskapp.models.Product;
import posdeskapp.models.SalesInvoice;
import posdeskapp.models.TaxBreakDown;
import posdeskapp.utils.DbConnection;
import posdeskapp.utils.DbHelper;
import posdeskapp.api.ApiClient;
import posdeskapp.api.ApiResponse;
import posdeskapp.api.InvoiceResponse;
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
    private TableColumn<LineItem, String> priceCol;
    @FXML
    private TableColumn<LineItem, Double> quantityCol;
    @FXML
    private TableColumn<LineItem, Double> discountCol;
    @FXML
    private TableColumn<LineItem, String> totalCol;
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
    @FXML
    private BorderPane rootPahe;
    @FXML
    private JFXButton homeButton;
    @FXML
    private JFXButton itemLookUpButton;
    @FXML
    private JFXButton paymentButton;
    @FXML
    private JFXButton voidButton;
    @FXML
    private JFXButton suspendButton;
    @FXML
    private JFXButton addLineItemButton;
    @FXML
    private MenuBar menubar;

    private static final Map<String, String> TABLE_DEFINITIONS = new HashMap<>();
    public static Text totalItemsText;
    public static Text taxableAmountText;
    public static Text invoiceTotalText;
    public static Text totalVAText;
    public static ObservableList<LineItem> suspendedLineItems;

    static {

    }
    ;

    ObservableList<LineItem> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDatabase();
        initializeColumns();

        Platform.runLater(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> searchProductTextField.requestFocus()));
            timeline.play();
        });

        String terminalLbel = DbHelper.getTerminalLabel();
        tillName.setText(terminalLbel);

        date.setText(POSHelper.getDate());
        cashierLabel.setText(DbHelper.USERNAME);

        totalItemsText = totalNoOfItems;
        taxableAmountText = subTotalLabel;
        invoiceTotalText = totalLabel;
        totalVAText = vatLabel;

        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedUnitPrice()));
        totalCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedTotal()));

        productsTable.setEditable(true);

        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.valueOf(string);
                } catch (NumberFormatException e) {
                    return 0.0; // Default value in case of invalid input
                }
            }
        }));
    }

    @FXML
    private void closeApp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure, you want to Sign Out?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            Stage stage = (Stage) menubar.getScene().getWindow();
            stage.close();
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("/posdeskapp/views/Login.fxml"));
                Scene scene = new Scene(root1);
                Stage stage1 = new Stage();
                stage1.setScene(scene);
                stage1.getIcons().add(new Image("/posdeskapp/images/point-of-sale-icon.png"));
                stage1.initStyle(StageStyle.UNDECORATED);
                stage1.centerOnScreen();
                stage1.show();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void viewAllTransactions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/posdeskapp/views/Transactions.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/posdeskapp/images/point-of-sale-icon.png"));
            stage.setTitle("POS");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void viewSuspendedSales(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/posdeskapp/views/SuspendedSales.fxml"));
        Parent root = loader.load();

        // Get the controller for the lookup screen
        SuspendedSalesController lookupController = loader.getController();

        // Pass the reference of this controller to the lookup controller
        lookupController.setMainController(this);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/posdeskapp/images/point-of-sale-icon.png"));
        stage.setTitle("POS");
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void loadHomePage(ActionEvent event) {
    }

    @FXML
    private void loadItemsLookupScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/posdeskapp/views/ProductsLookUp.fxml"));
        Parent root = loader.load();

        // Get the controller for the lookup screen
        ProductsLookUpController lookupController = loader.getController();

        // Pass the reference of this controller to the lookup controller
        lookupController.setMainController(this);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("/posdeskapp/images/point-of-sale-icon.png"));
        stage.setTitle("POS");
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
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
        if (!barcode.isEmpty()) {
            addProductToTable(barcode);
        }
    }

    @FXML
    private void checkOut(ActionEvent event) {
        if (data.isEmpty()) {
            Notification notification = new Notification("Warning", "No products added, add some products first ", 3);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String invoiceDate = POSHelper.formatDate(now);

        String terminalSite = DbHelper.fetchTerminalSiteId();
        String sellerTIN = DbHelper.getTaxPayerTIN();

        InvoiceHeader invoice = new InvoiceHeader(UUID.randomUUID().toString(), invoiceDate, sellerTIN, buyerTINTextField.getText(), "", terminalSite, 1, 1, 1);

        List<TaxBreakDown> taxBreakdowns = POSHelper.generateTaxSummary(data);

        double invoiceTotal = POSHelper.parseFormattedValue(totalLabel.getText());
        double totalVAT = POSHelper.parseFormattedValue(vatLabel.getText());

        SalesInvoice invoiceRequest = POSHelper.createInvoiceRequest(invoice, data, invoiceTotal, totalVAT);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String invoicePayload = gson.toJson(invoiceRequest);

        boolean isProcessed = DbHelper.processTransaction(invoice, data, taxBreakdowns, invoiceTotal, totalVAT);

        if (isProcessed) {
            posdeskapp.utils.Alert alert = new posdeskapp.utils.Alert(Alert.AlertType.INFORMATION, "Transaction", "Transaction completed successfully");
            Task<HttpResponseResult> task = new Task<HttpResponseResult>() {
                @Override
                protected HttpResponseResult call() throws Exception {
                    return ApiClient.submitSalesTransaction(invoicePayload);
                }
            };

            task.setOnSucceeded(e -> {
                HttpResponseResult result = task.getValue();
                if (result.getStatusCode() == 200) {
                    Gson gSon = new Gson();
                    Type apiResponseType = new TypeToken<ApiResponse<InvoiceResponse>>() {
                    }.getType();
                    ApiResponse<InvoiceResponse> apiResponse = gSon.fromJson(result.getResponseBody(), apiResponseType);
                    DbHelper.markInvoiceAsProcessed(invoice.getInvoiceNumber());
                    // Generate receipt with validation URL as QR code
                    data.clear();
                    POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
                    productsTable.refresh();
                    tenderedAmountTextField.clear();
                } else {
                    // Offline mode
                    data.clear();
                    POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
                    productsTable.refresh();
                    tenderedAmountTextField.clear();
                }
            });

            new Thread(task).start();
        } else {
            posdeskapp.utils.Alert alert = new posdeskapp.utils.Alert(Alert.AlertType.ERROR, "Transaction", "Failed to complete the transaction");
        }
    }

    @FXML
    private void voidTransaction(ActionEvent event) {
        if (data.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Void Transaction");
            alert.setContentText("Are you sure you want to void this transaction?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                data.clear();
                POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
                tenderedAmountTextField.clear();
                changeLabel.setText("0.00");
                Notification notification = new Notification("Information", "Transaction successfully voided.", 3);
            }
        } else {
            Notification notification = new Notification("Information", "No transaction to void.", 3);
        }
        searchProductTextField.requestFocus();
    }

    @FXML
    private void suspendTransaction(ActionEvent event) {
        if (data.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Void Transaction");
            alert.setContentText("Are you sure you want to suspend this transaction?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                int pausedId = POSHelper.generatePauseId();
                if (DbHelper.savePausedTransaction(data, pausedId)) {
                    posdeskapp.utils.Alert alert1 = new posdeskapp.utils.Alert(Alert.AlertType.INFORMATION, "Suspend Transaction", "Transaction successfully suspended. Customer Paused ID is " + pausedId);
                    data.clear();
                    POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
                    tenderedAmountTextField.clear();
                    changeLabel.setText("0.00");
                }
            } else {
                Notification notification = new Notification("Error", "Failed to suspend transaction.", 3);
            }
        } else {
            Notification notification = new Notification("Information", "No transaction to suspend.", 3);
        }
        searchProductTextField.requestFocus();
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

    @FXML
    private void updateCheckout(KeyEvent event) {
        try {
            double total = POSHelper.parseFormattedValue(totalLabel.getText());

            // Parsing the payment amount
            String paymentText = tenderedAmountTextField.getText();
            double paymentAmount = 0;

            if (!paymentText.isEmpty()) {
                paymentAmount = Double.parseDouble(paymentText);
            }

            // Checking if the payment amount is valid and calculating change
            if (total != 0) {
                if (paymentAmount >= total) {
                    double change = paymentAmount - total;
                    changeLabel.setText(POSHelper.formatValue(change));
                } else {
                    changeLabel.setText("0.00");
                }
            }
        } catch (NumberFormatException e) {
            // In case of invalid number input
            changeLabel.setText("0.00");
            Notification notification = new Notification("Error", "Invalid amount", 3);
        }
    }

    @FXML
    private void updateLineItemQuantity(TableColumn.CellEditEvent<LineItem, Double> event) {
        Double newQuantity = event.getNewValue();

        if (newQuantity == null || newQuantity == 0 || newQuantity.isNaN() || newQuantity.isInfinite()) {
            posdeskapp.utils.Alert alert = new posdeskapp.utils.Alert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid quantity.");
            event.getTableView().refresh();
            productsTable.requestFocus();
            return;
        }

        LineItem lineItem = event.getRowValue();

        double productQuantity = DbHelper.getProductQuantity(lineItem.getProductCode());

        if (productQuantity < 0) {
            return;
        }

        double totalLineItemsQuantity = data.stream()
                .filter(item -> item.getProductCode().equals(lineItem.getProductCode()) && item != lineItem)
                .mapToDouble(LineItem::getQuantity)
                .sum();

        double remainingQuantity = productQuantity - totalLineItemsQuantity;

        if (newQuantity > remainingQuantity) {
            posdeskapp.utils.Alert alert = new posdeskapp.utils.Alert(Alert.AlertType.WARNING, "Inventory Levels", "Inventory quantity is not sufficient");

            // Reset the quantity value in the table
            productsTable.getItems().get(event.getTablePosition().getRow()).setQuantity(lineItem.getQuantity());
            event.getTableView().refresh();
            return;
        }

        // Update line item quantity
        lineItem.setQuantity(newQuantity);

        // Update VAT and total for the line item
        double taxRate = DbHelper.getTaxRateById(lineItem.getTaxRateId());
        boolean isVATRegistered = DbHelper.isVATRegistered();

        if (isVATRegistered) {
            lineItem.setTotalVAT(POSHelper.extractVATAmount(lineItem.getUnitPrice(), newQuantity, taxRate));
        }

        lineItem.setTotal(newQuantity * lineItem.getUnitPrice());

        productsTable.refresh();

        POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);

        productsTable.requestFocus();
    }

    public void setSelectedProduct(Product product) {

        if (product.getQuantity() < 1) {
            Platform.runLater(() -> {
                Notification notification = new Notification("Warning", "Inventory quantity is not sufficient", 3);
            });

            return;
        }
        addProductToTable(product.getProductCode());
    }

    public void setRecaledTransactionLineItems(ObservableList<LineItem> lineItems) {
        data = lineItems;
        productsTable.setItems(data);
        POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
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
            LineItem lineItem = DbHelper.searchProductByCode(barcode, data);

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

            double productQuantity = DbHelper.getProductQuantity(barcode);
            double remainingQuantity = productQuantity - lineItemsQuantity;

            if (remainingQuantity < 1) {
                searchProductTextField.clear();
                Notification notification = new Notification("Warning", "Inventory quantity is not sufficient", 3);
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
                double taxRate = DbHelper.getTaxRateById(existingLineItem.getTaxRateId());
                boolean isVATRegistered = DbHelper.isVATRegistered();

                // Calculate VAT for the line item
                double lineItemVAT = isVATRegistered
                        ? POSHelper.extractVATAmount(existingLineItem.getUnitPrice() - (existingLineItem.getDiscount() / quantity), quantity, taxRate)
                        : 0;

                existingLineItem.setTotalVAT(lineItemVAT);
                existingLineItem.setTotal((quantity * existingLineItem.getUnitPrice()) - existingLineItem.getDiscount());
                productsTable.refresh(); //Update existing line item on the table
                changeLabel.setText("0.00");
                tenderedAmountTextField.setText(null);
            } else {

                double lineItemVAT = DbHelper.isVATRegistered()
                        ? POSHelper.extractVATAmount(lineItem.getUnitPrice() - (lineItem.getDiscount() / lineItem.getQuantity()), lineItem.getQuantity(), DbHelper.getTaxRateById(lineItem.getTaxRateId()))
                        : 0;

                lineItem.setTotalVAT(lineItemVAT);
                lineItem.setTotal((lineItem.getQuantity() * lineItem.getUnitPrice()) - lineItem.getDiscount());

                // Add the new line item
                data.add(lineItem);
                productsTable.setItems(data);
                changeLabel.setText("0.00");
                tenderedAmountTextField.setText(null);
            }

            // Update invoice summary after adding/updating line items
            POSHelper.updateInvoiceSummary(data, invoiceTotalText, subTotalLabel, totalVAText, totalNoOfItems);
            searchProductTextField.clear();

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void getTerminalSiteProducts(ActionEvent event) {
        int unTransmittedInvoices = DbHelper.getUntransmittedInvoices().size();
        if (unTransmittedInvoices == 0) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Gson gson = new Gson();
                    String siteId = DbHelper.fetchTerminalSiteId();
                    String TIN = DbHelper.getTIN();

                    Map<String, String> getSiteProducts = new HashMap<>();
                    getSiteProducts.put("tin", TIN);
                    getSiteProducts.put("siteId", siteId);

                    String getTerminalSitePayload = gson.toJson(getSiteProducts);
                    HttpResponseResult productsHttpResponseResult = ApiClient.getTaxpayerTerminalSiteProducts(getTerminalSitePayload);

                    if (productsHttpResponseResult.getStatusCode() == 200) {
                        if (DbHelper.saveProductsFromJson(productsHttpResponseResult.getResponseBody())) {
                            Platform.runLater(() -> {
                                Notification notification = new Notification("Information", "Products successfully saved to the Terminal!", 4);
                            });
                        } else {
                            Platform.runLater(() -> {
                                Notification notification = new Notification("Error", "Failed to save products to the terminal. Please try again.", 4);
                            });
                        }
                    } else {
                        Platform.runLater(() -> {
                            Notification notification = new Notification("Error", "Failed to fetch products from the API. Please check your connection and try again.", 4);
                        });
                    }
                    return null;
                }
            };

            // Start the background task
            new Thread(task).start();
        } else {
            Platform.runLater(() -> {
                Notification notification = new Notification("Error", "Transmission failed due to " + unTransmittedInvoices + " untransmitted invoices. Please ensure all invoices are transmitted before proceeding.", 4);
            });
        }
    }

}
