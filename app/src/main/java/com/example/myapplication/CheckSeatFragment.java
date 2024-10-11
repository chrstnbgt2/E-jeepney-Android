package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckSeatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckSeatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckSeatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckSeatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckSeatFragment newInstance(String param1, String param2) {
        CheckSeatFragment fragment = new CheckSeatFragment();
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
        View view = inflater.inflate(R.layout.fragment_check_seat, container, false);

        // Find the ImageView by its ID
        @SuppressLint("CutPasteId") ImageView imageView = view.findViewById(R.id.imageView24);

        // Set a click listener on the ImageView
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of Driver_HomeFragment
                Conductor_Check_SeatFragment conductor_checkSeatFragment = new Conductor_Check_SeatFragment ();

                // Start a fragment transaction to replace the current fragment with Driver_HomeFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_driver, conductor_checkSeatFragment);

                // Add to back stack if you want to allow back navigation
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        // Find the ImageView by its ID
        @SuppressLint("CutPasteId") ImageView imageview = view.findViewById(R.id.imageView24);

        // Set a click listener on the ImageView
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of Driver_HomeFragment
                HomeFragment homeFragment = new HomeFragment();

                // Start a fragment transaction to replace the current fragment with Driver_HomeFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container,homeFragment);

                // Add to back stack if you want to allow back navigation
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
