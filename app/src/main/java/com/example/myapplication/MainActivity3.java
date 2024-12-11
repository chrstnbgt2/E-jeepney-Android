package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Import TextView
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    private EditText firstNameInput, middleNameInput, lastNameInput;
    private DatabaseReference usersRef, latestUserIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        latestUserIdRef = database.getReference("latestUserId");

        // Get references to EditText fields
        firstNameInput = findViewById(R.id.person);
        middleNameInput = findViewById(R.id.person2);
        lastNameInput = findViewById(R.id.person3);

        // Button click listener to navigate to MainActivity4
        Button register = findViewById(R.id.button4);
        register.setOnClickListener(v -> registerUser());

        // Set up TextView click listener to open MainActivity2
        TextView textView5 = findViewById(R.id.textView5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start MainActivity2
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String middleName = middleNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();

        // Check if any field is empty
        if (firstName.isEmpty() || middleName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the latest user ID and then register the user
        latestUserIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long latestUserId = dataSnapshot.getValue(Long.class);  // Get the last user ID
                if (latestUserId == null) {
                    latestUserId = 0L;  // Start from 0 if there is no ID yet
                }

                // Increment the user ID for the new user
                long newUserId = latestUserId + 1;

                // Proceed to the next activity with the generated userId
                Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("middleName", middleName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("userId", newUserId);
                startActivity(intent);

                // Update the latestUserId in Firebase
                latestUserIdRef.setValue(newUserId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity3.this, "Error fetching user ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
