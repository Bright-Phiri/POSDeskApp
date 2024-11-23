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
import org.sqlite.SQLiteConfig;

/**
 *
 * @author biphiri
 */
public class DbConnection {

    public static Connection Connect() {
        try {
            Class.forName("org.sqlite.JDBC");

            SQLiteConfig configuration = new SQLiteConfig();
            configuration.enforceForeignKeys(true); // Enforce foreign key constraints

            // Establish connection
            Connection conn = DriverManager.getConnection("jdbc:sqlite:POS.db", configuration.toProperties());

            // Enable WAL mode
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");
            }

            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }

    public static void checkTable(String tableName, String query) {
        Statement st = null;
        Connection conn = null;
        try {
            conn = Connect();
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
