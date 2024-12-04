/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import posdeskapp.controllers.SuspendedSalesController;
import posdeskapp.models.ActivatedTerminal;
import posdeskapp.models.Invoice;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.LineItem;
import posdeskapp.models.PausedTransaction;
import posdeskapp.models.Product;
import posdeskapp.models.TaxBreakDown;
import posdeskapp.models.TaxConfiguration;
import posdeskapp.models.TaxRate;
import posdeskapp.models.TaxpayerConfiguration;
import posdeskapp.models.TerminalConfiguration;
import posdeskapp.models.TerminalCredentials;

/**
 *
 * @author biphiri
 */
public class DbHelper {

    public static String USERTYPE;
    public static String USERNAME;

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
                        lineItemVAT = POSHelper.extractVATAmount(price, 1, taxRate);
                    }

                    String formattedPrice = POSHelper.formatValue(price);
                    String formattedLineItemVAT = POSHelper.formatValue(lineItemVAT);

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
                            POSHelper.setActionButtons(productCode, data)
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

    public static String getTerminalJwtToken() {
        String jwtToken = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            String query = "SELECT JwtToken FROM TerminalKeys LIMIT 1";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                jwtToken = resultSet.getString("JwtToken");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching terminal info: " + ex.getMessage());
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
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return jwtToken;
    }

    public static List<LineItem> getSuspendedTransactionLineItems(int pauseId) {
        String query = "SELECT * FROM PausedLineItems WHERE PauseId = ?";
        List<LineItem> lineItems = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.createConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, pauseId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LineItem lineItem = new LineItem();
                lineItem.setProductCode(rs.getString("ProductCode"));
                lineItem.setDescription(rs.getString("Description"));
                lineItem.setUnitPrice(rs.getDouble("UnitPrice"));
                lineItem.setQuantity(rs.getDouble("Quantity"));
                lineItem.setTaxRateId(rs.getString("TaxRateID"));
                lineItem.setTotal(rs.getDouble("Total"));
                lineItem.setTotalVAT(rs.getDouble("TotalVAT"));
                lineItems.add(lineItem);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching line items: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return lineItems;
    }

    public static ObservableList<Invoice> fetchInvoices() {
        ObservableList<Invoice> invoices = FXCollections.observableArrayList();
        PreparedStatement pre = null;
        Connection conn = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Invoices";

        try {
            conn = DbConnection.createConnection();
            pre = conn.prepareStatement(query);
            rs = pre.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(5), rs.getDouble(6));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching invoices: " + e.getMessage());
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
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return invoices;
    }

    public static boolean isTerminalFullyActivated() {
        String query = "SELECT * FROM TerminalConfiguration WHERE IsActivated = 1 LIMIT 1";
        boolean isActivated = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.createConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                isActivated = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error while checking terminal activation status: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return isActivated;
    }

    public static ObservableList<LineItem> getLineSuspendedTransactionLineItems(int pauseId) {
        String query = "SELECT * FROM PausedLineItems WHERE PauseId = ?";
        ObservableList<LineItem> lineItems = FXCollections.observableArrayList();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.createConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, pauseId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LineItem lineItem = new LineItem();
                lineItem.setProductCode(rs.getString("ProductCode"));
                lineItem.setDescription(rs.getString("Description"));
                lineItem.setUnitPrice(rs.getDouble("UnitPrice"));
                lineItem.setQuantity(rs.getDouble("Quantity"));
                lineItem.setDiscount(0.0);
                lineItem.setControlsPane(POSHelper.setActionButtons(rs.getString("ProductCode"), lineItems));
                lineItem.setTaxRateId(rs.getString("TaxRateID"));
                lineItem.setTotal(rs.getDouble("Total"));
                lineItem.setTotalVAT(rs.getDouble("TotalVAT"));
                lineItems.add(lineItem);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching line items: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return lineItems;
    }

    public static List<Invoice> getUntransmittedInvoices() {
        String query = "SELECT * FROM Invoices WHERE State = 0";
        List<Invoice> invoices = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnection.createConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(5), rs.getDouble(6));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching untransmitted invoices: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return invoices;
    }

    public static ObservableList<PausedTransaction> getSuspendedTransactions() {
        ObservableList<PausedTransaction> suspendedTransactions = FXCollections.observableArrayList();
        String query = "SELECT * FROM PausedTransactions ORDER BY Timestamp DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SuspendedSalesController controller = new SuspendedSalesController();
        try {
            conn = DbConnection.createConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                PausedTransaction transaction = new PausedTransaction(rs.getInt(1), rs.getString(2), rs.getDouble(3));
                suspendedTransactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching suspended transactions: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }

        return suspendedTransactions;
    }

    public static void deletePausedTransactions(List<Integer> pausedIds) {
        PreparedStatement deleteLineItemsCommand = null;
        PreparedStatement deleteTransactionCommand = null;
        Connection connection = null;

        try {
            connection = DbConnection.createConnection();
            connection.setAutoCommit(false);

            for (Integer pausedId : pausedIds) {
                deleteLineItemsCommand = connection.prepareStatement("DELETE FROM PausedLineItems WHERE PauseId = ?");
                deleteLineItemsCommand.setInt(1, pausedId);
                deleteLineItemsCommand.executeUpdate();

                deleteTransactionCommand = connection.prepareStatement("DELETE FROM PausedTransactions WHERE PauseId = ?");
                deleteTransactionCommand.setInt(1, pausedId);
                deleteTransactionCommand.executeUpdate();
            }

            connection.commit();
        } catch (SQLException ex) {
            System.err.println("An error occurred while deleting paused transactions: " + ex.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
                if (deleteLineItemsCommand != null) {
                    deleteLineItemsCommand.close();
                }
                if (deleteTransactionCommand != null) {
                    deleteTransactionCommand.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
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
        double taxRate = 0.0;

        try {
            connection = DbConnection.createConnection();
            taxRateStmt = connection.prepareStatement(taxRateQuery);
            taxRateStmt.setString(1, taxRateId);

            taxRateRs = taxRateStmt.executeQuery();
            if (taxRateRs.next()) {
                taxRate = taxRateRs.getDouble("Rate");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
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

    public static String getTaxpayerSiteId() {
        String siteId = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            String query = "SELECT SiteId FROM TerminalSite LIMIT 1";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                siteId = resultSet.getString("SiteId");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching taxpayer SiteID: " + ex.getMessage());
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
                System.err.println("Error closing database resources: " + ex.getMessage());
            }
        }

        return siteId;
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

    public static Boolean userSignIn(String userName, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM Users WHERE UserName = ? AND Password = ? ";
        try {
            connection = DbConnection.createConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                USERNAME = resultSet.getString(4);
                USERTYPE = resultSet.getString(9);
                return Boolean.TRUE;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(POSHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(POSHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Boolean.FALSE;
    }

    public static void markInvoiceAsProcessed(String invoiceNumber) {
        String updateQuery = "UPDATE Invoices SET State = ? WHERE InvoiceNumber = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(updateQuery);

            statement.setInt(1, 1);
            statement.setString(2, invoiceNumber);

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
                tin = resultSet.getString(1);
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

    public static String fetchTerminalSecretKey() {
        String query = "SELECT SecretKey FROM TerminalKeys LIMIT 1";
        String terminalId = null;

        Connection connection = null;
        PreparedStatement terminalKeysStmt = null;
        ResultSet resultSet = null;

        try {

            connection = DbConnection.createConnection();
            terminalKeysStmt = connection.prepareStatement(query);
            resultSet = terminalKeysStmt.executeQuery();

            if (resultSet.next()) {
                terminalId = resultSet.getString("SecretKey");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching TerminalId: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (terminalKeysStmt != null) {
                    terminalKeysStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error closing resources: " + closeEx.getMessage());
            }
        }

        return terminalId;
    }

    public static boolean saveConfigurationDetails(TerminalConfiguration terminalConfiguration, TaxpayerConfiguration taxpayerConfiguration, TaxConfiguration globalConfiguration, ActivatedTerminal activatedTerminal, TerminalCredentials credentials) {
        boolean isSuccess = false;
        PreparedStatement terminalConfigStmt = null;
        PreparedStatement taxpayerConfigStmt = null;
        PreparedStatement terminalKeysStmt = null;
        PreparedStatement taxRatesStmt = null;
        PreparedStatement globalConfigStmt = null;

        try {
            Connection connection = DbConnection.createConnection();
            connection.setAutoCommit(false); // Enable transaction management

            // Insert TerminalConfiguration
            String terminalConfigSQL = "INSERT INTO TerminalConfiguration (TerminalLabel, EmailAddress, PhoneNumber, TradingName, AddressLine, VersionNo, MaxTransactionAgeInHours, MaxCummulativeAmount, IsActivated, IsActiveTerminal, SiteID, TerminalPosition) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            terminalConfigStmt = connection.prepareStatement(terminalConfigSQL);
            terminalConfigStmt.setString(1, terminalConfiguration.getTerminalLabel());
            terminalConfigStmt.setString(2, terminalConfiguration.getEmailAddress());
            terminalConfigStmt.setString(3, terminalConfiguration.getPhoneNumber());
            terminalConfigStmt.setString(4, terminalConfiguration.getTradingName());
            String addressLine = String.join(System.lineSeparator(), terminalConfiguration.getAddressLines());
            terminalConfigStmt.setString(5, addressLine);
            terminalConfigStmt.setInt(6, terminalConfiguration.getVersionNo());
            terminalConfigStmt.setInt(7, terminalConfiguration.getOfflineLimit().getMaxTransactionAgeInHours());
            terminalConfigStmt.setDouble(8, terminalConfiguration.getOfflineLimit().getMaxCummulativeAmount());
            terminalConfigStmt.setInt(9, 0);
            terminalConfigStmt.setInt(10, 1);
            terminalConfigStmt.setString(11, terminalConfiguration.getTerminalSite().getSiteId());
            terminalConfigStmt.setInt(12, activatedTerminal.getTerminalPosition());
            terminalConfigStmt.executeUpdate();

            // Insert TaxpayerConfiguration
            String taxpayerConfigSQL = "INSERT INTO TaxpayerConfiguration (TIN, IsVATRegistered, TaxOfficeCode, TaxOfficeName, VersionNo, TaxpayerId) VALUES (?, ?, ?, ?, ?, ?)";
            taxpayerConfigStmt = connection.prepareStatement(taxpayerConfigSQL);
            taxpayerConfigStmt.setString(1, taxpayerConfiguration.getTin());
            taxpayerConfigStmt.setBoolean(2, taxpayerConfiguration.isIsVATRegistered());
            taxpayerConfigStmt.setString(3, taxpayerConfiguration.getTaxOffice().getCode());
            taxpayerConfigStmt.setString(4, taxpayerConfiguration.getTaxOffice().getName());
            taxpayerConfigStmt.setInt(5, taxpayerConfiguration.getVersionNo());
            taxpayerConfigStmt.setLong(6, activatedTerminal.getTaxpayerId());
            taxpayerConfigStmt.executeUpdate();

            // Insert TerminalKeys
            String terminalKeysSQL = "INSERT INTO TerminalKeys (TerminalId, TaxpayerId, ActivationDate, JwtToken, SecretKey) VALUES (?, ?, ?, ?, ?)";
            terminalKeysStmt = connection.prepareStatement(terminalKeysSQL);
            terminalKeysStmt.setString(1, activatedTerminal.getTerminalId());
            terminalKeysStmt.setInt(2, (int) (long) activatedTerminal.getTaxpayerId());
            terminalKeysStmt.setString(3, activatedTerminal.getActivationDate());
            terminalKeysStmt.setString(4, credentials.getJwtToken());
            terminalKeysStmt.setString(5, credentials.getSecretKey());
            terminalKeysStmt.executeUpdate();

            // Insert TaxRates
            String taxRatesSQL = "INSERT INTO TaxRates (Id, Name, ChargeMode, Ordinal, Rate) VALUES (?, ?, ?, ?, ?)";
            taxRatesStmt = connection.prepareStatement(taxRatesSQL);
            for (TaxRate taxRate : globalConfiguration.getTaxRates()) {
                taxRatesStmt.setString(1, taxRate.getId());
                taxRatesStmt.setString(2, taxRate.getName());
                taxRatesStmt.setString(3, taxRate.getChargeMode());
                taxRatesStmt.setInt(4, taxRate.getOrdinal());
                taxRatesStmt.setDouble(5, taxRate.getRate());
                taxRatesStmt.executeUpdate();
            }

            // Insert GlobalConfiguration
            String globalConfigSQL = "INSERT INTO GlobalConfiguration (Id, VersionNo) VALUES (?, ?)";
            globalConfigStmt = connection.prepareStatement(globalConfigSQL);
            globalConfigStmt.setInt(1, 1); // Assuming a fixed ID for global configuration
            globalConfigStmt.setInt(2, globalConfiguration.getVersionNo());
            globalConfigStmt.executeUpdate();

            connection.commit(); // Commit the transaction
            isSuccess = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                if (globalConfigStmt != null) {
                    globalConfigStmt.getConnection().rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (terminalConfigStmt != null) {
                    terminalConfigStmt.close();
                }
                if (taxpayerConfigStmt != null) {
                    taxpayerConfigStmt.close();
                }
                if (terminalKeysStmt != null) {
                    terminalKeysStmt.close();
                }
                if (taxRatesStmt != null) {
                    taxRatesStmt.close();
                }
                if (globalConfigStmt != null) {
                    globalConfigStmt.close();
                }
            } catch (SQLException closeEx) {
                System.err.println(closeEx.getMessage());
            }
        }

        return isSuccess;
    }

    public static boolean doesTerminalKeyExist() {
        String query = "SELECT COUNT(*) FROM TerminalKeys";
        int count = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while fetching terminal keys: " + ex.getMessage());
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

        return count > 0;
    }

    public static boolean processTransaction(InvoiceHeader invoice, List<LineItem> lineItems, List<TaxBreakDown> taxBreakdowns, double total, double totalVAT) {
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

    public static boolean savePausedTransaction(List<LineItem> lineItems, int pausedId) {
        String insertPausedTransactionQuery = "INSERT INTO PausedTransactions (PauseId, Timestamp, Total) VALUES (?, ?, ?)";
        String insertPausedLineItemQuery = "INSERT INTO PausedLineItems (PauseId, ProductCode, Description, UnitPrice, Quantity, TaxRateID, Total, TotalVAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pausedTransactionStmt = null;
        PreparedStatement pausedLineItemStmt = null;
        Connection connection = null;

        double total = lineItems.stream().mapToDouble(LineItem::getTotal).sum();

        try {
            connection = DbConnection.createConnection();
            connection.setAutoCommit(false);

            // Save paused transaction
            pausedTransactionStmt = connection.prepareStatement(insertPausedTransactionQuery);
            pausedTransactionStmt.setInt(1, pausedId);
            pausedTransactionStmt.setString(2, LocalDate.now().toString());
            pausedTransactionStmt.setDouble(3, total);
            pausedTransactionStmt.executeUpdate();

            // Save paused line items
            for (LineItem lineItem : lineItems) {
                pausedLineItemStmt = connection.prepareStatement(insertPausedLineItemQuery);
                pausedLineItemStmt.setInt(1, pausedId);
                pausedLineItemStmt.setString(2, lineItem.getProductCode());
                pausedLineItemStmt.setString(3, lineItem.getDescription());
                pausedLineItemStmt.setDouble(4, lineItem.getUnitPrice());
                pausedLineItemStmt.setDouble(5, lineItem.getQuantity());
                pausedLineItemStmt.setString(6, lineItem.getTaxRateId());
                pausedLineItemStmt.setDouble(7, lineItem.getTotal());
                pausedLineItemStmt.setDouble(8, lineItem.getTotalVAT());
                pausedLineItemStmt.executeUpdate();
                pausedLineItemStmt.close();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("An error occurred suspending transaction: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pausedTransactionStmt != null) {
                    pausedTransactionStmt.close();
                }
                if (pausedLineItemStmt != null) {
                    pausedLineItemStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public static List<Integer> getUsedPauseIds() {
        List<Integer> usedIds = new ArrayList<>();
        String query = "SELECT PauseId FROM PausedTransactions";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.createConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pauseId = resultSet.getInt(1);
                usedIds.add(pauseId);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching used PauseIds: " + e.getMessage());
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

        return usedIds;
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
}
