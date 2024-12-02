package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Conductor_QR_ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Conductor_QR_ShareFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Conductor_QR_ShareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Conductor_QR_ShareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Conductor_QR_ShareFragment newInstance(String param1, String param2) {
        Conductor_QR_ShareFragment fragment = new Conductor_QR_ShareFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_conductor__q_r__share, container, false);

        // Find the button by ID
        Button btnCustomQR = rootView.findViewById(R.id.Btn_customQR_code);

        // Set up click listener for the button
        btnCustomQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the dialog
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_layout);  // Set the custom layout for the dialog
                dialog.setTitle("Custom QR Code Dialog");  // Set the dialog title

                // Show the dialog
                dialog.show();
            }
        });
        // Find the ImageView by ID
        ImageView imageView24 = rootView.findViewById(R.id.imageView24);

        // Set up click listener for the ImageView
        imageView24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of QR_ScannerFragment
                QR_ScannerFragment qrScannerFragment = new QR_ScannerFragment();

                // Begin a fragment transaction to replace the current fragment with QR_ScannerFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, qrScannerFragment); // Replace with your container's ID
                transaction.addToBackStack(null); // Optional: add to back stack for navigation back
                transaction.commit();
            }
        });

        return rootView;
    }
}
