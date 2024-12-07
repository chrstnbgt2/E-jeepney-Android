package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView textViewFirstName;
    private ImageView imageView25; // Updated ImageView for QR code
    private Button button7;       // Button to trigger download

    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        // Initialize views
        textViewFirstName = view.findViewById(R.id.textViewFirstNameQR);
        imageView25 = view.findViewById(R.id.imageView25); // Ensure this ID exists in XML
        button7 = view.findViewById(R.id.button7);         // Ensure this ID exists in XML

        // Fetch and display user data
        fetchUserData();

        // Set a click listener for the download button
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImageViewAsQR();
            }
        });

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users/passenger");
            usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        textViewFirstName.setText(firstName != null ? firstName : "First name not available");
                    } else {
                        textViewFirstName.setText("User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    textViewFirstName.setText("Error fetching data");
                    Log.e("DatabaseError", "Error: " + databaseError.getMessage());
                }
            });
        } else {
            textViewFirstName.setText("No user logged in");
        }
    }

    private void downloadImageViewAsQR() {
        if (imageView25.getDrawable() == null) {
            Toast.makeText(getContext(), "No QR code to download!", Toast.LENGTH_SHORT).show();
            return;
        }

        BitmapDrawable drawable = (BitmapDrawable) imageView25.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        saveImageToGallery(bitmap);
    }

    private void saveImageToGallery(Bitmap bitmap) {
        Context context = getContext();
        if (context == null) return;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/QR Codes");

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            Toast.makeText(context, "Failed to create image URI.", Toast.LENGTH_SHORT).show();
            return;
        }

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                Toast.makeText(context, "QR code saved to Downloads", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("SaveImageError", "Error saving image: " + e.getMessage());
            Toast.makeText(context, "Failed to save QR code.", Toast.LENGTH_SHORT).show();
        }
    }
}
