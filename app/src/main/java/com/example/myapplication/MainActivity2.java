package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    private EditText emailAddress;
    private EditText passcodeTxt;
    private ImageView showHidePassIcon;
    private boolean isPasswordVisible = false;
    private ProgressBar progressBar;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database Reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Initialize the EditTexts, ImageView, Button, and ProgressBar
        emailAddress = findViewById(R.id.EmailAddressTxt);
        passcodeTxt = findViewById(R.id.passcodetxt);
        showHidePassIcon = findViewById(R.id.showHidePassIcon);
        progressBar = findViewById(R.id.progressBar);
        Button login = findViewById(R.id.LogInButton);

        // Initialize the TextView and set OnClickListener
        TextView textView5 = findViewById(R.id.textView5);
        textView5.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            startActivity(intent);
        });

        // Set the initial input type to password
        passcodeTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // Set up click listener to toggle password visibility
        showHidePassIcon.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide Password
                passcodeTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showHidePassIcon.setImageResource(R.drawable.ic_showpass);  // Update icon
                passcodeTxt.setTransformationMethod(new PasswordTransformationMethod());
            } else {
                // Show Password
                passcodeTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showHidePassIcon.setImageResource(R.drawable.hide_pass);  // Update icon
                passcodeTxt.setTransformationMethod(null);
            }
            // Move the cursor to the end of the text
            passcodeTxt.setSelection(passcodeTxt.length());
            isPasswordVisible = !isPasswordVisible;
        });

        // Set up login button click listener
        login.setOnClickListener(v -> {
            String email = emailAddress.getText().toString().trim();
            String password = passcodeTxt.getText().toString().trim();

            // Input validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity2.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(MainActivity2.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);  // Show the ProgressBar
            login.setEnabled(false);  // Disable login button to prevent multiple clicks

            Log.d("LoginAttempt", "Email: " + email + ", Password: " + password);

            // Attempt to sign in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity2.this, task -> {
                        progressBar.setVisibility(View.GONE);  // Hide the ProgressBar
                        login.setEnabled(true);  // Enable the login button

                        if (task.isSuccessful()) {
                            // User logged in successfully
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Log.d("LoginSuccess", "User logged in successfully: " + user.getUid());
                                fetchUserRoleAndNavigate(user.getEmail());
                            }
                        } else {
                            // Login failed, display error message
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                            Log.e("LoginError", "Login failed: " + errorMessage);
                            Toast.makeText(MainActivity2.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    /**
     * Fetch the user's role from the database using their email and navigate accordingly.
     *
     * @param email The email of the user.
     */
    private void fetchUserRoleAndNavigate(String email) {
        searchUserRole(email, "driver", role -> {
            if (role != null) {
                navigateToNextActivity(role);
            } else {
                searchUserRole(email, "conductor", role2 -> {
                    if (role2 != null) {
                        navigateToNextActivity(role2);
                    } else {
                        searchUserRole(email, "passenger", role3 -> {
                            if (role3 != null) {
                                navigateToNextActivity(role3);
                            } else {
                                Log.e("UserRole", "User role not found.");
                                Toast.makeText(MainActivity2.this, "User role not found.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Search for the user's role in the specified node.
     *
     * @param email  The email of the user.
     * @param role   The role node to search in.
     * @param callback The callback to return the result.
     */
    private void searchUserRole(String email, String role, UserRoleCallback callback) {
        usersRef.child(role).orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onRoleFound(role);
                } else {
                    callback.onRoleFound(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                Toast.makeText(MainActivity2.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navigate to the appropriate activity based on the user's role.
     *
     * @param role The role of the user (passenger, conductor, driver).
     */
    private void navigateToNextActivity(String role) {
        Intent intent;
        switch (role.toLowerCase()) {
            case "conductor":
                intent = new Intent(MainActivity2.this, DriverActivity.class);
                break;
            case "driver":
                intent = new Intent(MainActivity2.this, ConductorActivity.class);
                break;
            case "passenger":
            default:
                intent = new Intent(MainActivity2.this, MainActivity5.class);
                break;
        }
        try {
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("NavigationError", "Error navigating to the next activity: " + e.getMessage());
            Toast.makeText(MainActivity2.this, "Error navigating to the next activity.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback interface for user role search.
     */
    private interface UserRoleCallback {
        void onRoleFound(String role);
    }
}
