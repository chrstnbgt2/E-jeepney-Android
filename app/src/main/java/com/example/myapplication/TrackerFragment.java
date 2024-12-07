package com.example.myapplication;

import android.os.Bundle;
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
import androidx.annotation.NonNull;

public class TrackerFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference driversLocationRef;

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
        driversLocationRef = FirebaseDatabase.getInstance().getReference("driversLocations");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchDriverLocations();
    }

    private void fetchDriverLocations() {
        driversLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear(); // Clear old markers
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    double lat = snapshot.child("latitude").getValue(Double.class);
                    double lng = snapshot.child("longitude").getValue(Double.class);
                    LatLng driverLocation = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 15));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
            }
        });
    }
}
