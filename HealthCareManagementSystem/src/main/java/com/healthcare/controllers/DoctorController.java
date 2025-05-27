package com.healthcare.controllers;

import com.healthcare.models.Doctor;
import com.healthcare.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

public class DoctorController {
    @FXML private TextField nameField;
    @FXML private TextField specializationField;
    @FXML private TextField contactField;

    private boolean isEditMode = false;
    private Doctor currentDoctor;

    @FXML
    private void saveDoctor() {
        try {
            Doctor doctor = new Doctor(
                nameField.getText(),
                specializationField.getText(),
                contactField.getText()
            );

            if (isEditMode) {
                doctor.setDoctorId(currentDoctor.getDoctorId());
                updateDoctor(doctor);
            } else {
                saveToDatabase(doctor);
            }

            showAlert("Success", 
                isEditMode ? "Doctor updated successfully!" : "Doctor saved successfully!");
            closeForm();
            
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
        } catch (SQLException e) {
            showAlert("Database Error", "Error saving doctor: " + e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        closeForm();
    }

    public void setDoctorData(Doctor doctor) {
        this.currentDoctor = doctor;
        this.isEditMode = true;
        
        nameField.setText(doctor.getName());
        specializationField.setText(doctor.getSpecialization());
        contactField.setText(doctor.getContact());
    }

    private void saveToDatabase(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (name, specialization, contact) VALUES (?, ?, ?)";
        executeDoctorStatement(doctor, sql);
    }

    private void updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET name=?, specialization=?, contact=? WHERE doctor_id=?";
        executeDoctorStatement(doctor, sql);
    }

    private void executeDoctorStatement(Doctor doctor, String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getContact());
            
            if (isEditMode) {
                stmt.setInt(4, doctor.getDoctorId());
            }
            
            stmt.executeUpdate();
        }
    }

    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}