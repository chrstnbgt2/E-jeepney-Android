package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMain5Binding;

public class MainActivity5 extends AppCompatActivity {

    ActivityMain5Binding binding;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain5Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main5);
        setContentView(binding.getRoot());

        // Set the initial fragment to HomeFragment
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment()); // Load the HomeFragment by default
            binding.botnav.setSelectedItemId(R.id.nav_home); // Set the home item as selected
        }

        binding.botnav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_tracker) {
                replaceFragment(new TrackerFragment());
            } else if (item.getItemId() == R.id.nav_QrCode) {
                replaceFragment(new MyQrFragment());
            } else if (item.getItemId() == R.id.nav_history) {
                replaceFragment(new HistoryFragment());
            } else if (item.getItemId() == R.id.nav_profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}
