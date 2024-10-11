package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Conductor_Check_SeatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Conductor_Check_SeatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Conductor_Check_SeatFragment() {
        // Required empty public constructor
    }

    public static Conductor_Check_SeatFragment newInstance(String param1, String param2) {
        Conductor_Check_SeatFragment fragment = new Conductor_Check_SeatFragment();
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
        View view = inflater.inflate(R.layout.fragment_conductor__check__seat, container, false);

        // Find the button2 by its ID and set an OnClickListener
        Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the CheckSeatFragment
                CheckSeatFragment checkSeatFragment = new CheckSeatFragment();

                // Replace the current fragment with CheckSeatFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, checkSeatFragment);
                transaction.addToBackStack(null);  // Optional, adds the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });
        // Find the ImageView24 by its ID and set an OnClickListener
        ImageView imageView24 = view.findViewById(R.id.imageView24);
        imageView24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the Driver_HomeFragment
                Driver_HomeFragment driverHomeFragment = new Driver_HomeFragment();

                // Replace the current fragment with Driver_HomeFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, driverHomeFragment);
                transaction.addToBackStack(null);  // Optional, adds the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        return view;
    }
}