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
 * Use the {@link CashInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashInFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CashInFragment() {
        // Required empty public constructor
    }

    public static CashInFragment newInstance(String param1, String param2) {
        CashInFragment fragment = new CashInFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_in, container, false);

        // Find the ImageView by its ID
        ImageView imageView19 = view.findViewById(R.id.imageView19);

        // Set an OnClickListener on the ImageView
        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of HomeFragment
                HomeFragment homeFragment = new HomeFragment();

                // Replace the current fragment with HomeFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, homeFragment);
                transaction.addToBackStack(null); // Add this transaction to the back stack
                transaction.commit();
            }
        });

        return view;
    }
}
