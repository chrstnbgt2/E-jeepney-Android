package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityDriverBinding;

public class DriverActivity extends AppCompatActivity {

    ActivityDriverBinding binding;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Set the layout

        // Set the initial fragment to HomeFragment
        if (savedInstanceState == null) {
            replaceFragment(new Driver_HomeFragment()); // Load the HomeFragment by default
            binding.botnav3.setSelectedItemId(R.id.nav_home3); // Set the home item as selected
        }

        binding.botnav3.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home3) {
                replaceFragment(new Driver_HomeFragment());
            } else if (item.getItemId() == R.id.nav_tracker3) {
                replaceFragment(new TrackerFragment());
            } else if (item.getItemId() == R.id.nav_QrCode3) {
                replaceFragment(new QR_ScannerFragment());
            } else if (item.getItemId() == R.id.nav_history3) {
                replaceFragment(new HistoryFragment());
            } else if (item.getItemId() == R.id.nav_profile3) {
                replaceFragment(new Driver_ProfileFragment());
            }

            return true;
        });
    }

    // Method for fragment replacement
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_driver, fragment);
        fragmentTransaction.commit();
    }
}
