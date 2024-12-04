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

public class HomeFragment extends Fragment {

    // Firebase variables
    private FirebaseAuth mAuth;
    private TextView textView8; // To display the first name

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the TextView for displaying the user's first name
        textView8 = view.findViewById(R.id.textView8);

        // Fetch and display the user's first name
        fetchUserFirstName();

        // Button navigation setup
        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            // Navigate to CashInFragment
            CashInFragment cashInFragment = new CashInFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, cashInFragment); // Replace with the new fragment
            transaction.addToBackStack(null); // Add to back stack
            transaction.commit(); // Commit the transaction
        });

        return view;
    }

    /**
     * Fetch and display the first name of the current user from Firebase Realtime Database.
     */
    @SuppressLint("SetTextI18n")
    private void fetchUserFirstName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users/passenger");

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        if (firstName != null) {
                            textView8.setText(firstName); // Set the first name to the TextView
                        } else {
                            Log.e("UserData", "First name not found.");
                            textView8.setText("Name not available"); // Fallback text
                        }
                    } else {
                        Log.e("UserData", "User data not found at users/passenger/" + uid);
                        textView8.setText("Data not found"); // Fallback text
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                    textView8.setText("Error fetching name"); // Fallback text
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found.");
            textView8.setText("No user logged in"); // Fallback text
        }
    }
}
