package com.healthcare.controllers;

import com.healthcare.models.Appointment;
import com.healthcare.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentController {
    @FXML private ComboBox<Integer> patientCombo;
    @FXML private ComboBox<Integer> doctorCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextArea notesArea;
    @FXML private Label statusLabel;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private void initialize() {
        loadPatientIds();
        loadDoctorIds();
        setupDefaultValues();
        setupInputValidation();
    }

    private void setupDefaultValues() {
        datePicker.setValue(LocalDate.now());
        timeField.setText(LocalTime.now().format(timeFormatter));
        statusLabel.setText("");
    }

    private void setupInputValidation() {
        timeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("^\\d{0,2}:?\\d{0,2}:?\\d{0,2}$")) {
                timeField.setText(oldVal);
            }
        });
    }

    private void loadPatientIds() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT patient_id FROM patients ORDER BY patient_id")) {
            
            ObservableList<Integer> patientIds = FXCollections.observableArrayList();
            while (rs.next()) {
                patientIds.add(rs.getInt("patient_id"));
            }
            patientCombo.setItems(patientIds);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load patient IDs: " + e.getMessage());
        }
    }

    private void loadDoctorIds() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT doctor_id FROM doctors ORDER BY doctor_id")) {
            
            ObservableList<Integer> doctorIds = FXCollections.observableArrayList();
            while (rs.next()) {
                doctorIds.add(rs.getInt("doctor_id"));
            }
            doctorCombo.setItems(doctorIds);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load doctor IDs: " + e.getMessage());
        }
    }

    @FXML
    private void bookAppointment() {
        if (!validateInput()) return;
        
        try {
            Appointment appointment = createAppointmentFromForm();
            saveAppointment(appointment);
            showAlert("Success", "Appointment booked successfully!");
            clearFields();
        } catch (DateTimeParseException e) {
            showAlert("Invalid Time", "Please enter time in HH:MM:SS format");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to book appointment: " + e.getMessage());
        }
    }

    // Add this method to handle the Clear button action
    @FXML
    private void clearFields() {
        patientCombo.getSelectionModel().clearSelection();
        doctorCombo.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        timeField.setText(LocalTime.now().format(timeFormatter));
        notesArea.clear();
        statusLabel.setText("");
    }

    private Appointment createAppointmentFromForm() throws DateTimeParseException {
        return new Appointment(
            patientCombo.getValue(),
            doctorCombo.getValue(),
            datePicker.getValue(),
            LocalTime.parse(timeField.getText(), timeFormatter),
            notesArea.getText()
        );
    }

    private void saveAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, " +
                    "appointment_time, notes, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, Date.valueOf(appointment.getDate()));
            stmt.setTime(4, Time.valueOf(appointment.getTime()));
            stmt.setString(5, appointment.getNotes());
            stmt.setString(6, appointment.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
        }
    }

    private boolean validateInput() {
        if (patientCombo.getValue() == null) {
            statusLabel.setText("Please select a patient");
            return false;
        }
        
        if (doctorCombo.getValue() == null) {
            statusLabel.setText("Please select a doctor");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            statusLabel.setText("Please select a date");
            return false;
        }
        
        try {
            LocalTime.parse(timeField.getText(), timeFormatter);
        } catch (DateTimeParseException e) {
            statusLabel.setText("Invalid time format (HH:MM:SS)");
            return false;
        }
        
        statusLabel.setText("");
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}