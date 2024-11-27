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

    // TextViews for displaying firstName and phoneNumber
    private TextView firstNameTextView;
    private TextView phoneNumberTextView;

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
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize TextViews for firstName and phoneNumber
        firstNameTextView = view.findViewById(R.id.textViewFirstName);
        phoneNumberTextView = view.findViewById(R.id.textViewPhoneNumber);

        // Fetch and display user data from Firebase
        fetchUserData();

        // Set up the button click listener for BtnQRShare
        Button btnQRShare = view.findViewById(R.id.BtnQRShare);
        btnQRShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the QRFragment when the button is clicked
                QRFragment qrFragment = new QRFragment();

                // Begin the fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, qrFragment);  // Replace the container with QRFragment
                transaction.addToBackStack(null);  // Optional: Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve firstName and phoneNumber
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                        // Set the retrieved values to the TextViews
                        if (firstName != null) {
                            firstNameTextView.setText(firstName);
                        } else {
                            firstNameTextView.setText("First name not found");
                        }

                        if (phoneNumber != null) {
                            phoneNumberTextView.setText(phoneNumber);
                        } else {
                            phoneNumberTextView.setText("Phone number not found");
                        }
                    } else {
                        Log.e("UserData", "User data not found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found.");
        }
    }
}
