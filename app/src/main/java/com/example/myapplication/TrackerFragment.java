package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackerFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference jeepneysRef;

    public TrackerFragment() {
        // Required empty public constructor
    }

    public static TrackerFragment newInstance() {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jeepneysRef = FirebaseDatabase.getInstance().getReference("jeepneys");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchJeepneyLocations();
    }

    private void fetchJeepneyLocations() {
        jeepneysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear(); // Clear old markers
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Access location data directly
                    Double lat = snapshot.child("location").child("latitude").getValue(Double.class);
                    Double lng = snapshot.child("location").child("longitude").getValue(Double.class);
                    String plateNumber = snapshot.child("plateNumber").getValue(String.class);
                    String route = snapshot.child("route").getValue(String.class);

                    if (lat != null && lng != null) {
                        LatLng jeepneyLocation = new LatLng(lat, lng);
                        String title = plateNumber != null ? plateNumber : "Jeepney";
                        String snippet = route != null ? "Route: " + route : "";
                        mMap.addMarker(new MarkerOptions()
                                .position(jeepneyLocation)
                                .title(title)
                                .snippet(snippet));
                    }
                }

                // Optionally, adjust the camera to show all markers
                if (dataSnapshot.getChildrenCount() > 0) {
                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                    Double lat = firstChild.child("location").child("latitude").getValue(Double.class);
                    Double lng = firstChild.child("location").child("longitude").getValue(Double.class);
                    if (lat != null && lng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value: " + databaseError.toException());
            }
        });
    }
}
