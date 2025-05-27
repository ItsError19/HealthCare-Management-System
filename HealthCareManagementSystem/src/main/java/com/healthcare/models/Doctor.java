package com.healthcare.models;

public class Doctor {
    private int doctorId;
    private String name;
    private String specialization;
    private String contact;

    // Constructors
    public Doctor() {}

    public Doctor(String name, String specialization, String contact) {
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
    }

    // Getters and Setters with validation
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty");
        }
        this.name = name.trim();
    }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
        this.specialization = specialization.trim();
    }
    
    public String getContact() { return contact; }
    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }
        this.contact = contact.trim();
    }

    @Override
    public String toString() {
        return String.format("Dr. %s (%s)", name, specialization);
    }
}