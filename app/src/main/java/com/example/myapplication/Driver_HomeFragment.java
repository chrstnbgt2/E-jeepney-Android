package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.cardview.widget.CardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Driver_HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Driver_HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Driver_HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Driver_HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Driver_HomeFragment newInstance(String param1, String param2) {
        Driver_HomeFragment fragment = new Driver_HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_driver__home, container, false);

        // Find the button2 for CashInFragment
        Button button2 = view.findViewById(R.id.button2);

        // Set an OnClickListener on button2
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the CashInFragment
                CashInFragment cashInFragment = new CashInFragment();

                // Replace the current fragment with CashInFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, cashInFragment);
                transaction.addToBackStack(null);  // Optional, adds the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        // Find the qr_scanner_card for QR_ScannerFragment
        CardView qrScannerCard = view.findViewById(R.id.qr_scanner_card);

        // Set an OnClickListener on qr_scanner_card
        qrScannerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the QR_ScannerFragment
                QR_ScannerFragment qrScannerFragment = new QR_ScannerFragment();

                // Replace the current fragment with QR_ScannerFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, qrScannerFragment);
                transaction.addToBackStack(null);  // Optional, adds the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        // Find the qr_share_card for QRFragment
        CardView qrShareCard = view.findViewById(R.id.qr_share_card);

        // Set an OnClickListener on qr_share_card
        qrShareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the QRFragment
                Driver_QRFragment driver_qrFragment = new Driver_QRFragment();

                // Replace the current fragment with QRFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_driver, driver_qrFragment);
                transaction.addToBackStack(null);  // Optional, adds the transaction to the back stack so the user can navigate back
                transaction.commit();
            }
        });

        // Find the seat_card for CheckSeatFragment
        CardView seatCard = view.findViewById(R.id.seat_card);

        // Set an OnClickListener on seat_card
        seatCard.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
}
