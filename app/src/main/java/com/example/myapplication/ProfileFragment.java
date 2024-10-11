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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button button;
    private ProgressBar progressBar;

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

        // Find the button and progress bar by ID
        button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);

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

        return view;
    }
}
