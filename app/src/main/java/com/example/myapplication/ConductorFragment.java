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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConductorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConductorFragment extends Fragment {

    // Define ARG_PARAM1 and ARG_PARAM2 as constants
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;

    // Firebase authentication and database references
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private TextView textView8;

    public ConductorFragment() {
        // Required empty public constructor
    }

    public static ConductorFragment newInstance(String param1, String param2) {
        ConductorFragment fragment = new ConductorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize the database reference to the users/driver node
        usersRef = FirebaseDatabase.getInstance().getReference("users/driver");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conductor, container, false);

        // Initialize the TextView
        textView8 = view.findViewById(R.id.textView8);

        // Fetch the driver's first name from Firebase
        fetchDriverFirstName();

        // Find the button2 by its ID
        Button button2 = view.findViewById(R.id.button2);

        // Set an OnClickListener for button2
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of CashInFragment
                CashInFragment cashInFragment = new CashInFragment();

                // Begin a fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with CashInFragment
                transaction.replace(R.id.frame_conductor, cashInFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit(); // Commit the transaction
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void fetchDriverFirstName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        if (firstName != null) {
                            textView8.setText(firstName);
                        } else {
                            Log.e("UserData", "First name not found.");
                            textView8.setText("Name not available");
                        }
                    } else {
                        Log.e("UserData", "User data not found at users/driver/" + uid);
                        textView8.setText("Data not found");
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
                    textView8.setText("Error fetching name");
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found.");
            textView8.setText("No user logged in");
        }
    }
}
