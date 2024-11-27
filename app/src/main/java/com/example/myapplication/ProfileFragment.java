package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button button;
    private ProgressBar progressBar;
    private View linear2; // Add reference for LinearLayout

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the button, progress bar, and LinearLayout by ID
        button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        linear2 = view.findViewById(R.id.linear2);

        // Set an onClickListener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Delay for a short period to simulate loading
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Start MainActivity2
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);

                        // Hide the progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                }, 1000); // 1-second delay
            }
        });

        // Set an onClickListener for LinearLayout (linear2)
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiscountFragment();
            }
        });

        return view;
    }

    // Method to open DiscountFragment
    private void openDiscountFragment() {
        Fragment discountFragment = new DiscountFragment();
        FragmentManager fragmentManager = getParentFragmentManager(); // Use parent FragmentManager
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, discountFragment); // Ensure fragment_container exists
        transaction.addToBackStack(null); // Optional: adds transaction to back stack
        transaction.commit();
    }
}
