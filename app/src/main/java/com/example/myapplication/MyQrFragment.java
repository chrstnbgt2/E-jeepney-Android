package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyQrFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView viewFirstNameOnly;
    private TextView viewFullName;
    private TextView phoneNumberTextView;

    public MyQrFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_qr, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/passenger");

        viewFirstNameOnly = view.findViewById(R.id.textViewUsername2);
        viewFullName = view.findViewById(R.id.textViewFirstName);
        phoneNumberTextView = view.findViewById(R.id.textViewPhoneNumber);

        fetchUserData();

        Button btnQRShare = view.findViewById(R.id.BtnQRShare);
        btnQRShare.setOnClickListener(v -> {
            QRFragment qrFragment = new QRFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, qrFragment)
                    .addToBackStack(null)
                    .commit();
        });

        Button btnQrSavePerma = view.findViewById(R.id.qrsaveperma);
        btnQrSavePerma.setOnClickListener(v -> saveQrCode());

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

                        viewFirstNameOnly.setText(firstName != null ? firstName : "First name not available");
                        viewFullName.setText(firstName != null && lastName != null ? firstName + " " + lastName : "Name not available");
                        phoneNumberTextView.setText(phoneNumber != null ? phoneNumber : "Phone number not available");
                    } else {
                        Log.e("UserData", "User data not found");
                        viewFirstNameOnly.setText("Data not found");
                        viewFullName.setText("Data not found");
                        phoneNumberTextView.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", databaseError.getMessage());
                }
            });
        } else {
            Log.e("UserAuth", "No authenticated user found");
        }
    }

    private void saveQrCode() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            QRCodeWriter writer = new QRCodeWriter();
            try {
                Bitmap bitmap = generateQrCodeBitmap(userId, writer);
                saveBitmapToStorage(bitmap, "qr_permanent.png");
            } catch (WriterException | IOException e) {
                Log.e("QRCodeError", "Failed to generate or save QR code", e);
                Toast.makeText(getContext(), "Failed to save QR code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap generateQrCodeBitmap(String content, QRCodeWriter writer) throws WriterException {
        int size = 512;
        return Bitmap.createBitmap(writer.encode(content, BarcodeFormat.QR_CODE, size, size).getWidth(),
                writer.encode(content, BarcodeFormat.QR_CODE, size, size).getHeight(), Bitmap.Config.ARGB_8888);
    }

    private void saveBitmapToStorage(Bitmap bitmap, String fileName) throws IOException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!directory.exists()) directory.mkdirs();

        File qrFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(qrFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(getContext(), "QR code saved to Downloads",Toast.LENGTH_SHORT).show();
        }
    }
}
