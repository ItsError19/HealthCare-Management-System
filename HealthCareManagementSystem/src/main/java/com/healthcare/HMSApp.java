package com.healthcare;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HMSApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login form
    	// Try with and without leading slash if one doesn't work
    	URL fxmlUrl = getClass().getResource("/com/healthcare/views/LoginForm.fxml");
    	if (fxmlUrl == null) {
    	    System.err.println("FXML file not found!");
    	    return;
    	}
    	Parent root = FXMLLoader.load(fxmlUrl);
        
        // Create and configure the main scene
        Scene scene = new Scene(root, 400, 300);
        
        // Set up the primary stage
        primaryStage.setTitle("Healthcare Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/healthcare/resources/icon.png")));
        } catch (Exception e) {
            System.out.println("Could not load application icon: " + e.getMessage());
        }
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Initialize any application-wide configurations here if needed
        launch(args);
    }
}