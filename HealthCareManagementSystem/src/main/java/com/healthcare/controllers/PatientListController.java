package com.healthcare.controllers;

import com.healthcare.models.Patient;
import com.healthcare.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class PatientListController {
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, String> colContact;
    @FXML private TableColumn<Patient, String> colAddress;

    @FXML
    private void initialize() {
        setupColumns();
        loadPatients();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    @FXML
    private void loadPatients(ActionEvent event) {
        loadPatients();
    }

    private void loadPatients() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT * FROM patients";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("contact"),
                    rs.getString("address")
                );
                patient.setPatientId(rs.getInt("patient_id"));
                patients.add(patient);
            }
            
            patientTable.setItems(patients);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load patients: " + e.getMessage());
        }
    }

    @FXML
    private void loadPatientForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthcare/views/PatientForm.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Patient");
            stage.showAndWait();
            
            // Refresh table after form is closed
            loadPatients();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load patient form: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}