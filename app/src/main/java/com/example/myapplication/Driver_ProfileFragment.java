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
 * Use the {@link Driver_ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Driver_ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;

    public Driver_ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Driver_ProfileFragment.
     */
    public static Driver_ProfileFragment newInstance(String param1, String param2) {
        Driver_ProfileFragment fragment = new Driver_ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver__profile, container, false);

        // Find the button by its correct ID
        Button button = view.findViewById(R.id.BTnOut);

        // Find the ProgressBar by its correct ID
        progressBar = view.findViewById(R.id.progressBar);

        // Set an OnClickListener on the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Simulate a delay using a Handler to mimic a loading process
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        progressBar.setVisibility(View.GONE);

                        // Create an Intent to start MainActivity2
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
                    }
                }, 4000); // Delay of 4 seconds
            }
        });

        return view;
    }
}
