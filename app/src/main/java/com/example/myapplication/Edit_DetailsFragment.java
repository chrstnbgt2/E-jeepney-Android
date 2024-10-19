package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_DetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Edit_DetailsFragment() {
        // Required empty public constructor
    }

    public static Edit_DetailsFragment newInstance(String param1, String param2) {
        Edit_DetailsFragment fragment = new Edit_DetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit__details, container, false);

        // Set up the ImageView click listener
        ImageView imageView = view.findViewById(R.id.imageView24);
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
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_conductor, fragment); // Ensure you have a FrameLayout with this ID in your layout
        transaction.addToBackStack(null); // Optional: to add the transaction to the back stack
        transaction.commit();
    }
}
