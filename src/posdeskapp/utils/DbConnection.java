/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author biphiri
 */
public class DbConnection {

    public static Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");

            SQLiteConfig configuration = new SQLiteConfig();
            configuration.enforceForeignKeys(true); // Enforce foreign key constraints

            // Establish connection
            Connection conn = DriverManager.getConnection("jdbc:sqlite:POS.db", configuration.toProperties());

            // Enable WAL mode
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA busy_timeout = 5000;");
            }

            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }

    public static void initializeDatabase() {
        Map<String, String> tables = new HashMap<>();

        tables.put("Users", "CREATE TABLE Users (UserID INTEGER NOT NULL, FirstName VARCHAR(50) NOT NULL, LastName VARCHAR(50) NOT NULL, UserName VARCHAR(50) NOT NULL UNIQUE, PhoneNumber VARCHAR(25), EmailAddress VARCHAR(100) UNIQUE, Address TEXT, Role VARCHAR(7) NOT NULL CHECK(Role IN ('ADMIN', 'CASHIER')), Password VARCHAR(255) NOT NULL, PRIMARY KEY(UserID AUTOINCREMENT))");
        tables.put("TerminalKeys", "CREATE TABLE TerminalKeys (TerminalId TEXT NOT NULL, TaxpayerId TEXT NOT NULL, ActivationDate DATETIME NOT NULL, JwtToken TEXT NOT NULL, SecretKey TEXT NOT NULL, ActivationCode TEXT, PRIMARY KEY(TerminalId, TaxpayerId))");
        tables.put("TerminalConfiguration", "CREATE TABLE TerminalConfiguration (TerminalLabel TEXT NOT NULL, EmailAddress TEXT NOT NULL, PhoneNumber TEXT NOT NULL, TradingName TEXT NOT NULL, AddressLine TEXT NOT NULL, VersionNo INTEGER NOT NULL, MaxTransactionAgeInHours INTEGER NOT NULL, MaxCummulativeAmount REAL NOT NULL, IsActivated INTEGER NOT NULL, IsActiveTerminal INTEGER NOT NULL, SiteID TEXT, TerminalPosition INTEGER, PRIMARY KEY(TerminalLabel))");
        tables.put("TaxpayerConfiguration", "CREATE TABLE TaxpayerConfiguration (TIN TEXT NOT NULL, IsVATRegistered BOOLEAN NOT NULL, TaxOfficeCode TEXT NOT NULL, TaxOfficeName TEXT NOT NULL, VersionNo INTEGER NOT NULL, TaxpayerId INTEGER, PRIMARY KEY(TIN))");
        tables.put("TaxRates", "CREATE TABLE TaxRates (Id TEXT NOT NULL, Name TEXT NOT NULL, ChargeMode TEXT NOT NULL, Ordinal INTEGER NOT NULL, Rate REAL NOT NULL, PRIMARY KEY(Id))");
        tables.put("Products", "CREATE TABLE Products (ProductCode TEXT NOT NULL PRIMARY KEY, ProductName TEXT NOT NULL, Description TEXT NOT NULL, Quantity REAL NOT NULL, UnitOfMeasure TEXT, Price REAL NOT NULL, SiteId TEXT, ProductExpiryDate TEXT, MinimumStockLevel REAL, TaxRateId TEXT)");
        tables.put("PausedTransactions", "CREATE TABLE PausedTransactions (PauseId INTEGER, Timestamp DATETIME, Total REAL, PRIMARY KEY(PauseId))");
        tables.put("PausedLineItems", "CREATE TABLE PausedLineItems (ID INTEGER, ProductCode TEXT, Description TEXT, UnitPrice REAL, Quantity REAL, PauseID TEXT, TaxRateID TEXT, Total REAL, TotalVAT REAL, PRIMARY KEY(ID))");
        tables.put("LineItems", "CREATE TABLE LineItems (ID INTEGER, ProductCode TEXT, Description TEXT, UnitPrice REAL, Quantity REAL, InvoiceNumber TEXT, TaxRateID TEXT, Discount REAL, TotalVAT REAL, PRIMARY KEY(ID))");
        tables.put("Invoices", "CREATE TABLE Invoices (InvoiceNumber TEXT, InvoiceDateTime DATETIME, InvoiceTotal REAL, SellerTin TEXT, BuyerTIN TEXT, TotalVat REAL, TransmissionState INTEGER, PRIMARY KEY(InvoiceNumber))");
        tables.put("InvoiceTaxBreakDown", "CREATE TABLE InvoiceTaxBreakDown (InvoiceNumber TEXT, RateID TEXT, TaxableAmount REAL, TaxAmount REAL)");
        tables.put("GlobalConfiguration", "CREATE TABLE GlobalConfiguration (Id INTEGER NOT NULL, VersionNo INTEGER NOT NULL)");
        tables.put("Discounts", "CREATE TABLE Discounts (DiscountID INTEGER PRIMARY KEY AUTOINCREMENT, ProductCode TEXT NOT NULL, DiscountPercentage DECIMAL(5,2) NOT NULL, StartDate DATETIME NOT NULL, EndDate DATETIME NOT NULL, FOREIGN KEY(ProductCode) REFERENCES Products(ProductCode))");

        tables.entrySet().forEach((entry) -> {
            checkTable(entry.getKey(), entry.getValue());
        });
    }

    public static void checkTable(String tableName, String query) {
        Statement st = null;
        Connection conn = null;
        try {
            conn = createConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, null);
            if (rs.next()) {
            } else {
                st = conn.createStatement();
                st.execute(query);
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
}
