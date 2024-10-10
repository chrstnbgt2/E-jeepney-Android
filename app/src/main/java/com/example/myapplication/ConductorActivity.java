package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityConductorBinding;

public class ConductorActivity extends AppCompatActivity {

    ActivityConductorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityConductorBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_conductor);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the initial fragment to HomeFragment
        if (savedInstanceState == null) {
            replaceFragment(new ConductorFragment()); // Load the HomeFragment by default
            binding.botnav.setSelectedItemId(R.id.nav_home2); // Set the home item as selected
        }

        binding.botnav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home2) {
                replaceFragment(new ConductorFragment());
            } else if (item.getItemId() == R.id.nav_seat) {
                replaceFragment(new CheckSeatFragment());
            } else if (item.getItemId() == R.id.nav_history2) {
                replaceFragment(new HistoryFragment());
            } else if (item.getItemId() == R.id.nav_profile2) {
                replaceFragment(new Conductor_ProfileFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_conductor, fragment);
        fragmentTransaction.commit();
    }
}
