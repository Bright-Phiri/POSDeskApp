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
import java.text.DecimalFormat;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import posdeskapp.controllers.MainController;
import posdeskapp.models.LineItem;

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
            connection = DbConnection.Connect();

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

                    LineItem lineItem = new LineItem(productCode, description, 1, price, price, 0, lineItemVAT, taxRateId, setActionButtons(productCode, data));

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

    public static double getTaxRateById(String taxRateId) {
        String taxRateQuery = "SELECT * FROM TaxRates WHERE ID = ?";
        Connection connection = null;
        PreparedStatement taxRateStmt = null;
        ResultSet taxRateRs = null;
        double taxRate = 0.0; // Default tax rate if not found or error occurs

        try {
            connection = DbConnection.Connect();
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

    public static double getProductQuantity(String productCode) {
        double quantity = 0;
        String query = "SELECT Quantity FROM Products WHERE ProductCode = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.Connect();
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
            for (LineItem lineItem : data) {
                if (lineItem.getProductCode().equals(productCode)) {
                    data.remove(lineItem);
                    updateInvoiceSummary(data, MainController.invoiceTotalText, MainController.taxableAmountText, MainController.totalVAText, MainController.totalItemsText);
                    break;
                }
            }
        });

        // Add the icon to the HBox
        hBox.getChildren().add(icon);

        return hBox;
    }

    public static void updateInvoiceSummary(ObservableList<LineItem> data, Text invoiceTotalText, Text taxableAmountText, Text totalVATText, Text itemsCount) {
        DecimalFormat df = new DecimalFormat("0.00");

        double invoiceTotal = data.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        invoiceTotalText.setText(df.format(invoiceTotal));

        double taxableAmount = data.stream()
                .mapToDouble(item -> item.getTotal() - item.getTotalVAT())
                .sum();
        taxableAmountText.setText(df.format(taxableAmount));

        double totalVAT = data.stream()
                .mapToDouble(LineItem::getTotalVAT)
                .sum();
        totalVATText.setText(df.format(totalVAT));

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
            connection = DbConnection.Connect();
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
