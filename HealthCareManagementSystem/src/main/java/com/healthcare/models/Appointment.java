package com.healthcare.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private LocalTime time;
    private String notes;
    private String status;

    // Constructors
    public Appointment() {
        this.status = "Scheduled";
    }
    
    public Appointment(int patientId, int doctorId, LocalDate date, LocalTime time, String notes) {
        this();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Appointment [ID=%d, Patient=%d, Doctor=%d, Date=%s, Time=%s]", 
            appointmentId, patientId, doctorId, date, time);
    }
}