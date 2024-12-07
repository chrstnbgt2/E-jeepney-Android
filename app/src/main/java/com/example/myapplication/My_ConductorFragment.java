package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_ConductorFragment extends Fragment {

    // Firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    // RecyclerView and Adapter
    private RecyclerView recyclerView;
    private ConductorAdapter adapter;
    private List<User> conductorList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_my__conductor, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.conductorRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conductorList = new ArrayList<>();
        adapter = new ConductorAdapter(getContext(), conductorList);
        recyclerView.setAdapter(adapter);

        // Load conductors from Firebase
        loadConductorData();

        return view;
    }

    private void loadConductorData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AuthError", "No authenticated user found.");
            return;
        }

        // Fetch all users with "conductor" role
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conductorList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && "conductor".equalsIgnoreCase(user.getRole())) {
                        conductorList.add(user);
                    }
                }

                // Notify the adapter to refresh the RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching conductors: " + error.getMessage());
                Toast.makeText(getContext(), "Error loading conductors.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
