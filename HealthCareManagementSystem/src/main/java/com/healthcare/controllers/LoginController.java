package com.healthcare.controllers;

import com.healthcare.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = SHA1(?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Get current stage
                Stage stage = (Stage) usernameField.getScene().getWindow();
                
                // Load dashboard
                Parent root = FXMLLoader.load(getClass().getResource("/com/healthcare/views/MainDashboard.fxml"));
                
                // Set new scene
                stage.setScene(new Scene(root));
                stage.setTitle("Healthcare Dashboard");
                stage.show();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errorLabel.setText("Failed to load dashboard");
            e.printStackTrace();
        }
    }
}