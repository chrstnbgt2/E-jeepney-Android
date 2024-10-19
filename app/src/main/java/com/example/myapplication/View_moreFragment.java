package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link View_moreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class View_moreFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public View_moreFragment() {
        // Required empty public constructor
    }

    public static View_moreFragment newInstance(String param1, String param2) {
        View_moreFragment fragment = new View_moreFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_more, container, false);

        // Find the ImageView and set the click listener
        ImageView imageView = view.findViewById(R.id.imageView24); // Adjust the ID if necessary
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyConductorFragment();
            }
        });

        return view;
    }

    private void openMyConductorFragment() {
        My_ConductorFragment fragment = new My_ConductorFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_conductor, fragment); // Make sure to use the correct container ID
        fragmentTransaction.addToBackStack(null); // Optional, if you want to add it to the back stack
        fragmentTransaction.commit();
    }
}
