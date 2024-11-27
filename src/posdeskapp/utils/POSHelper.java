/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import posdeskapp.controllers.MainController;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.LineItem;
import posdeskapp.models.Product;
import posdeskapp.models.TaxBreakDown;

/**
 *
 * @author biphiri
 */
public class POSHelper {

    public static LineItem searchProductByCode(String productCode, ObservableList<LineItem> data) {
        String query = "SELECT * FROM Products WHERE ProductCode = ?";
        String taxRateQuery = "SELECT * FROM TaxRates WHERE ID = ?";
        Connection connection = null;
        PreparedStatement productStmt = null;
        PreparedStatement taxRateStmt = null;
        ResultSet productRs = null;
        ResultSet taxRateRs = null;

        try {
            connection = DbConnection.createConnection();

            productStmt = connection.prepareStatement(query);
            productStmt.setString(1, productCode);
            productRs = productStmt.executeQuery();

            if (productRs.next()) {
                String description = productRs.getString("Description");
                double price = productRs.getDouble("Price");
                String taxRateId = productRs.getString("TaxRateId");

                taxRateStmt = connection.prepareStatement(taxRateQuery);
                taxRateStmt.setString(1, taxRateId);
                taxRateRs = taxRateStmt.executeQuery();

                if (taxRateRs.next()) {
                    double taxRate = taxRateRs.getDouble("Rate");
                    double lineItemVAT = 0.0;

                    boolean isVATRegistered = isVATRegistered();

                    if (isVATRegistered) {
                        lineItemVAT = extractVATAmount(price, 1, taxRate);
                    }

                    // Format numeric values
                    String formattedPrice = formatValue(price);
                    String formattedLineItemVAT = formatValue(lineItemVAT);

                    double formattedPriceDouble = Double.parseDouble(formattedPrice.replace(",", ""));
                    double formattedLineItemVATDouble = Double.parseDouble(formattedLineItemVAT.replace(",", ""));

                    LineItem lineItem = new LineItem(
                            productCode,
                            description,
                            1,
                            formattedPriceDouble,
                            formattedPriceDouble,
                            0,
                            formattedLineItemVATDouble,
                            taxRateId,
                            setActionButtons(productCode, data)
                    );

                    return lineItem;
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while fetching product: " + ex.getMessage());
        } finally {
            try {
                if (taxRateRs != null) {
                    taxRateRs.close();
                }
                if (productRs != null) {
                    productRs.close();
                }
                if (taxRateStmt != null) {
                    taxRateStmt.close();
                }
                if (productStmt != null) {
                    productStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }

        return null;
    }

    public static ObservableList<Product> fetchProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        PreparedStatement pre = null;
        Connection conn = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Products";

        try {
            conn = DbConnection.createConnection();
            pre = conn.prepareStatement(query);
            rs = pre.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getString(1),
                        rs.getString(3),
                        rs.getDouble(6),
                        rs.getString(5),
                        rs.getDouble(4),
                        rs.getString(8),
                        rs.getString(10)
                ));
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (pre != null) {
                    pre.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }

        return products;
    }

    public static double getTaxRateById(String taxRateId) {
        String taxRateQuery = "SELECT * FROM TaxRates WHERE ID = ?";
        Connection connection = null;
        PreparedStatement taxRateStmt = null;
        ResultSet taxRateRs = null;
        double taxRate = 0.0; // Default tax rate if not found or error occurs

        try {
            connection = DbConnection.createConnection();
            taxRateStmt = connection.prepareStatement(taxRateQuery);
            taxRateStmt.setString(1, taxRateId); // Set the parameter for the query

            // Execute the query and get the result
            taxRateRs = taxRateStmt.executeQuery();
            if (taxRateRs.next()) {
                // Retrieve the tax rate from the result set
                taxRate = taxRateRs.getDouble("Rate");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            // Ensure resources are closed to avoid resource leaks
            try {
                if (taxRateRs != null) {
                    taxRateRs.close();
                }
                if (taxRateStmt != null) {
                    taxRateStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex);
            }
        }
        return taxRate;
    }

    public static String getTerminalLabel() {
        String terminalLabel = null;
        String query = "SELECT TerminalLabel FROM TerminalConfiguration";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                terminalLabel = resultSet.getString("TerminalLabel");
            }

        } catch (SQLException ex) {
            System.err.println("An error occurred while fetching Terminal Label: " + ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing database resources: " + ex.getMessage());
            }
        }

        return terminalLabel;
    }

    public static void markInvoiceAsProcessed(String invoiceNumber) {
        String updateQuery = "UPDATE Invoices SET State = ? WHERE InvoiceNumber = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(updateQuery);

            statement.setString(1, invoiceNumber);
            statement.setInt(2, 1);

            statement.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("An error occurred while updating invoice state: " + ex.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public static String getTaxPayerTIN() {
        String tin = null;
        String query = "SELECT TIN FROM TaxpayerConfiguration";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                tin = resultSet.getString(1); // Column index starts at 1
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while fetching TIN: " + ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return tin;
    }

    public static double parseDecimal(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static String formatValue(double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }

    public static double parseFormattedValue(String formattedValue) {
        try {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            Number number = formatter.parse(formattedValue);
            return number.doubleValue();
        } catch (ParseException e) {
            System.err.println("Invalid formatted value: " + formattedValue);
            return 0.0;
        }
    }

    public static double getProductQuantity(String productCode) {
        double quantity = 0;
        String query = "SELECT Quantity FROM Products WHERE ProductCode = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, productCode);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                quantity = resultSet.getDouble("Quantity");
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
        return quantity;
    }

    public static boolean processTransaction(
            InvoiceHeader invoice,
            List<LineItem> lineItems,
            List<TaxBreakDown> taxBreakdowns,
            double total,
            double totalVAT
    ) {
        String insertInvoiceQuery = "INSERT INTO Invoices (InvoiceNumber, InvoiceDateTime, InvoiceTotal, SellerTin, BuyerTin, TotalVAT, State) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertLineItemQuery = "INSERT INTO LineItems (ProductCode, Description, UnitPrice, Quantity, InvoiceNumber, TaxRateID, Discount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertTaxBreakdownQuery = "INSERT INTO InvoiceTaxBreakDown (InvoiceNumber, RateID, TaxableAmount, TaxAmount) VALUES (?, ?, ?, ?)";
        String updateProductQuery = "UPDATE Products SET Quantity = ? WHERE ProductCode = ?";

        Connection connection = null;
        PreparedStatement invoiceStmt = null;
        PreparedStatement lineItemStmt = null;
        PreparedStatement taxBreakdownStmt = null;
        PreparedStatement updateProductStmt = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint("SaveTransaction");

            // Save the invoice
            invoiceStmt = connection.prepareStatement(insertInvoiceQuery);
            invoiceStmt.setString(1, invoice.getInvoiceNumber());
            invoiceStmt.setObject(2, invoice.getInvoiceDateTime());
            invoiceStmt.setDouble(3, total);
            invoiceStmt.setString(4, invoice.getSellerTIN());
            invoiceStmt.setString(5, invoice.getBuyerTIN());
            invoiceStmt.setDouble(6, totalVAT);
            invoiceStmt.setInt(7, 0);
            invoiceStmt.executeUpdate();

            // Save line items and update Product quantity
            for (LineItem lineItem : lineItems) {
                double currentQuantity = getProductQuantity(lineItem.getProductCode());
                double newQuantity = currentQuantity - lineItem.getQuantity();

                // Update product quantity
                updateProductStmt = connection.prepareStatement(updateProductQuery);
                updateProductStmt.setDouble(1, newQuantity);
                updateProductStmt.setString(2, lineItem.getProductCode());
                updateProductStmt.executeUpdate();

                // Save line item
                lineItemStmt = connection.prepareStatement(insertLineItemQuery);
                lineItemStmt.setString(1, lineItem.getProductCode());
                lineItemStmt.setString(2, lineItem.getDescription());
                lineItemStmt.setDouble(3, lineItem.getUnitPrice());
                lineItemStmt.setDouble(4, lineItem.getQuantity());
                lineItemStmt.setString(5, invoice.getInvoiceNumber());
                lineItemStmt.setString(6, lineItem.getTaxRateId());
                lineItemStmt.setDouble(7, lineItem.getDiscount());
                lineItemStmt.executeUpdate();
            }

            // Save tax breakdowns
            for (TaxBreakDown taxBreakdown : taxBreakdowns) {
                taxBreakdownStmt = connection.prepareStatement(insertTaxBreakdownQuery);
                taxBreakdownStmt.setString(1, invoice.getInvoiceNumber());
                taxBreakdownStmt.setString(2, taxBreakdown.getRateId());
                taxBreakdownStmt.setDouble(3, taxBreakdown.getTaxableAmount());
                taxBreakdownStmt.setDouble(4, taxBreakdown.getTaxAmount());
                taxBreakdownStmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error while saving transaction: " + e.getMessage());
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (invoiceStmt != null) {
                    invoiceStmt.close();
                }
                if (lineItemStmt != null) {
                    lineItemStmt.close();
                }
                if (taxBreakdownStmt != null) {
                    taxBreakdownStmt.close();
                }
                if (updateProductStmt != null) {
                    updateProductStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    private static HBox setActionButtons(String productCode, ObservableList<LineItem> data) {
        HBox hBox = new HBox(5);
        // Create a trash icon
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
        icon.setSize("20");
        icon.setFill(Color.RED);
        icon.setId(productCode);

        HBox.setMargin(icon, new Insets(0, 0, 0, 0));
        HBox.setHgrow(icon, Priority.ALWAYS);

        hBox.setAlignment(Pos.CENTER);

        icon.setOnMouseClicked(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove line item");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove this line item?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                for (LineItem lineItem : data) {
                    if (lineItem.getProductCode().equals(productCode)) {
                        data.remove(lineItem);
                        updateInvoiceSummary(data, MainController.invoiceTotalText, MainController.taxableAmountText, MainController.totalVAText, MainController.totalItemsText);
                        break;
                    }
                }
            }
        });

        // Add the icon to the HBox
        hBox.getChildren().add(icon);

        return hBox;
    }

    public static List<TaxBreakDown> generateTaxSummary(List<LineItem> lineItems) {
        Map<String, TaxBreakDown> taxBreakdowns = new HashMap<>();

        lineItems.forEach((item) -> {
            double taxableAmount = item.getTotal() - item.getTotalVAT();
            double taxAmount = item.getTotalVAT();

            if (taxBreakdowns.containsKey(item.getTaxRateId())) {
                TaxBreakDown existing = taxBreakdowns.get(item.getTaxRateId());
                double updatedTaxableAmount = existing.getTaxableAmount() + taxableAmount;
                double updatedTaxAmount = existing.getTaxAmount() + taxAmount;

                taxBreakdowns.put(item.getTaxRateId(),
                        new TaxBreakDown(item.getTaxRateId(), updatedTaxableAmount, updatedTaxAmount));
            } else {
                taxBreakdowns.put(item.getTaxRateId(),
                        new TaxBreakDown(item.getTaxRateId(), taxableAmount, taxAmount));
            }
        });

        return new ArrayList<>(taxBreakdowns.values());
    }

    public static String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return currentDate.format(formatter);
    }

    public static void updateInvoiceSummary(ObservableList<LineItem> data, Text invoiceTotalText, Text taxableAmountText, Text totalVATText, Text itemsCount) {

        double invoiceTotal = data.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        invoiceTotalText.setText(formatValue(invoiceTotal));

        double taxableAmount = data.stream()
                .mapToDouble(item -> item.getTotal() - item.getTotalVAT())
                .sum();
        taxableAmountText.setText(formatValue(taxableAmount));

        double totalVAT = data.stream()
                .mapToDouble(LineItem::getTotalVAT)
                .sum();
        totalVATText.setText(formatValue(totalVAT));

        double totalQuantity = data.stream()
                .mapToDouble(LineItem::getQuantity)
                .sum();
        itemsCount.setText(String.valueOf(totalQuantity));
    }

    public static boolean isVATRegistered() {
        String query = "SELECT IsVATRegistered FROM TaxpayerConfiguration";
        boolean isVATRegistered = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int result = resultSet.getInt("IsVATRegistered");
                isVATRegistered = result == 1;
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while checking VAT registration: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("An error occurred while closing resources: " + ex.getMessage());
            }
        }

        return isVATRegistered;
    }

    public static double extractVATAmount(double sellingPrice, double quantity, double taxRate) {
        double totalPrice = sellingPrice * quantity;
        double vatAmount = (totalPrice * taxRate) / (100 + taxRate);
        return Math.round(vatAmount * 100.0) / 100.0; // Round to 2 decimal places
    }
}
