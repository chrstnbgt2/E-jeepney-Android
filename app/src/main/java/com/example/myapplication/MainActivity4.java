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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

        if (password.length() < 6) {
            Toast.makeText(MainActivity4.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                String uid = task.getResult().getUser().getUid();
                generateUserId(latestUserId -> {
                    if (latestUserId == null) {
                        Toast.makeText(MainActivity4.this, "Error generating user ID", Toast.LENGTH_SHORT).show();
                    } else {
                        String concatenatedId = latestUserId + latestUserId; // Concatenate latestUserId with itself
                        saveUserToDatabase(uid, concatenatedId, latestUserId, firstName, middleName, lastName, phoneNumber, email);
                    }
                });
            } else {
                Toast.makeText(MainActivity4.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateUserId(UserIdCallback callback) {
        DatabaseReference latestIdRef = FirebaseDatabase.getInstance().getReference("latestUserId");
        latestIdRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Integer latestId = task.getResult().getValue(Integer.class);
                if (latestId == null) latestId = 0;
                int newId = latestId + 1;

                // Update latestUserId in the database
                latestIdRef.setValue(newId).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        callback.onIdGenerated(String.valueOf(newId));
                    } else {
                        Log.e("DatabaseError", "Failed to update latestUserId", updateTask.getException());
                        callback.onIdGenerated(null);
                    }
                });
            } else {
                Log.e("DatabaseError", "Failed to fetch latestUserId", task.getException());
                callback.onIdGenerated("1"); // Default to ID 1 if task fails
            }
        });
    }

    private void saveUserToDatabase(String uid, String concatenatedId, String latestUserId, String firstName, String middleName, String lastName, String phoneNumber, String email) {
        try {
            String qrData = "ID: " + concatenatedId + "\nName: " + firstName + " " + middleName + " " + lastName + "\nPhone: " + phoneNumber + "\nEmail: " + email;

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference qrCodeRef = firebaseStorage.getReference().child("qrcodes/passenger/" + concatenatedId + "/acc_qr/qr.png");
            qrCodeRef.putBytes(data).addOnSuccessListener(taskSnapshot -> qrCodeRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("firstName", firstName);
                userDetails.put("middleName", middleName);
                userDetails.put("lastName", lastName);
                userDetails.put("phoneNumber", phoneNumber);
                userDetails.put("email", email);
                userDetails.put("qr", uri.toString());
                userDetails.put("wallet_balance", 0);
                userDetails.put("role", "passenger");
                userDetails.put("transaction", null);
                userDetails.put("user_id", concatenatedId);
                userDetails.put("timestamp", System.currentTimeMillis() / 1000L);

                databaseReference.child("passenger").child(uid).setValue(userDetails).addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity4.this, "Passenger registered successfully", Toast.LENGTH_SHORT).show();
                    navigateToNextActivity();
                }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity4.this, "Failed to save passenger: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(MainActivity4.this, "Failed to get QR code URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            })).addOnFailureListener(e -> {
                Toast.makeText(MainActivity4.this, "QR Code upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity4.this, "Error generating QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
        startActivity(intent);
        finish();
    }

    interface UserIdCallback {
        void onIdGenerated(String latestUserId);
    }
}
