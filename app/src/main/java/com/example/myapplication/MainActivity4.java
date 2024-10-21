package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Initialize Firebase Auth and Storage
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI components
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.Email2);
        passwordEditText = findViewById(R.id.person3);
        roleSpinner = findViewById(R.id.roleSpinner);
        Button registerButton = findViewById(R.id.button5);

        // Create a list of roles
        List<String> roles = new ArrayList<>();
        roles.add("Select Role");
        roles.add("Passenger");
        roles.add("Conductor");
        roles.add("Driver");

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, roles);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setSelection(0);

        // Retrieve intent extras
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String middleName = intent.getStringExtra("middleName");
        String lastName = intent.getStringExtra("lastName");

        // Set click listener for Register button
        registerButton.setOnClickListener(v -> registerUser(firstName, middleName, lastName));
    }

    private void registerUser(String firstName, String middleName, String lastName) {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        if (phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity4.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            generateAndUploadQRCode(user.getUid(), role, firstName, middleName, lastName, phoneNumber, email);
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        Toast.makeText(MainActivity4.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void generateAndUploadQRCode(String uid, String role, String firstName, String middleName, String lastName, String phoneNumber, String email) {
        try {
            // QR Code data
            String qrData = "UID: " + uid + "\nName: " + firstName + " " + lastName + "\nRole: " + role;

            // Generate the QR code using ZXing
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400);

            if (bitmap == null) {
                Log.e("QRCodeError", "Bitmap is null after QR code generation");
                Toast.makeText(MainActivity4.this, "Error generating QR code.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert bitmap to byte array for upload
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            // Firebase Storage reference for storing the QR code image
            StorageReference qrCodeRef = firebaseStorage.getReference().child("qrcodes/" + uid + ".png");

            // Upload QR code to Firebase Storage
            UploadTask uploadTask = qrCodeRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> qrCodeRef.getDownloadUrl().addOnSuccessListener(uri -> {
                if (uri != null) {
                    saveUserToDatabase(uid, role, firstName, middleName, lastName, phoneNumber, email, uri.toString());
                } else {
                    Log.e("QRCodeError", "QR Code download URL is null");
                    Toast.makeText(MainActivity4.this, "Failed to get QR code URL.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("QRCodeError", "Failed to get download URL", e);
                Toast.makeText(MainActivity4.this, "Failed to get QR code URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            })).addOnFailureListener(e -> {
                Log.e("StorageError", "QR Code upload failed", e);
                Toast.makeText(MainActivity4.this, "QR Code upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            Log.e("QRCodeError", "Error generating QR Code", e);
            Toast.makeText(MainActivity4.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToDatabase(String uid, String role, String firstName, String middleName, String lastName, String phoneNumber, String email, String qrCodeUrl) {
        // Create a Map for user details
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstName);
        userDetails.put("middleName", middleName);
        userDetails.put("lastName", lastName);
        userDetails.put("phoneNumber", phoneNumber);
        userDetails.put("email", email);
        userDetails.put("role", role);
        userDetails.put("qr", qrCodeUrl);
        userDetails.put("wallet_balance", 0); // Default wallet balance
        userDetails.put("transaction", null); // Placeholder for future transactions

        // If the role is "Driver", add predefined conductor and jeepney details
        if (role.equals("Driver")) {
            userDetails.put("conductor", "Default Conductor Name"); // Predefined conductor
            userDetails.put("jeepney", "Default Jeepney Details");  // Predefined jeepney
        }

        // Save data to Firebase under appropriate node based on role
        switch (role) {
            case "Passenger":
                databaseReference.child("passenger").child(uid).setValue(userDetails)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DatabaseSuccess", "Passenger saved successfully");
                            Toast.makeText(MainActivity4.this, "Passenger registered successfully", Toast.LENGTH_SHORT).show();
                            navigateToNextActivity(role);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("DatabaseError", "Failed to save passenger to Firebase Database", e);
                            Toast.makeText(MainActivity4.this, "Failed to save passenger: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Conductor":
                databaseReference.child("conductor").child(uid).setValue(userDetails)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DatabaseSuccess", "Conductor saved successfully");
                            Toast.makeText(MainActivity4.this, "Conductor registered successfully", Toast.LENGTH_SHORT).show();
                            navigateToNextActivity(role);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("DatabaseError", "Failed to save conductor to Firebase Database", e);
                            Toast.makeText(MainActivity4.this, "Failed to save conductor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Driver":
                databaseReference.child("driver").child(uid).setValue(userDetails)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("DatabaseSuccess", "Driver saved successfully");
                            Toast.makeText(MainActivity4.this, "Driver registered successfully", Toast.LENGTH_SHORT).show();
                            navigateToNextActivity(role);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("DatabaseError", "Failed to save driver to Firebase Database", e);
                            Toast.makeText(MainActivity4.this, "Failed to save driver: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            default:
                Log.e("RoleError", "Unknown role selected");
                Toast.makeText(MainActivity4.this, "Unknown role selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void navigateToNextActivity(String role) {
        Intent intent;
        switch (role) {
            case "Conductor":
                intent = new Intent(MainActivity4.this, ConductorActivity.class);
                break;
            case "Driver":
                intent = new Intent(MainActivity4.this, DriverActivity.class);
                break;
            default:
                intent = new Intent(MainActivity4.this, MainActivity5.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
