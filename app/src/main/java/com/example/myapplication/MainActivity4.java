package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner; // Spinner for user roles

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI components
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.Email2);
        passwordEditText = findViewById(R.id.person3);
        roleSpinner = findViewById(R.id.roleSpinner); // Initialize Spinner
        Button registerButton = findViewById(R.id.button5);

        // Create a list of roles, including the hint
        List<String> roles = new ArrayList<>();
        roles.add("Select Role"); // This will be the hint
        roles.add("Passenger");
        roles.add("Conductor");
        roles.add("Driver");

        // Set up a simple ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, roles);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setSelection(0); // Show the hint at the top

        // Set up touch listener to handle hint
        roleSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (roleSpinner.getSelectedItemPosition() == 0) {
                    // If the user clicks on the hint, reset the selection
                    roleSpinner.setSelection(0);
                }
            }
            return false;
        });

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
        String role = roleSpinner.getSelectedItem().toString(); // Get selected role

        if (phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity4.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registered successfully
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Proceed to save additional user details in the database
                            saveUserToDatabase(firstName, middleName, lastName, phoneNumber, email, role, user.getUid());
                        }
                    } else {
                        // Registration failed, display error message
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        Toast.makeText(MainActivity4.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String firstName, String middleName, String lastName, String phoneNumber, String email, String role, String uid) {
        // Create the User object with role
        User user = new User(firstName, middleName, lastName, phoneNumber, email, role);

        // Store the user with the UID as the key
        databaseReference.child(uid).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity4.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    navigateToNextActivity(role);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity4.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseError", e.getMessage(), e);
                });
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
            case "Passenger":
            default:
                intent = new Intent(MainActivity4.this, MainActivity5.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
