package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyQrFragment extends Fragment {

    // Firebase Auth and Database references
    private FirebaseAuth mAuth;
    private ImageView imageViewQR;

    public MyQrFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);

        // Initialize ImageView for QR code
        imageViewQR = view.findViewById(R.id.imageView1);

        // Fetch and display user data from Firebase
        fetchUserData();

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Check in the "passenger" node first
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            checkUserInNode(usersRef.child("passenger").child(userId), "Passenger");
        } else {
            Log.e("UserAuth", "No authenticated user found.");
        }
    }

    private void checkUserInNode(DatabaseReference userRef, String role) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found, load the QR code URL
                    String qrUrl = dataSnapshot.child("qr").getValue(String.class);
                    if (qrUrl != null && !qrUrl.isEmpty()) {
                        loadQRImage(qrUrl);
                    } else {
                        Log.e("UserData", "QR URL is empty for " + role);
                    }
                } else {
                    // If user not found in this node, check in the next node
                    if (role.equals("Passenger")) {
                        checkUserInNode(FirebaseDatabase.getInstance().getReference("users/conductor").child(userRef.getKey()), "Conductor");
                    } else if (role.equals("Conductor")) {
                        checkUserInNode(FirebaseDatabase.getInstance().getReference("users/driver").child(userRef.getKey()), "Driver");
                    } else {
                        Log.e("UserData", "User data not found in any role.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error retrieving user data: " + databaseError.getMessage());
            }
        });
    }

    private void loadQRImage(String qrUrl) {
        // Create a reference to the QR code image in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(qrUrl);

        // Download the image as a byte array
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Convert the byte array to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // Display the bitmap in the ImageView
            imageViewQR.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            Log.e("StorageError", "Failed to load QR image: " + exception.getMessage());
        });
    }
}
