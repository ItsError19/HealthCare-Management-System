package com.healthcare.controllers;

import com.healthcare.models.Patient;
import com.healthcare.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class PatientController {
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField contactField;
    @FXML private TextArea addressArea;
    
    private Patient currentPatient;
    private boolean isEditMode = false;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @FXML
    private void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");
        setupFormValidation();
    }

    public void setPatientData(Patient patient) {
        this.currentPatient = patient;
        this.isEditMode = true;
        
        nameField.setText(patient.getName());
        ageField.setText(String.valueOf(patient.getAge()));
        genderCombo.setValue(patient.getGender());
        contactField.setText(patient.getContact());
        addressArea.setText(patient.getAddress());
        
        saveButton.setText("Update Patient");
    }

    @FXML
    private void savePatient() {
        if (!validateForm()) return;
        
        try {
            Patient patient = new Patient(
                nameField.getText().trim(),
                Integer.parseInt(ageField.getText()),
                genderCombo.getValue(),
                contactField.getText().trim(),
                addressArea.getText().trim()
            );

            if (isEditMode) {
                patient.setPatientId(currentPatient.getPatientId());
                updatePatient(patient);
            } else {
                saveToDatabase(patient);
            }
            
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                isEditMode ? "Patient updated successfully!" : "Patient saved successfully!");
            closeForm();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid age!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        closeForm();
    }

    private void saveToDatabase(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (name, age, gender, contact, address) VALUES (?, ?, ?, ?, ?)";
        executePatientStatement(patient, sql);
    }

    private void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name=?, age=?, gender=?, contact=?, address=? WHERE patient_id=?";
        executePatientStatement(patient, sql);
    }

    private void executePatientStatement(Patient patient, String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getContact());
            stmt.setString(5, patient.getAddress());
            
            if (isEditMode) {
                stmt.setInt(6, patient.getPatientId());
            }
            
            stmt.executeUpdate();
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter patient name");
            return false;
        }
        
        if (ageField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter patient age");
            return false;
        }
        
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0 || age > 120) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid age (1-120)");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Age must be a number");
            return false;
        }
        
        if (genderCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select gender");
            return false;
        }
        
        if (contactField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter contact number");
            return false;
        }
        
        return true;
    }

    private void setupFormValidation() {
        ageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        contactField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                contactField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void closeForm() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}