package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

public class QR_ScannerFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private DecoratedBarcodeView cameraView;

    public QR_ScannerFragment() {
        // Required empty public constructor
    }

    public static QR_ScannerFragment newInstance(String param1, String param2) {
        QR_ScannerFragment fragment = new QR_ScannerFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r__scanner, container, false);

        // Initialize the cameraView for scanning QR codes
        cameraView = view.findViewById(R.id.cameraView);
        checkCameraPermission();

        // Start QR code scanning
        cameraView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String qrCode = result.getText();
                Toast.makeText(getActivity(), "Scanned: " + qrCode, Toast.LENGTH_SHORT).show();
                // Handle QR code result here
            }

            @Override
            public void possibleResultPoints(List<com.google.zxing.ResultPoint> resultPoints) {
                // Optional: Handle possible result points (for UI effects, if needed)
            }
        });

        // Set up the back button (imageView24)
        ImageView imageView24 = view.findViewById(R.id.imageView24);
        imageView24.setOnClickListener(v -> {
            // Navigate to Driver_HomeFragment
            Driver_HomeFragment driverHomeFragment = new Driver_HomeFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_driver, driverHomeFragment);
            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
            transaction.commit();
        });
        // Set up the QR share button
        Button btnQRShare = view.findViewById(R.id.btn_QR_share);
        btnQRShare.setOnClickListener(v -> {
            // Replace the current fragment with QRFragment
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_driver, new Conductor_QR_ShareFragment());
            fragmentTransaction.addToBackStack(null); // Add to back stack to allow navigation back
            fragmentTransaction.commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.resume(); // Resume the camera when the fragment is active
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.pause(); // Pause the camera when the fragment is not active
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            cameraView.resume(); // Permission already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraView.resume();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required for scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
