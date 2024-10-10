package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_ConductorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_ConductorFragment extends Fragment {

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Declare status dots
    private View statusDot;
    private View statusDot2;

    public My_ConductorFragment() {
        // Required empty public constructor
    }

    public static My_ConductorFragment newInstance(String param1, String param2) {
        My_ConductorFragment fragment = new My_ConductorFragment();
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
        View view = inflater.inflate(R.layout.fragment_my__conductor, container, false);

        /*setupNavigationButtons(view);*/

        // Initialize status dots
        statusDot = view.findViewById(R.id.statusDot);
        statusDot2 = view.findViewById(R.id.statusDot2);

        // Find the button in the inflated layout
        Button button = view.findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of MyConductor2Fragment
                MyConductor2Fragment fragment = new MyConductor2Fragment();

                // Begin a fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with MyConductor2Fragment
                transaction.replace(R.id.frame_conductor, fragment); // Ensure 'frame_conductor' is your actual container ID
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit(); // Commit the transaction
            }
        });

        // Find the image view in the inflated layout
        ImageView imageView = view.findViewById(R.id.imageView13);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // Find linear2 in the inflated layout and set click listener for opening View_MoreFragment
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of View_MoreFragment
                View_moreFragment viewMoreFragment = new View_moreFragment();

                // Begin a fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with View_MoreFragment
                transaction.replace(R.id.frame_conductor, viewMoreFragment); // Ensure 'frame_conductor' is your container ID
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit(); // Commit the transaction
            }
        });

        return view;
    }

    // Method to show the dialog
    private void showDialog() {
        // Create a dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_conductor_options); // Use the dialog layout XML you provided

        // Set up dialog options
        LinearLayout setStatusOption = dialog.findViewById(R.id.set_status_option);
        LinearLayout editDetailsOption = dialog.findViewById(R.id.edit_details_option);
        LinearLayout viewMoreOption = dialog.findViewById(R.id.view_more_option);

        // Set click listeners for the options
        setStatusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the status dialog when set status option is clicked
                showStatusDialog();
                dialog.dismiss(); // Close the original dialog
            }
        });

        editDetailsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of Edit_DetailsFragment
                Edit_DetailsFragment editDetailsFragment = new Edit_DetailsFragment();

                // Begin a fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with Edit_DetailsFragment
                transaction.replace(R.id.frame_conductor, editDetailsFragment); // Ensure 'frame_conductor' is the container ID where you want to place the fragment
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit(); // Commit the transaction

                dialog.dismiss(); // Close the dialog after starting the fragment
            }
        });

        viewMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of View_MoreFragment
                View_moreFragment viewMoreFragment = new View_moreFragment();

                // Begin a fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with View_MoreFragment
                transaction.replace(R.id.frame_conductor, viewMoreFragment); // Ensure 'frame_conductor' is the container ID where you want to place the fragment
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit(); // Commit the transaction

                dialog.dismiss(); // Close the dialog after starting the fragment
            }
        });

        // Show the dialog
        dialog.show();
    }

    // Method to show the status dialog
    private void showStatusDialog() {
        // Create a dialog
        Dialog statusDialog = new Dialog(getActivity());
        statusDialog.setContentView(R.layout.dialog_set_status); // Use the set status dialog layout

        // Find the buttons in the status dialog
        Button btnActive = statusDialog.findViewById(R.id.btn_active);
        Button btnInactive = statusDialog.findViewById(R.id.btn_inactive);

        // Set click listeners for the buttons
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDot.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dot_green));
                Toast.makeText(getActivity(), "Status set to Active", Toast.LENGTH_SHORT).show();
                statusDialog.dismiss(); // Close the dialog after setting the status
            }
        });

        btnInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDot.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dot_orange));
                Toast.makeText(getActivity(), "Status set to Inactive", Toast.LENGTH_SHORT).show();
                statusDialog.dismiss(); // Close the dialog after setting the status
            }
        });

        // Show the status dialog
        statusDialog.show();
    }
}
