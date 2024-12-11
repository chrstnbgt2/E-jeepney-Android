package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConductorFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 100;
    private static final String TAG = "ConductorFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler;
    private Runnable locationUpdateTask;
    private TextView textView8; // Define TextView for firstName

    public ConductorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth and Database references
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize handler for periodic location updates
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conductor, container, false);

        // Initialize textView8
        textView8 = view.findViewById(R.id.textView8);

        // Request location permissions
        requestLocationPermissions();

        // Fetch and display the user's first name
        fetchAndDisplayFirstName();

        return view;
    }

    private void startLocationUpdates() {
        locationUpdateTask = new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            updateJeepneyLocation(location); // Update Firebase with location
                        } else {
                            Log.e(TAG, "Location is null");
                        }
                    }
                });

                // Schedule the next update after 1 second
                handler.postDelayed(locationUpdateTask, 1000);
            }
        };

        // Trigger the location update task
        handler.post(locationUpdateTask);
    }

    private void updateJeepneyLocation(Location location) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String conductorUid = currentUser.getUid();

            // Write latitude and longitude to `jeep_loc/{conductorUid}`
            DatabaseReference locationRef = databaseRef.child("jeep_loc").child(conductorUid);
            locationRef.child("latitude").setValue(location.getLatitude());
            locationRef.child("longitude").setValue(location.getLongitude());

            Log.d(TAG, "Location updated for UID: " + conductorUid);
        } else {
            Log.e(TAG, "User is not authenticated.");
        }
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Log.e(TAG, "Location permissions denied.");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop location updates when fragment is destroyed
        if (handler != null && locationUpdateTask != null) {
            handler.removeCallbacks(locationUpdateTask);
        }
    }

    private void fetchAndDisplayFirstName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference firstNameRef = databaseRef.child("users").child("driver").child(uid).child("firstName");

            firstNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.getValue(String.class);
                    if (firstName != null) {
                        textView8.setText(firstName);
                    } else {
                        Log.e(TAG, "First name is null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Failed to read first name", databaseError.toException());
                }
            });
        } else {
            Log.e(TAG, "User is not authenticated.");
        }
    }
}
