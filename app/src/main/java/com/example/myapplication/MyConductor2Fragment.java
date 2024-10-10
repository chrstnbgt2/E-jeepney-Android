package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyConductor2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyConductor2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyConductor2Fragment() {
        // Required empty public constructor
    }

    public static MyConductor2Fragment newInstance(String param1, String param2) {
        MyConductor2Fragment fragment = new MyConductor2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_my_conductor2, container, false);

        // Find the ImageView
        ImageView backButton = view.findViewById(R.id.imageView24);

        // Set an OnClickListener to go back to MyConductorFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform fragment transaction to replace MyConductor2Fragment with MyConductorFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conductor, new My_ConductorFragment());
                transaction.addToBackStack(null); // Optional: Add to back stack so you can return to MyConductor2Fragment if needed
                transaction.commit();
            }
        });

        return view;
    }
}
