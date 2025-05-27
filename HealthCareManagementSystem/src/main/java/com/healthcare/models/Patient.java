package com.healthcare.models;

import java.time.LocalDate;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String address;
    private LocalDate registrationDate;
    private String bloodType;
    private String allergies;
    private String medicalNotes;

    // Constructors
    public Patient() {
        this.registrationDate = LocalDate.now();
    }

    public Patient(String name, int age, String gender, String contact, String address) {
        this();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }

    // Full constructor
    public Patient(int patientId, String name, int age, String gender, String contact, 
                 String address, LocalDate registrationDate, String bloodType, 
                 String allergies, String medicalNotes) {
        this(name, age, gender, contact, address);
        this.patientId = patientId;
        this.registrationDate = registrationDate;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalNotes = medicalNotes;
    }

    // Getters and Setters
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim(); 
    }
    
    public int getAge() { return age; }
    public void setAge(int age) { 
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120");
        }
        this.age = age; 
    }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { 
        if (!"Male".equals(gender) && !"Female".equals(gender) && !"Other".equals(gender)) {
            throw new IllegalArgumentException("Invalid gender value");
        }
        this.gender = gender; 
    }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { 
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }
        this.contact = contact.trim(); 
    }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { 
        this.registrationDate = registrationDate; 
    }
    
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }

    // Utility methods
    public boolean isAdult() {
        return age >= 18;
    }

    public String getFormattedContact() {
        if (contact == null || contact.length() < 10) return contact;
        return String.format("(%s) %s-%s", 
            contact.substring(0, 3), 
            contact.substring(3, 6), 
            contact.substring(6));
    }

    @Override
    public String toString() {
        return String.format("Patient [ID=%d, Name=%s, Age=%d, Gender=%s]", 
            patientId, name, age, gender);
    }
}