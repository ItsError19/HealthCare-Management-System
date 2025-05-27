package com.healthcare.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {
    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
        // Load default view when dashboard opens
        try {
            loadDefaultView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultView() throws IOException {
        loadView("/com/healthcare/views/WelcomeView.fxml");
    }

    @FXML
    private void loadPatientForm() throws IOException {
        loadView("/com/healthcare/views/PatientForm.fxml");
    }

    @FXML
    private void loadPatientList() throws IOException {
        loadView("/com/healthcare/views/PatientListView.fxml");
    }

    @FXML
    private void loadDoctorForm() throws IOException {
        loadView("/com/healthcare/views/DoctorForm.fxml");
    }

    @FXML
    private void loadDoctorList() throws IOException {
        loadView("/com/healthcare/views/DoctorListView.fxml");
    }

    @FXML
    private void loadAppointmentForm() throws IOException {
        loadView("/com/healthcare/views/AppointmentForm.fxml");
    }

    private void loadView(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent fxml = loader.load();
            contentArea.getChildren().setAll(fxml);
            
            // Add fade transition for smooth view changes
            fxml.setOpacity(0);
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
                javafx.util.Duration.millis(300), fxml);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            throw e;
        }
    }
}