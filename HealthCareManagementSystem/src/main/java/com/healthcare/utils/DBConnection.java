package com.healthcare.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    // XAMPP Default Configuration
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 4306; // XAMPP default MySQL port
    private static final String DB_NAME = "healthcare";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // XAMPP default empty password
    
    private static final String JDBC_URL = String.format(
        "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true", 
        DB_HOST, DB_PORT, DB_NAME);
    
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.put("user", DB_USER);
        props.put("password", DB_PASSWORD);
        props.put("autoReconnect", "true");
        props.put("characterEncoding", "UTF-8");
        props.put("useUnicode", "true");
        props.put("connectTimeout", "3000");
        props.put("socketTimeout", "30000");

        SQLException lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(JDBC_URL, props);
                
                if (conn.isValid(2)) {
                    return conn;
                }
                conn.close();
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found. Add mysql-connector-java to dependencies", e);
            } catch (SQLException e) {
                lastException = e;
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection retry interrupted", ie);
                    }
                }
            }
        }
        
        throw new SQLException(
            String.format("Failed to connect to XAMPP MySQL after %d attempts", MAX_RETRIES), 
            lastException);
    }

    public static boolean testXAMPPConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Successfully connected to XAMPP MySQL!");
            return true;
        } catch (SQLException e) {
            System.err.println("XAMPP Connection Failed:");
            System.err.println("URL: " + JDBC_URL);
            System.err.println("Error: " + e.getMessage());
            System.err.println("Ensure:");
            System.err.println("1. XAMPP MySQL is running");
            System.err.println("2. Port 4306 is not blocked");
            System.err.println("3. Database 'healthcare' exists");
            return false;
        }
    }
}