package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyQrFragment extends Fragment {

    // Firebase Auth and Database references
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // TextViews for displaying user data
    private TextView viewFirstNameOnly; // First Name only
    private TextView viewFullName; // Concatenated First Name and Last Name
    private TextView phoneNumberTextView; // Phone Number

    public MyQrFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/passenger"); // Targeting the passenger node

        // Initialize TextViews for user data
        viewFirstNameOnly = view.findViewById(R.id.textViewUsername2); // First Name only
        viewFullName = view.findViewById(R.id.textViewFirstName); // Concatenated First and Last Name
        phoneNumberTextView = view.findViewById(R.id.textViewPhoneNumber); // Phone Number

        // Fetch and display user data from Firebase
        fetchUserData();

        // Set up the button click listener for BtnQRShare
        Button btnQRShare = view.findViewById(R.id.BtnQRShare);
        btnQRShare.setOnClickListener(v -> {
            // Open the QRFragment when the button is clicked
            QRFragment qrFragment = new QRFragment();

            // Begin the fragment transaction
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, qrFragment); // Replace the container with QRFragment
            transaction.addToBackStack(null); // Add to back stack to allow navigation back
            transaction.commit(); // Commit the transaction
        });

        return view;
    }

    /**
     * Fetch user data (firstName, lastName, phoneNumber) from Firebase Realtime Database.
     */
    @SuppressLint("SetTextI18n")
    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user details
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                        // Display First Name only
                        if (firstName != null) {
                            viewFirstNameOnly.setText(firstName);
                        } else {
                            viewFirstNameOnly.setText("First name not available");
                        }

                        // Display concatenated First Name and Last Name
                        if (firstName != null && lastName != null) {
                            viewFullName.setText(firstName + " " + lastName);
                        } else if (firstName != null) {
                            viewFullName.setText(firstName);
                        } else if (lastName != null) {
                            viewFullName.setText(lastName);
                        } else {
                            viewFullName.setText("Name not available");
                        }

                        // Display Phone Number
                        if (phoneNumber != null) {
                            phoneNumberTextView.setText(phoneNumber);
                        } else {
                            phoneNumberTextView.setText("Phone number not available");
                        }
                    } else {
                        Log.e("UserData", "User data not found at users/passenger/" + uid);
                        viewFirstNameOnly.setText("Data not found");
                        viewFullName.setText("Data not found");
                        phoneNumberTextView.setText("");
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                    viewFirstNameOnly.setText("Error fetching data");
                    viewFullName.setText("Error fetching data");
                    phoneNumberTextView.setText("");
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found.");
            viewFirstNameOnly.setText("No user logged in");
            viewFullName.setText("No user logged in");
            phoneNumberTextView.setText("");
        }
    }
}
