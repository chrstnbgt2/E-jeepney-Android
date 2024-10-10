package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Driver_QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Driver_QRFragment extends Fragment {

    // Fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Driver_QRFragment() {
        // Required empty public constructor
    }

    public static Driver_QRFragment newInstance(String param1, String param2) {
        Driver_QRFragment fragment = new Driver_QRFragment();
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
        View view = inflater.inflate(R.layout.fragment_driver__q_r, container, false);

        // Find the button by its ID and set an OnClickListener
        Button button = view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and display the dialog when the button is clicked
                showPassengerTypeDialog();
            }
        });

        return view;
    }

    // Method to show the dialog for selecting the type of passenger
    private void showPassengerTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        // Inflate your custom dialog layout
        View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogLayout);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();
    }
}
