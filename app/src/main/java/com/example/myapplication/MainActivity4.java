package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView textView7;

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
        Button registerButton = findViewById(R.id.button5);
        textView7 = findViewById(R.id.textView7);

        // Retrieve intent extras
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String middleName = intent.getStringExtra("middleName");
        String lastName = intent.getStringExtra("lastName");

        // Set click listener for Register button
        registerButton.setOnClickListener(v -> registerUser(firstName, middleName, lastName));

        // Set click listener for textView7
        textView7.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(MainActivity4.this, MainActivity2.class);
            startActivity(mainActivityIntent);
        });
    }

    private void registerUser(String firstName, String middleName, String lastName) {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input fields
        if (phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity4.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            Toast.makeText(MainActivity4.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            generateAndUploadQRCode(user.getUid(), firstName, middleName, lastName, phoneNumber, email);
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(MainActivity4.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                            Toast.makeText(MainActivity4.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void generateAndUploadQRCode(String uid, String firstName, String middleName, String lastName, String phoneNumber, String email) {
        try {
            // QR Code data
            String qrData = "UID: " + uid + "\nName: " + firstName + " " + middleName + " " + lastName + "\nPhone: " + phoneNumber + "\nEmail: " + email;

            // Generate the QR code
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400);

            if (bitmap == null) {
                Log.e("QRCodeError", "Bitmap is null after QR code generation");
                Toast.makeText(MainActivity4.this, "Error generating QR code.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert bitmap to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            // Firebase Storage path for the QR code
            StorageReference qrCodeRef = firebaseStorage.getReference()
                    .child("qrcodes/passenger/" + uid + "/acc_qr/qr.png");

            // Upload the QR code to Firebase Storage
            qrCodeRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> qrCodeRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        if (uri != null) {
                            saveUserToDatabase(uid, firstName, middleName, lastName, phoneNumber, email, uri.toString());
                        } else {
                            Log.e("QRCodeError", "QR Code download URL is null");
                            Toast.makeText(MainActivity4.this, "Failed to get QR code URL.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("QRCodeError", "Failed to get download URL", e);
                        Toast.makeText(MainActivity4.this, "Failed to get QR code URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> {
                        Log.e("StorageError", "QR Code upload failed", e);
                        Toast.makeText(MainActivity4.this, "QR Code upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Log.e("QRCodeError", "Error generating QR Code", e);
            Toast.makeText(MainActivity4.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveUserToDatabase(String uid, String firstName, String middleName, String lastName, String phoneNumber, String email, String qrCodeUrl) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstName);
        userDetails.put("middleName", middleName);
        userDetails.put("lastName", lastName);
        userDetails.put("phoneNumber", phoneNumber);
        userDetails.put("email", email);
        userDetails.put("qr", qrCodeUrl);
        userDetails.put("wallet_balance", 0);
        userDetails.put("role", "passenger");
        userDetails.put("transaction", null);

        databaseReference.child("passenger").child(uid).setValue(userDetails)
                .addOnSuccessListener(aVoid -> {
                    Log.d("DatabaseSuccess", "Passenger saved successfully");
                    Toast.makeText(MainActivity4.this, "Passenger registered successfully", Toast.LENGTH_SHORT).show();
                    navigateToNextActivity();
                })
                .addOnFailureListener(e -> {
                    Log.e("DatabaseError", "Failed to save passenger to Firebase Database", e);
                    Toast.makeText(MainActivity4.this, "Failed to save passenger: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
        startActivity(intent);
        finish();
    }
}
