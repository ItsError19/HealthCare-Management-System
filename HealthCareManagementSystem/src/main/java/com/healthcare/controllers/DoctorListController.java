package com.healthcare.controllers;

import com.healthcare.models.Doctor;
import com.healthcare.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class DoctorListController {
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colName;
    @FXML private TableColumn<Doctor, String> colSpecialization;
    @FXML private TableColumn<Doctor, String> colContact;
    @FXML private Button refreshButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML
    private void initialize() {
        setupColumns();
        loadDoctors();
        setupButtonActions();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    private void setupButtonActions() {
        refreshButton.setOnAction(e -> loadDoctors());
        addButton.setOnAction(e -> openDoctorForm(null));
        editButton.setOnAction(e -> {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openDoctorForm(selected);
            } else {
                showAlert("No Selection", "Please select a doctor to edit");
            }
        });
        deleteButton.setOnAction(e -> deleteSelectedDoctor());
    }

    private void loadDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String query = "SELECT * FROM doctors ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("contact")
                );
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctors.add(doctor);
            }
            
            doctorTable.setItems(doctors);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load doctors: " + e.getMessage());
        }
    }

    private void openDoctorForm(Doctor doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthcare/views/DoctorForm.fxml"));
            Parent root = loader.load();
            
            DoctorController controller = loader.getController();
            if (doctor != null) {
                controller.setDoctorData(doctor);
            }
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(doctor == null ? "Add New Doctor" : "Edit Doctor");
            stage.showAndWait();
            loadDoctors(); // Refresh table after form closes
            
        } catch (IOException e) {
            showAlert("Error", "Could not load doctor form: " + e.getMessage());
        }
    }

    private void deleteSelectedDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a doctor to delete");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Doctor");
        confirmation.setContentText("Are you sure you want to delete Dr. " + selected.getName() + "?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM doctors WHERE doctor_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, selected.getDoctorId());
                stmt.executeUpdate();
                
                showAlert("Success", "Doctor deleted successfully");
                loadDoctors();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete doctor: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}