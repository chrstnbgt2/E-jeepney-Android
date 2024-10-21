package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private Map<String, Object> passenger;
    private Map<String, Object> conductor;
    private Map<String, Object> driver;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    // Constructor without password
    public User(String firstName, String middleName, String lastName, String phoneNumber, String email, String role) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;

        // Initialize nested role nodes
        this.passenger = initializePassengerNode();
        this.conductor = initializeConductorNode();
        this.driver = initializeDriverNode();  // Includes jeepney details if role is Driver
    }

    // Initialize the Passenger node
    private Map<String, Object> initializePassengerNode() {
        Map<String, Object> passengerNode = new HashMap<>();
        passengerNode.put("transaction", "");
        passengerNode.put("walletBalance", 0.0);
        passengerNode.put("qrCode", "");  // Empty placeholder for QR code
        return passengerNode;
    }

    // Initialize the Conductor node
    private Map<String, Object> initializeConductorNode() {
        Map<String, Object> conductorNode = new HashMap<>();
        conductorNode.put("transaction", "");
        conductorNode.put("walletBalance", 0.0);
        conductorNode.put("qrCode", "");  // Empty placeholder for QR code
        return conductorNode;
    }

    // Initialize the Driver node (including Jeepney details)
    private Map<String, Object> initializeDriverNode() {
        Map<String, Object> driverNode = new HashMap<>();
        driverNode.put("transaction", "");
        driverNode.put("walletBalance", 0.0);
        driverNode.put("qrCode", "");  // Empty placeholder for QR code
        driverNode.put("jeepney", initializeJeepneyNode());  // Include jeepney details within the driver node
        return driverNode;
    }

    // Initialize the Jeepney node
    private Map<String, Object> initializeJeepneyNode() {
        Map<String, Object> jeepneyNode = new HashMap<>();
        jeepneyNode.put("plateNo", "");
        jeepneyNode.put("capacity", 0);
        jeepneyNode.put("route", "");
        jeepneyNode.put("status", "");
        jeepneyNode.put("conductor", null);  // Leave conductor null for now
        return jeepneyNode;
    }

    // Getters and setters for role nodes
    public Map<String, Object> getPassenger() {
        return passenger;
    }

    public void setPassenger(Map<String, Object> passenger) {
        this.passenger = passenger;
    }

    public Map<String, Object> getConductor() {
        return conductor;
    }

    public void setConductor(Map<String, Object> conductor) {
        this.conductor = conductor;
    }

    public Map<String, Object> getDriver() {
        return driver;
    }

    public void setDriver(Map<String, Object> driver) {
        this.driver = driver;
    }

    // Other existing getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
