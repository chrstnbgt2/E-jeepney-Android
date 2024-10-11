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
 * Use the {@link CashInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CashInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CashInFragment.
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_in, container, false);

        // Find the ImageView by its ID
        ImageView imageView19 = view.findViewById(R.id.imageView19);

        // Set an OnClickListener on the ImageView
        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of Driver_HomeFragment
                HomeFragment homeFragment = new HomeFragment();

                // Replace the current fragment with Driver_HomeFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,homeFragment);
                transaction.addToBackStack(null); // Add this transaction to the back stack
                transaction.commit();
            }
        });
        // Find the ImageView by its ID
        @SuppressLint("CutPasteId") ImageView imageView = view.findViewById(R.id.imageView19);

        // Set an OnClickListener on the ImageView
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of Driver_HomeFragment
                Driver_HomeFragment homeFragment = new Driver_HomeFragment();

                // Replace the current fragment with Driver_HomeFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver,homeFragment);
                transaction.addToBackStack(null); // Add this transaction to the back stack
                transaction.commit();
            }
        });


        return view;
    }
}
