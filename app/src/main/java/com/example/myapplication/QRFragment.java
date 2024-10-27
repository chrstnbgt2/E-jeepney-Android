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

public class QRFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private ImageView imageViewQR;

    public QRFragment() {
        // Required empty public constructor
    }

    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
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
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        imageViewQR = view.findViewById(R.id.imageView25);

        fetchUserData();

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // First check in the "passenger" node
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
                    // User found in this role's node
                    String qrUrl = dataSnapshot.child("qr").getValue(String.class);
                    if (qrUrl != null && !qrUrl.isEmpty()) {
                        loadQRImage(qrUrl);
                    } else {
                        Log.e("UserData", "QR URL is empty for " + role);
                    }
                } else {
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
