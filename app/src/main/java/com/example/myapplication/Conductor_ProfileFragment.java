package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Conductor_ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Conductor_ProfileFragment extends Fragment {

    // Rename parameter arguments, choose names that match the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Conductor_ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Conductor_ProfileFragment.
     */
    public static Conductor_ProfileFragment newInstance(String param1, String param2) {
        Conductor_ProfileFragment fragment = new Conductor_ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_conductor__profile, container, false);

        // Get reference to the ProgressBar
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        // Initially hide the ProgressBar
        progressBar.setVisibility(View.GONE);

        // Get reference to the Button
        Button button10 = view.findViewById(R.id.button10);

        // Set OnClickListener to the Button
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Use Handler to introduce a delay of 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar after 4 seconds
                        progressBar.setVisibility(View.GONE);
                    }
                }, 4000); // 4 seconds delay
            }
        });

        // Other existing code...

        // Get reference to LinearLayout (linear1)
        LinearLayout myConductorLayout1 = view.findViewById(R.id.linear1);

        // Set OnClickListener to open MyConductorFragment
        myConductorLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of MyConductorFragment
                My_ConductorFragment myConductorFragment = new My_ConductorFragment();

                // Perform the fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, myConductorFragment); // Use the container ID of the FrameLayout
                transaction.addToBackStack(null); // Add to backstack to allow user to navigate back
                transaction.commit();
            }
        });

        // Get reference to LinearLayout (linear2)
        LinearLayout myConductorLayout2 = view.findViewById(R.id.linear2);

        // Set OnClickListener to open MyConductorFragment
        myConductorLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of MyConductorFragment
                My_ConductorFragment myConductorFragment = new My_ConductorFragment();

                // Perform the fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, myConductorFragment); // Use the container ID of the FrameLayout
                transaction.addToBackStack(null); // Add to backstack to allow user to navigate back
                transaction.commit();
            }
        });

        return view;
    }
}